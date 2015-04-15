package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;

import com.th3l4b.srm.sync.server.persistence.ISyncServerPersistence;

public class DirectedGraphFromPersistence implements IDirectedGraph {

	String _start;
	private ISyncServerPersistence _persistence;

	public DirectedGraphFromPersistence(String start,
			ISyncServerPersistence persistence) {
		_start = start;
		_persistence = persistence;
	}

	@Override
	public void setRoot(String root) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRoot() throws Exception {
		return _start;
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
