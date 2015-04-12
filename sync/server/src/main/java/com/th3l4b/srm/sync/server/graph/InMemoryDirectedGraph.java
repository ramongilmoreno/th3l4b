package com.th3l4b.srm.sync.server.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InMemoryDirectedGraph implements IDirectedGraph {

	private String _root;
	private HashMap<String, Collection<String>> _froms = new HashMap<String, Collection<String>>();
	private HashMap<String, Collection<String>> _tos = new HashMap<String, Collection<String>>();

	protected static Collection<String> nonNullGet(String n,
			Map<String, Collection<String>> map) {
		if (map.containsKey(n)) {
			return map.get(n);
		} else {
			return Collections.<String> emptyList();
		}
	}

	protected static void nonNullAdd(String n, String m,
			Map<String, Collection<String>> map) {
		if (!map.containsKey(n)) {
			map.put(n,  new HashSet<String>());
		}
		map.get(n).add(m);
	}

	@Override
	public void setRoot(String root) throws Exception {
		_root = root;
	}

	@Override
	public String getRoot() throws Exception {
		return _root;
	}

	@Override
	public void addLink(String from, String to) throws Exception {
		nonNullAdd(from, to, _froms);
		nonNullAdd(to, from, _tos);
	}

	@Override
	public Collection<String> linksFrom(String from) throws Exception {
		return nonNullGet(from,  _froms);
	}

	@Override
	public Collection<String> linksTo(String to) throws Exception {
		return nonNullGet(to, _tos);
	}
}
