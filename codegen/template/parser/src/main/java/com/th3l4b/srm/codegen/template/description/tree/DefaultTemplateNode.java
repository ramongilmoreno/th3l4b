package com.th3l4b.srm.codegen.template.description.tree;

import java.io.PrintWriter;
import java.util.Collection;

public class DefaultTemplateNode implements ITemplateNode {

	public static void print(Collection<ITemplateNode> nodes, PrintWriter out)
			throws Exception {
		for (ITemplateNode n: nodes) {
			print(n, out);
		}
	}

	public static void print(ITemplateNode node, PrintWriter out)
			throws Exception {
		if (node instanceof ITextNode) {
			ITextNode t = (ITextNode) node;
			out.print(t.getText());
		} else if (node instanceof ISubstitutionNode) {
			ISubstitutionNode s = (ISubstitutionNode) node;
			out.print("<%= ");
			out.print(s.getSubstitution());
			out.print(" %>");
		} else if (node instanceof ICodeNode) {
			ICodeNode c = (ICodeNode) node;
			out.print("<% ");
			out.print(c.getCode());
			out.print(" %>");
		} else {
			throw new IllegalArgumentException(
					"Don't now what to do with node of class: "
							+ node.getClass());
		}
	}

}
