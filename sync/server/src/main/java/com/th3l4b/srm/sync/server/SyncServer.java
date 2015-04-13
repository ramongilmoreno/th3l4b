package com.th3l4b.srm.sync.server;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.json.Generator;
import com.th3l4b.srm.json.IJsonModelRuntime;
import com.th3l4b.srm.json.JsonUtils;
import com.th3l4b.srm.json.Parser;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.sync.base.SyncUtils;
import com.th3l4b.srm.sync.server.generated.IServerSyncFinder;
import com.th3l4b.srm.sync.server.generated.ServerSyncModelUtils;
import com.th3l4b.srm.sync.server.generated.entities.IClient;
import com.th3l4b.srm.sync.server.generated.entities.IMerge;
import com.th3l4b.srm.sync.server.generated.entities.IStatus;
import com.th3l4b.srm.sync.server.graph.DirectedGraphFromFinder;
import com.th3l4b.srm.sync.server.graph.DirectedGraphUtils;
import com.th3l4b.srm.sync.server.graph.Tarjan1976;
import com.th3l4b.srm.sync.server.graph.TrackLeadsDirectedGraph;

public class SyncServer {

	public static final boolean LOG = false;

	public void log(String msg) {
		if (LOG) {
			System.out.println(msg);
		}
	}

	public <T> void log(String msg, Collection<T> c) {
		if (LOG) {
			log(msg);
			TreeSet<T> ts = new TreeSet<T>(c);
			for (T i : ts) {
				System.out.println("    * " + i);
			}
		}
	}

	public <T> void logList(String msg, List<T> l) {
		if (LOG) {
			log(msg);
			int j = 0;
			for (T i : l) {
				System.out
						.println("    " + (j < 10 ? "0" : "") + j + " - " + i);
				j++;
			}
		}
	}

	private IRuntime _repository;

	public SyncServer(IRuntime repository) throws Exception {
		_repository = repository;
	}

	public IRuntime getRepository() {
		return _repository;
	}

	public class SyncResult {
		public String _status;
		public Collection<String> _missingStatuses = new ArrayList<String>();
		public Collection<IInstance> _updates;

		public SyncResult(String status) {
			_status = status;
		}
	}

	/**
	 * Discovers the new status of a client (first step of the synchronization).
	 * 
	 * @param clientId
	 *            The id of the client. If na {@link IClient} instance does not
	 *            exist, this method creates one.
	 * @return The ID of the {@link IStatus} of the discovered status for the
	 *         customer and the list of updates.
	 */
	public SyncResult discover(String clientId, Collection<IInstance> updates,
			IModelRuntime runtime) throws Exception {
		ServerSyncModelUtils utils = new ServerSyncModelUtils(_repository);
		ArrayList<IInstance> changes = new ArrayList<IInstance>();

		// Locate the client
		IServerSyncFinder finder = utils.finder();
		IClient client = finder.findClient(clientId);

		// Find all active statuses from clients
		ArrayList<String> statuses = new ArrayList<String>();
		for (IStatus s : finder.allStatus()) {
			statuses.add(s.coordinates().getIdentifier().getKey());
		}

		// If only the start status is found, do nothing.
		String start = client.getStatus();
		if ((statuses.size() == 1)
				&& NullSafe.equals(start, statuses.iterator().next())
				&& ((updates == null) || (updates.size() == 0))) {
			return new SyncResult(start);
		} else {
			// Need to create new status
			IStatus newStatus = utils.createStatus();
			// Place updates on it
			if ((updates != null) && (updates.size() > 0)) {
				StringWriter sw = new StringWriter();
				IJsonModelRuntime jr = JsonUtils.runtime(runtime);
				Generator generator = new Generator(jr, sw);
				try {
					generator.write(updates);
				} finally {
					generator.close();
				}
				newStatus.setContents(sw.getBuffer().toString());
				log("Changes at: " + newStatus);
			}
			changes.add(newStatus);

			// Delete parent status if no yet deleted
			IStatus originalStatus = utils.createStatus();
			originalStatus.coordinates().getIdentifier().setKey(start);
			originalStatus.coordinates().setStatus(EntityStatus.ToDelete);
			changes.add(originalStatus);

			// Update client
			client.setStatus(newStatus);
			client.coordinates().setStatus(EntityStatus.ToMerge);
			changes.add(client);

			// Merge from missing statuses
			Collection<String> missing = parentsStatuses(finder, newStatus
					.coordinates().getIdentifier().getKey(), statuses, start);
			for (String parent : statuses) {
				IMerge merge = utils.createMerge();
				merge.setTo(newStatus);
				merge.setFrom(parent);
				changes.add(merge);

				IStatus deleteStatus = utils.createStatus();
				ICoordinates coordinates = deleteStatus.coordinates();
				coordinates.getIdentifier().setKey(parent);
				coordinates.setStatus(EntityStatus.ToDelete);
				changes.add(deleteStatus);
			}
			utils.getRuntime().updater().update(changes);

			// Return
			SyncResult r = new SyncResult(newStatus.coordinates()
					.getIdentifier().getKey());
			r._missingStatuses.addAll(missing);
			r._updates = updates(missing, start, runtime);
			return r;
		}

	}

	private Collection<IInstance> updates(Collection<String> statuses,
			String start, IModelRuntime runtime) throws Exception {
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		IJsonModelRuntime jr = JsonUtils.runtime(runtime);
		ServerSyncModelUtils utils = new ServerSyncModelUtils(getRepository());
		IServerSyncFinder finder = utils.finder();
		for (String s : statuses) {
			if (NullSafe.equals(start, s)) {
				// Do not apply changes from the start status. They are already
				// applied.
				continue;
			}
			String contents = finder.findStatus(s).getContents();
			if (contents != null) {
				StringReader input = new StringReader(contents);
				Parser parser = new Parser(jr, input);
				try {
					r.addAll(parser.parse(false, true)._many);
				} finally {
					parser.close();
				}
			}
		}

		return SyncUtils.groupUpdates(r, runtime);
	}

	/**
	 * @return A sorted collection of the parent statuses. Updates shall be read
	 *         and applied in the order returned by this method.
	 */
	protected Collection<String> parentsStatuses(IServerSyncFinder finder,
			String start, Collection<String> found, String stop)
			throws Exception {

		DirectedGraphFromFinder data = new DirectedGraphFromFinder(start,
				found, finder);
		TrackLeadsDirectedGraph builder = new TrackLeadsDirectedGraph(data);
		builder.addLead(start);

		// Ignore all leading to the stop
		for (IMerge m : finder.referencesStatus_ComesFrom(stop)) {
			builder.ignore(m.getFrom());
		}

		// Follow leads until nothing is left
		while (!builder.leads().isEmpty()) {
			String status = builder.leads().iterator().next();
			builder.setVisited(status);
			for (String lead : builder.linksFrom(status)) {
				builder.addLead(lead);
			}
		}
		log("Found statuses", builder.visited());
		log("Found tree");
		if (LOG) {
			DirectedGraphUtils.print(builder, start, System.out);
		}
		// Collect all links and sort them out
		List<String> sorted = new Tarjan1976().sort(builder);
		if (sorted.indexOf(start) != (sorted.size() - 1)) {
			throw new IllegalStateException();
		} else {
			sorted.remove((sorted.size() - 1));
		}
		logList("Apply", sorted);
		return sorted;
	}
}
