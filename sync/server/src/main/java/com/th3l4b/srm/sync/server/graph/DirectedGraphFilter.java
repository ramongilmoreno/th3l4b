package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;

public class DirectedGraphFilter implements IDirectedGraph {

	IDirectedGraph _delegated;

	public DirectedGraphFilter() {
	}

	public DirectedGraphFilter(IDirectedGraph delegated) {
		_delegated = delegated;
	}

	public IDirectedGraph getDelegated() {
		return _delegated;
	}

	public void setDelegated(IDirectedGraph delegated) {
		_delegated = delegated;
	}

	public void setRoot(String root) throws Exception {
		_delegated.setRoot(root);
	}

	public String getRoot() throws Exception {
		return _delegated.getRoot();
	}

	public void addLink(String from, String to) throws Exception {
		_delegated.addLink(from, to);
	}

	public Collection<String> linksFrom(String from) throws Exception {
		return _delegated.linksFrom(from);
	}

	public Collection<String> linksTo(String to) throws Exception {
		return _delegated.linksTo(to);
	}

}
