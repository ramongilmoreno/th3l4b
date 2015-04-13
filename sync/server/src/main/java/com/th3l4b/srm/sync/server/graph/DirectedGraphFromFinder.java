package com.th3l4b.srm.sync.server.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.sync.server.generated.IServerSyncFinder;
import com.th3l4b.srm.sync.server.generated.entities.IMerge;

/**
 * Builds a graph from the data in the {@link IServerSyncFinder} by following
 * links {@link IMerge} in the direction {@link IMerge#getTo()} to
 * {@link IMerge#getFrom()}
 */
public class DirectedGraphFromFinder implements IDirectedGraph {
	String _start;
	Collection<String> _fromStart = new HashSet<String>();
	private IServerSyncFinder _finder;

	public DirectedGraphFromFinder(String start, Collection<String> fromStart,
			IServerSyncFinder finder) {
		_start = start;
		_fromStart = fromStart;
		_finder = finder;
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
		if (NullSafe.equals(from, _start)) {
			return _fromStart;
		} else {
			ArrayList<String> r = new ArrayList<String>();
			for (IMerge s : _finder.referencesStatus_ComesFrom(from)) {
				String f = s.getFrom();
				if (f != null) {
					r.add(f);
				}
			}
			return r;
		}
	}

	@Override
	public Collection<String> linksTo(String to) throws Exception {
		if (_fromStart.contains(to)) {
			return Collections.singleton(_start);
		} else {
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
}
