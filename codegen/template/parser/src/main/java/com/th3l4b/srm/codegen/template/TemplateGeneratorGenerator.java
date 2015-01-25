package com.th3l4b.srm.codegen.template;

import java.io.PrintWriter;
import java.util.Map;

import com.th3l4b.common.text.IndentedWriter;
import com.th3l4b.common.text.codegen.JavaEscape;
import com.th3l4b.srm.codegen.template.description.INamesEntry;
import com.th3l4b.srm.codegen.template.description.ITemplate;
import com.th3l4b.srm.codegen.template.description.TemplateUnit;
import com.th3l4b.srm.codegen.template.description.tree.IIterationNode;
import com.th3l4b.srm.codegen.template.description.tree.ILabelDefinitionNode;
import com.th3l4b.srm.codegen.template.description.tree.ILabelNode;
import com.th3l4b.srm.codegen.template.description.tree.ISubstitutionNode;
import com.th3l4b.srm.codegen.template.description.tree.ITemplateNode;
import com.th3l4b.srm.codegen.template.description.tree.ITextNode;
import com.th3l4b.srm.codegen.template.runtime.AbstractTemplateGenerator;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IField;
import com.th3l4b.srm.model.base.IModel;

public class TemplateGeneratorGenerator {

	protected void content(ITemplateNode node, PrintWriter out)
			throws Exception {
		boolean doChildren = true;
		if (node instanceof ITextNode) {
			ITextNode tn = (ITextNode) node;
			out.print("out.print(");
			JavaEscape.javaTextQuoted(tn.getText(), out);
			out.println(");");
		} else if (node instanceof ILabelDefinitionNode) {
			ILabelDefinitionNode ldn = (ILabelDefinitionNode) node;
			out.println("String label_" + ldn.getLabel() + " = "
					+ ldn.getContents() + ";");
		} else if (node instanceof ILabelNode) {
			ILabelNode ln = (ILabelNode) node;
			out.println("out.print(label_" + ln.getLabel() + ");");
		} else if (node instanceof ISubstitutionNode) {
			ISubstitutionNode sn = (ISubstitutionNode) node;
			out.print("out.print(");
			out.print(sn.getSubstitution());
			out.println(");");
		} else if (node instanceof IIterationNode) {
			IIterationNode in = (IIterationNode) node;
			String param = in.getParameterName();
			switch (in.getIteration()) {
			case entity:
				out.println("for (" + IEntity.class.getName() + " " + param
						+ ": model) {");
				break;
			case field:
				out.println("for (" + IField.class.getName() + " " + param
						+ ": entity) {");
				break;
			case fieldImmediate: {
				out.println("for (" + IField.class.getName() + " " + param
						+ ": entity) {");
				PrintWriter iout = IndentedWriter.get(out);
				iout.println("if (" + param + ".isReference()) { continue };");
				iout.flush();
				break;
			}
			case fieldReference: {
				out.println("for (" + IField.class.getName() + " " + param
						+ ": entity) {");
				PrintWriter iout = IndentedWriter.get(out);
				iout.println("if (!" + param + ".isReference()) { continue };");
				iout.flush();
				break;
			}
			default:
				throw new IllegalStateException();

			}

			// Iterate
			PrintWriter iout = IndentedWriter.get(out);
			for (ITemplateNode n : node.children()) {
				content(n, iout);
			}
			iout.flush();
			doChildren = false;
			out.println("}");
		}

		if (doChildren) {
			for (ITemplateNode n : node.children()) {
				content(n, out);
			}
		}
	}

	public void content(ITemplate template, String pkg, PrintWriter out)
			throws Exception {
		out.println("package " + pkg + ";");
		out.println();
		out.print("public class " + template.getTemplateName() + " extends "
				+ AbstractTemplateGenerator.class.getName() + ".");

		// Decide inheritance
		boolean isEntity = template.getTemplateUnit() == TemplateUnit.entity;
		if (isEntity) {
			out.print(AbstractTemplateGenerator.Entity.class.getSimpleName());
		} else {
			out.print(AbstractTemplateGenerator.Model.class.getSimpleName());
		}
		out.println(" {");

		PrintWriter iout = IndentedWriter.get(out);
		PrintWriter iiout = IndentedWriter.get(iout);

		out.println();
		iout.println("// Names go here");
		// Instantiate names as attributes.
		for (INamesEntry n : template.getNames()) {
			iout.println(n.getClazz() + " " + n.getName() + " = new "
					+ n.getClazz() + "();");
		}
		out.println();

		// Filename
		iout.print("protected void file (");
		if (isEntity) {
			iout.print(IEntity.class.getName() + " entity, ");
		}
		iout.println(IModel.class.getName() + " model, " + Map.class.getName()
				+ "<String, String> properties, " + PrintWriter.class.getName()
				+ " out) throws " + Exception.class.getName() + " {");
		content(template.getFileNameRoot(), iiout);
		iout.println("}");
		out.println();

		// Content
		iout.print("protected void content (");
		if (isEntity) {
			iout.print(IEntity.class.getName() + " entity, ");
		}
		iout.println(IModel.class.getName() + " model, " + Map.class.getName()
				+ "<String, String> properties, " + PrintWriter.class.getName()
				+ " out) throws " + Exception.class.getName() + " {");
		content(template.getContentRoot(), iiout);
		iout.println("}");
		iiout.flush();
		iout.flush();
		out.println("}");
	}
}
