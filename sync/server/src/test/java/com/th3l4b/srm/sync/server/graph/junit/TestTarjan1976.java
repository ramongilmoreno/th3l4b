package com.th3l4b.srm.sync.server.graph.junit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.srm.sync.server.graph.IDirectedGraph;
import com.th3l4b.srm.sync.server.graph.InMemoryDirectedGraph;
import com.th3l4b.srm.sync.server.graph.Tarjan1976;

public class TestTarjan1976 {

	class TestIt {
		String _a, _b;

		public TestIt(String a, String b) {
			_a = a;
			_b = b;
		}

		public void testit(List<String> sorted) {
			Assert.assertTrue(sorted.indexOf(_a) < sorted.indexOf(_b));
		}
	}

	void addLink(String a, String b, IDirectedGraph dg, Set<String> nodes,
			Collection<TestIt> tests) throws Exception {
		dg.addLink(a, b);
		nodes.add(a);
		nodes.add(b);
		tests.add(new TestIt(a, b));
	}

	@Test
	public void test() throws Exception {
		// Example as in
		// https://en.wikipedia.org/w/index.php?title=Topological_sorting&oldid=642496240
		InMemoryDirectedGraph dg = new InMemoryDirectedGraph();
		HashSet<String> nodes = new HashSet<String>();
		ArrayList<TestIt> tests = new ArrayList<TestIt>();
		addLink("7", "8", dg, nodes, tests);
		addLink("5", "11", dg, nodes, tests);
		addLink("3", "8", dg, nodes, tests);
		addLink("3", "10", dg, nodes, tests);
		addLink("11", "2", dg, nodes, tests);
		addLink("11", "9", dg, nodes, tests);
		addLink("11", "10", dg, nodes, tests);
		addLink("8", "9", dg, nodes, tests);

		HashSet<String> temporary = new HashSet<String>();
		ArrayList<String> r = new ArrayList<String>();
		Tarjan1976 algo = new Tarjan1976();
		while (!nodes.isEmpty()) {
			algo.visit(nodes.iterator().next(), dg, nodes, temporary, r);
		}
		for (TestIt t : tests) {
			t.testit(r);
		}
	}
}
