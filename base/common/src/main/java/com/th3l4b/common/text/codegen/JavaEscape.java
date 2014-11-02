package com.th3l4b.common.text.codegen;

import java.io.PrintWriter;

public class JavaEscape {
	public static void javaText (String input, PrintWriter out) {
		String fixme;
		out.print(input);
	}
	
	public static void javaTextQuoted (String input, PrintWriter out) {
		out.print('"');
		javaText(input, out);
		out.print('"');
	}
}
