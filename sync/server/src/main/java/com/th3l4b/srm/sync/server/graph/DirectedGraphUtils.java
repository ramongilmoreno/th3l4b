package com.th3l4b.srm.sync.server.graph;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.th3l4b.common.text.IndentedWriter;

public class DirectedGraphUtils {
	
	public static void print(IDirectedGraph dg, String start, PrintStream out)
			throws Exception {
		PrintWriter pw = new PrintWriter(out);
		print(dg, start, pw);
		pw.flush();
	}
	
	public static void print(IDirectedGraph dg, String start, PrintWriter out)
			throws Exception {
		out.println(start);
		PrintWriter iout = IndentedWriter.get(out);
		for (String n : dg.linksFrom(start)) {
			print(dg, n, iout);
		}
		iout.flush();
	}
}
