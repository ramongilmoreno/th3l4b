package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;

public class ReverseLinks extends DirectedGraphFilter {

	public ReverseLinks() {
		super();
	}

	public ReverseLinks(IDirectedGraph delegated) {
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
