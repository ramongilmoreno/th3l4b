package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.sync.server.persistence.ISyncServerPersistence;

public class DirectedGraphFromPersistenceWithFirstLevel extends
		DirectedGraphFromPersistence {

	Collection<String> _fromStart = new HashSet<String>();

	public DirectedGraphFromPersistenceWithFirstLevel(String start,
			Collection<String> fromStart, ISyncServerPersistence persistence)
			throws Exception {
		super(start, persistence);
		_fromStart = fromStart;
	}

	@Override
	public Collection<String> linksFrom(String from) throws Exception {
		if (NullSafe.equals(from, _start)) {
			return _fromStart;
		} else {
			return super.linksFrom(from);
		}
	}

	@Override
	public Collection<String> linksTo(String to) throws Exception {
		if (_fromStart.contains(to)) {
			return Collections.singleton(_start);
		} else {
			return super.linksTo(to);
		}
	}
}
