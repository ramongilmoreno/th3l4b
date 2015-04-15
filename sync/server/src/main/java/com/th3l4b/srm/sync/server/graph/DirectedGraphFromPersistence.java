package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;

import com.th3l4b.srm.sync.server.persistence.ISyncServerPersistence;

public class DirectedGraphFromPersistence implements IDirectedGraph {

	private ISyncServerPersistence _persistence;

	public DirectedGraphFromPersistence(ISyncServerPersistence persistence) {
		_persistence = persistence;
	}

	@Override
	public void addLink(String from, String to) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> linksFrom(String from) throws Exception {
		return _persistence.statusesThisStatusDependsOn(from);
	}

	@Override
	public Collection<String> linksTo(String to) throws Exception {
		return _persistence.statusesThatDependsOnThisStatus(to);
	}

}
