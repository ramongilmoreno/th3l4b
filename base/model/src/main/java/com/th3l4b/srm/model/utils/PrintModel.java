package com.th3l4b.srm.model.utils;

import java.io.PrintWriter;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.common.data.propertied.IPropertied;
import com.th3l4b.common.text.codegen.JavaEscape;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IField;
import com.th3l4b.srm.model.base.IModel;

public class PrintModel {

	private final static String INDENT = "    ";

	private static <N extends INamed> Iterable<N> sort(Iterable<N> input) {
		TreeSet<N> r = new TreeSet<N>(new Comparator<N>() {
			Collator _s = Collator.getInstance(Locale.ROOT);

			@Override
			public int compare(N o1, N o2) {
				try {
					return _s.compare(o1.getName(), o2.getName());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		for (N i : input) {
			r.add(i);
		}
		return r;
	}

	public static void print(IModel model, PrintWriter out) throws Exception {
		out.print("model " + model.getName());
		printProperties(model, out, "");
		out.println(";");
		out.println();
		for (IEntity e : sort(model)) {
			out.println("entity " + e.getName() + " {");
			for (IField f : sort(e)) {
				String n = f.getName();
				String t = f.getTarget();
				if (t == null) {
					out.print(INDENT + "field ");
					JavaEscape.javaTextQuoted(n, out);
				} else {
					out.print(INDENT + "reference ");
					JavaEscape.javaTextQuoted(t, out);
					if (!NullSafe.equals(n,  t)) {
						out.print(' ');
						JavaEscape.javaTextQuoted(n, out);
					}
				}
				printProperties(f, out, INDENT);
				out.println(";");
			}
			out.print('}');
			printProperties(e, out, "");
			out.println(";");
			out.println();
		}
	}

	private static void printProperties(IPropertied propertied,
			PrintWriter out, String prefix) throws Exception {
		Map<String, String> p = propertied.getProperties();
		if (p.isEmpty()) {
			return;
		}
		int size = p.size();
		if (size > 1) {
			out.println();
			prefix += INDENT;
			out.print(prefix);
		} else {
			out.print(' ');
		}
		out.print("properties {");
		TreeSet<String> keys = new TreeSet<String>(
				Collator.getInstance(Locale.ROOT));
		keys.addAll(p.keySet());
		for (String key : keys) {
			if (size > 1) {
				out.println();
				out.print(prefix + INDENT);
			} else {
				out.print(' ');
			}
			JavaEscape.javaTextQuoted(key, out);
			out.print(" = ");
			JavaEscape.javaTextQuoted(p.get(key), out);
			out.print(';');
		}
		if (size > 1) {
			out.println();
			out.print(prefix);
		} else {
			out.print(' ');
		}
		out.print("}");
	}
}
