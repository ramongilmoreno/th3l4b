package com.th3l4b.srm.model.utils;

import java.io.PrintWriter;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.NullSafe;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.common.data.propertied.IPropertied;
import com.th3l4b.common.text.codegen.JavaEscape;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IField;
import com.th3l4b.srm.model.base.IModel;
import com.th3l4b.srm.model.base.IReference;

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
		out.print("model ");
		JavaEscape.javaTextQuoted(model.getName(), out);
		printProperties(model, out, "");
		out.println(";");
		out.println();
		for (IEntity e : sort(model)) {
			out.print("entity ");
			JavaEscape.javaTextQuoted(e.getName(), out);
			out.println(" {");
			for (IField f : sort(e)) {
				String n = f.getName();
				if (f instanceof IReference) {
					IReference ref = (IReference) f;
					out.print(INDENT + "reference ");
					String t = ref.getTarget();
					JavaEscape.javaTextQuoted(t, out);
					boolean newLineByProperties = printProperties(f, out,
							INDENT);
					INamedPropertied reverse = ref.getReverse();
					String rn = reverse.getName();
					boolean printed = false;
					String reverseString = "reverse";
					if (!NullSafe.equals(rn, e.getName())) {
						if (newLineByProperties) {
							out.println();
							out.print(INDENT);
							out.print(INDENT);
						} else {
							out.print(' ');
						}
						out.print(reverseString);
						out.print(' ');
						JavaEscape.javaTextQuoted(rn, out);
						printed = true;
					}

					if (!reverse.getProperties().isEmpty()) {
						if (!printed) {
							if (newLineByProperties) {
								out.println();
								out.print(INDENT);
								out.print(INDENT);
							} else {
								out.print(' ');
							}
							out.print(reverseString);
							printed = true;
						}
						printProperties(reverse, out, INDENT + INDENT);
					}
					out.println(";");

				} else {
					out.print(INDENT + "field ");
					JavaEscape.javaTextQuoted(n, out);
					printProperties(f, out, INDENT);
					out.println(";");
				}
			}
			out.print('}');
			printProperties(e, out, "");
			out.println(";");
			out.println();
		}
	}

	/**
	 * @return true if a new line was printed
	 */
	private static boolean printProperties(IPropertied propertied,
			PrintWriter out, String prefix) throws Exception {
		boolean r = false;
		Map<String, String> p = propertied.getProperties();
		if (p.isEmpty()) {
			return false;
		}
		int size = p.size();
		if (size > 1) {
			out.println();
			prefix += INDENT;
			out.print(prefix);
			r = true;
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
		return r;
	}
}
