package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;

public interface IDirectedGraph {
	void addLink (String from, String to) throws Exception;
	Collection<String> linksFrom (String from) throws Exception;
	Collection<String> linksTo (String to) throws Exception;
}