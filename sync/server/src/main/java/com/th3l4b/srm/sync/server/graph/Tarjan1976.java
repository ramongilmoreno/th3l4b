package com.th3l4b.srm.sync.server.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * From Wikipedia:
 * https://en.wikipedia.org/w/index.php?title=Topological_sorting&oldid=642496240
 * 
 * L <- Empty list that will contain the sorted nodes
 * while there are unmarked nodes do
 *     select an unmarked node n
 *     visit(n)
 * function visit(node n)
 *     if n has a temporary mark then stop (not a DAG)
 *     if n is not marked (i.e. has not been visited yet) then
 *         mark n temporarily
 *         for each node m with an edge from n to m do
 *             visit(m)
 *         mark n permanently
 *         unmark n temporarily
 *         add n to head of L
 */
public class Tarjan1976 {

	/**
	 * Finds the appropriate sorting for the nodes of the dg graph which may
	 * be visited from the root. This allows finding all nodes in the graph,
	 * but to compute the order the reversed links graph is used.
	 */
	public List<String> sort (IDirectedGraph dg) throws Exception {
		ArrayList<String> r = new ArrayList<String>();
	    HashSet<String> unmarked = new HashSet<String>();	
	    bfs(dg, dg.getRoot(), unmarked);
	    HashSet<String> temporary = new HashSet<String>();
	    ReverseLinks reversed = new ReverseLinks(dg);
		while (!unmarked.isEmpty()) {
			visit(unmarked.iterator().next(), reversed, unmarked, temporary, r);
		}
		return r;
	}
	
	public void visit(String n, IDirectedGraph dg, HashSet<String> unmarked,
			HashSet<String> temporary, List<String> r) throws Exception {
		if (temporary.contains(n)) {
			throw new IllegalStateException("Not a DAG");
		}
		if (unmarked.contains(n)) {
			temporary.add(n);
			for (String m: dg.linksFrom(n)) {
				visit(m, dg, unmarked, temporary, r);
			}
			unmarked.remove(n);
			temporary.remove(n);
			int index = r.indexOf(n);
			if (index > 0) {
				r.remove(index);
			}
			r.add(0, n);
		}
	}

	public void bfs (IDirectedGraph dg, String start, Set<String> nodes) throws Exception {
		if (nodes.contains(start)) {
			return;
		}
	    nodes.add(start);
	    for (String to: dg.linksFrom(start)) {
	    	bfs(dg, to, nodes);
	    }
	}
}
