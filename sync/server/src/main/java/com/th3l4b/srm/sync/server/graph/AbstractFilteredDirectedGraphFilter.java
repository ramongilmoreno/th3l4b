package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Filters the acceptable nodes of a graph
 */
public abstract class AbstractFilteredDirectedGraphFilter extends DirectedGraphFilter {

	public AbstractFilteredDirectedGraphFilter(IDirectedGraph delegated) {
		super(delegated);
	}

	protected abstract boolean accept(String node) throws Exception;
	
	@Override
	public Collection<String> linksFrom(String from) throws Exception {
		if (!accept(from)) {
			return Collections.emptySet();
		}
		HashSet<String> r = new HashSet<String>();
		for (String n : super.linksFrom(from)) {
			if (accept(n)) {
				r.add(n);
			}
		}
		return r;
	}

	@Override
	public Collection<String> linksTo(String to) throws Exception {
		if (!accept(to)) {
			return Collections.emptySet();
		}
		HashSet<String> r = new HashSet<String>();
		for (String n : super.linksTo(to)) {
			if (accept(n)) {
				r.add(n);
			}
		}
		return r;
	}
}
