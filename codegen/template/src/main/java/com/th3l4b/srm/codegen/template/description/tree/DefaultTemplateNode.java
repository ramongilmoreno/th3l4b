package com.th3l4b.srm.codegen.template.description.tree;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

public class DefaultTemplateNode implements ITemplateNode {
	ArrayList<ITemplateNode> _children = new ArrayList<ITemplateNode>();

	@Override
	public Collection<ITemplateNode> children() throws Exception {
		return _children;
	}

	public static boolean innerPrint(ITemplateNode node, PrintWriter out)
			throws Exception {
		if (node instanceof IIterationNode) {
			IIterationNode it = (IIterationNode) node;
			out.print("<% iterate " + it.getIteration().name() + " "
					+ it.getParameterName() + " %>");
			for (ITemplateNode child : node.children()) {
				print(child, out);
			}
			out.print("<% end iterate %>");
			return true;
		} else if (node instanceof ITextNode) {
			ITextNode t = (ITextNode) node;
			out.print(t.getText());
		} else if (node instanceof ISubstitutionNode) {
			ISubstitutionNode s = (ISubstitutionNode) node;
			out.print("<%= ");
			out.print(s.getSubstitution());
			out.print(" %>");
		} else {
			// Do nothing
		}
		return false;
	}

	public static void print(ITemplateNode node, PrintWriter out)
			throws Exception {
		boolean doneWithChildren = innerPrint(node, out);
		if (!doneWithChildren) {
			for (ITemplateNode child : node.children()) {
				print(child, out);
			}
		}
	}

}
