package com.th3l4b.common.text.codegen;

import java.io.PrintWriter;

public class JavaEscape {
	
	public static String javaText (String input) {
		StringPrintWriter spw = new StringPrintWriter();
		javaText(input, spw);
		return spw.toString();
	}

	public static void javaText(String input, PrintWriter out) {
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
                        // http://stackoverflow.com/questions/3866187/why-cant-i-use-u000d-and-u000a-as-cr-and-lf-in-java/3866219#3866219
			if (c == '\r') {
				out.write("\\r");
			} else if (c == '\n') {
				out.write("\\n");
			} else if (c == '\\') {
				out.write("\\\\");
			} else if (c == '\"') {
				out.write("\\\"");
			} else if (c < ' ' || c > 127) {
				out.write("\\u");
				String s = Integer.toHexString((int) c);
				for (int j = s.length(); j < 4; j++) {
					out.write('0');
				}
				out.write(s);
			} else {
				out.write(c);
			}
		}
	}

	public static String javaTextQuoted(String input) {
		StringPrintWriter spw = new StringPrintWriter();
		javaTextQuoted(input, spw);
		return spw.toString();
	}
	
	public static void javaTextQuoted(String input, PrintWriter out) {
		out.print('"');
		javaText(input, out);
		out.print('"');
	}
}
