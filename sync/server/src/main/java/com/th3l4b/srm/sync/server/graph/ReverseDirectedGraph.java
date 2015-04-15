package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;

public class ReverseDirectedGraph extends DirectedGraphFilter {

	public ReverseDirectedGraph() {
		super();
	}

	public ReverseDirectedGraph(IDirectedGraph delegated) {
		super(delegated);
	}		
	
	public Collection<String> linksFrom(String from) throws Exception {
		return super.linksTo(from);
	}
	
	@Override
	public Collection<String> linksTo(String to) throws Exception {
		return super.linksFrom(to);
	}
	
}
