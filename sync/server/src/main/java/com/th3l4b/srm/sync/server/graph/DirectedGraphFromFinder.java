package com.th3l4b.srm.sync.server.graph;

import java.util.ArrayList;
import java.util.Collection;

import com.th3l4b.srm.sync.server.SyncServer;
import com.th3l4b.srm.sync.server.generated.IServerSyncFinder;
import com.th3l4b.srm.sync.server.generated.entities.IMerge;

/**
 * Builds a graph from the data in the {@link IServerSyncFinder} by following
 * links {@link IMerge} in the direction {@link IMerge#getTo()} to
 * {@link IMerge#getFrom()}.
 * 
 * No longer used by the {@link SyncServer} but left for debugging purposes.
 */
public class DirectedGraphFromFinder implements IDirectedGraph {

	private IServerSyncFinder _finder;

	public DirectedGraphFromFinder(IServerSyncFinder finder) {
		_finder = finder;
	}

	@Override
	public void addLink(String from, String to) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> linksFrom(String from) throws Exception {
		ArrayList<String> r = new ArrayList<String>();
		for (IMerge s : _finder.referencesStatus_ComesFrom(from)) {
			String f = s.getFrom();
			if (f != null) {
				r.add(f);
			}
		}
		return r;
	}

	@Override
	public Collection<String> linksTo(String to) throws Exception {
		ArrayList<String> r = new ArrayList<String>();
		for (IMerge s : _finder.referencesStatus_LeadsTo(to)) {
			String f = s.getTo();
			if (f != null) {
				r.add(f);
			}
		}
		return r;
	}
}
