package com.th3l4b.common.text.codegen;

import java.io.PrintWriter;

public class JavaEscape {

	public static void javaText(String input, PrintWriter out) {
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c < ' ' || c > 127 || c == '\\' || c == '\"') {
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

	public static void javaTextQuoted(String input, PrintWriter out) {
		out.print('"');
		javaText(input, out);
		out.print('"');
	}
}
