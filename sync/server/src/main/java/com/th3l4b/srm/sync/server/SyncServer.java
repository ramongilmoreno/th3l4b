package com.th3l4b.srm.sync.server;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import com.th3l4b.common.data.NullSafe;
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
import com.th3l4b.srm.sync.server.graph.Tarjan1976;
import com.th3l4b.srm.sync.server.graph.TrackLeadsDirectedGraph;

public class SyncServer {

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
	public SyncResult discover(String clientId) throws Exception {
		ServerSyncModelUtils utils = new ServerSyncModelUtils(_repository);
		ArrayList<IInstance> changes = new ArrayList<IInstance>();

		// Locate the client
		IServerSyncFinder finder = utils.finder();
		IClient client = finder.findClient(clientId);
		boolean clientAdded = false;
		if (client.coordinates().getStatus() == EntityStatus.Unknown) {
			client = utils.createClient();
			client.coordinates().getIdentifier().setKey(clientId);
			changes.add(client);
			clientAdded = true;
		}

		// Find all active statuses
		ArrayList<String> statuses = new ArrayList<String>();
		for (IStatus s : finder.allStatus()) {
			statuses.add(s.coordinates().getIdentifier().getKey());
		}

		// If only the start status is found, do nothing.
		String start = client.getStatus();
		if ((start != null) && (statuses.size() == 1)
				&& NullSafe.equals(start, statuses.iterator().next())) {
			return new SyncResult(start);
		} else {
			// Need to create new status
			IStatus discoveredStatus = utils.createStatus();
			changes.add(discoveredStatus);

			Collection<String> missing = parentsStatuses(finder,
					discoveredStatus.coordinates().getIdentifier().getKey(),
					statuses, client.getStatus());

			// Update client
			if (!clientAdded) {
				changes.add(client);
				clientAdded = true;
			}
			client.setStatus(discoveredStatus);

			// Merge from missing statuses
			for (String parent : missing) {
				IMerge merge = utils.createMerge();
				merge.setTo(discoveredStatus);
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
			SyncResult r = new SyncResult(discoveredStatus.coordinates()
					.getIdentifier().getKey());
			r._missingStatuses.addAll(missing);

			return r;
		}

	}

	public Collection<IInstance> updates(Collection<String> statuses,
			IModelRuntime runtime) throws Exception {
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		IJsonModelRuntime jr = JsonUtils.runtime(runtime);
		ServerSyncModelUtils utils = new ServerSyncModelUtils(getRepository());
		IServerSyncFinder finder = utils.finder();
		for (String s : statuses) {
			String contents = finder.findStatus(s).getContents();
			if (contents != null) {
				StringReader input = new StringReader(contents);
				r.addAll(new Parser(jr, input).parse(false, true)._many);
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
		builder.ignore(stop);

		// Follow leads until nothing is left
		while (!builder.leads().isEmpty()) {
			String status = builder.leads().iterator().next();
			builder.setVisited(status);
			for (String lead : builder.linksFrom(status)) {
				builder.addLead(lead);
			}
		}

		// Collect all links and sort them out
		return new Tarjan1976().sort(builder);
	}
}
