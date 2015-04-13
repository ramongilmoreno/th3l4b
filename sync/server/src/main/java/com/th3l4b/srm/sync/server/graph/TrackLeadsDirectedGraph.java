package com.th3l4b.srm.sync.server.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class TrackLeadsDirectedGraph extends DirectedGraphFilter {

	protected HashSet<String> _visited = new HashSet<String>();
	protected HashSet<String> _ignored = new HashSet<String>();

	// Keep leads sorted
	protected LinkedHashSet<String> _leads = new LinkedHashSet<String>();

	public TrackLeadsDirectedGraph() {
		super();
	}

	public TrackLeadsDirectedGraph(IDirectedGraph delegated) {
		super(delegated);
	}

	public Collection<String> visited() {
		return _visited;
	}
	
	public Collection<String> leads() {
		return _leads;
	}

	public void ignore(String node) throws Exception {
		ignore(node, new HashSet<String>());
	}

	public void ignore(String node, Set<String> loopControl) throws Exception {
		// Prevent infinite loops
		if (loopControl.contains(node)) {
			return;
		} else {
			loopControl.add(node);
		}
		if (!_ignored.contains(node)) {
			_ignored.add(node);
		}
		if (_leads.contains(node)) {
			_leads.remove(node);
		}
		if (_visited.contains(node)) {
			// Add to ignored all pointed nodes.
			for (String c : new HashSet<String>(linksFrom(node))) {
				ignore(c, loopControl);
			}
		}
	}

	@Override
	public Collection<String> linksFrom(String from) throws Exception {
		if (_ignored.contains(from)) {
			return Collections.<String> emptyList();
		} else {
			ArrayList<String> r = new ArrayList<String>();
			for (String f : super.linksFrom(from)) {
				if (!_ignored.contains(f)) {
					r.add(f);
				}
			}
			return r;
		}
	}

	@Override
	public Collection<String> linksTo(String to) throws Exception {
		if (_ignored.contains(to)) {
			return Collections.<String> emptyList();
		} else {
			ArrayList<String> r = new ArrayList<String>();
			for (String f : super.linksTo(to)) {
				if (!_ignored.contains(f)) {
					r.add(f);
				}
			}
			return r;
		}
	}

	public boolean addLead(String node) throws Exception {
		if (_ignored.contains(node) || _visited.contains(node)) {
			// Skip
			return false;
		} else {
			_leads.add(node);
			return true;
		}
	}

	public void setVisited(String node) throws Exception {
		_visited.add(node);
		_leads.remove(node);
	}

	public boolean isVisited(String node) throws Exception {
		return _visited.contains(node);
	}
}