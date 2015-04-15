package com.th3l4b.srm.sync.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.sync.base.SyncUtils;
import com.th3l4b.srm.sync.server.graph.DirectedGraphFromPersistence;
import com.th3l4b.srm.sync.server.graph.DirectedGraphFromPersistenceWithFirstLevel;
import com.th3l4b.srm.sync.server.graph.DirectedGraphUtils;
import com.th3l4b.srm.sync.server.graph.IDirectedGraph;
import com.th3l4b.srm.sync.server.graph.Tarjan1976;
import com.th3l4b.srm.sync.server.graph.TrackLeadsDirectedGraph;
import com.th3l4b.srm.sync.server.persistence.ISyncServerPersistence;
import com.th3l4b.srm.sync.server.persistence.actions.DefaultMoveToOtherStatusAction;
import com.th3l4b.srm.sync.server.persistence.actions.DefaultNewStatusAction;

public class SyncServer {

	public static final boolean LOG = false;
	private ISyncServerPersistence _persistence;

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

	public SyncServer(ISyncServerPersistence persistence) throws Exception {
		_persistence = persistence;
	}

	public ISyncServerPersistence getPersistence() {
		return _persistence;
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
	 * @param client
	 *            The id of the client.
	 * @return The id of the status of the discovered status for the customer
	 *         and the list of updates.
	 */
	public SyncResult discover(String client, Collection<IInstance> updates)
			throws Exception {

		String start = _persistence.clientStatus(client);
		log("Current status: " + start);

		// Find all active statuses from clients
		ArrayList<String> statuses = new ArrayList<String>(
				_persistence.liveStatuses());

		// If only the start status is found, do nothing.
		boolean noUpdates = (updates == null) || (updates.size() == 0);
		if ((statuses.size() == 1)
				&& NullSafe.equals(start, statuses.iterator().next())
				&& noUpdates) {
			return new SyncResult(start);
		} else {
			DefaultNewStatusAction newStatus = null;
			DefaultMoveToOtherStatusAction moveStatus = null;
			boolean reuseStatus = false;
			if (noUpdates && (statuses.size() == 1)) {
				// Need to move to an existing status
				moveStatus = new DefaultMoveToOtherStatusAction();
				moveStatus.setClient(client);
				String targetStatus = statuses.iterator().next();
				moveStatus.setStatus(targetStatus);
				moveStatus.getStatusesToDelete().add(start);
				reuseStatus = true;
			} else {
				// Need to create new status
				newStatus = new DefaultNewStatusAction();
				newStatus.setClient(client);
				newStatus.setUpdates(updates);
				newStatus.setStatusesToDelete(statuses);
				newStatus.setStatusesForNewStatus(statuses);
			}

			// Merge from missing statuses
			Collection<String> missing = reuseStatus ? parentsStatusesForReusedStartStatus(
					moveStatus.getStatus(), start)
					: parentsStatusesForUnexistingStartStatus(statuses, start);
			String resultStatus = null;
			if (reuseStatus) {
				resultStatus = moveStatus.getStatus();
				_persistence.moveToOtherStatus(moveStatus);
			} else {
				resultStatus = _persistence.newStatus(newStatus);
			}

			// Return
			SyncResult r = new SyncResult(resultStatus);
			r._missingStatuses.addAll(missing);
			r._updates = updates(missing, start);

			if (LOG) {
				log("Resulting tree:");
				DirectedGraphUtils.print(new DirectedGraphFromPersistence(
						r._status, _persistence), r._status, System.out);
			}

			return r;
		}

	}

	private Collection<IInstance> updates(Collection<String> statuses,
			String start) throws Exception {
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		for (String s : statuses) {
			if (NullSafe.equals(start, s)) {
				// Do not apply changes from the start status. They are already
				// applied.
				continue;
			}
			r.addAll(_persistence.updates(s));
		}

		return SyncUtils.groupUpdates(r, _persistence.modelRuntime());
	}

	/**
	 * @return A sorted collection of the parent statuses. Updates shall be read
	 *         and applied in the order returned by this method.
	 */
	protected Collection<String> parentsStatusesForUnexistingStartStatus(
			Collection<String> found, String stop) throws Exception {
		// Make up a status name
		String start = "candidate-status-" + UUID.randomUUID().toString();
		DirectedGraphFromPersistenceWithFirstLevel data = new DirectedGraphFromPersistenceWithFirstLevel(
				start, found, _persistence);
		List<String> sorted = parentStatuses(start, stop, data);
		if (sorted.indexOf(start) != (sorted.size() - 1)) {
			throw new IllegalStateException();
		} else {
			sorted.remove((sorted.size() - 1));
		}
		logList("Apply", sorted);
		return sorted;
	}

	protected Collection<String> parentsStatusesForReusedStartStatus(
			String start, String stop) throws Exception {
		DirectedGraphFromPersistence data = new DirectedGraphFromPersistence(
				start, _persistence);
		List<String> sorted = parentStatuses(start, stop, data);
		logList("Apply", sorted);
		return sorted;
	}

	private List<String> parentStatuses(String start, String stop,
			IDirectedGraph data) throws Exception {
		TrackLeadsDirectedGraph builder = new TrackLeadsDirectedGraph(data);
		builder.addLead(start);
		builder.ignore(stop);

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
		return sorted;
	}
}
