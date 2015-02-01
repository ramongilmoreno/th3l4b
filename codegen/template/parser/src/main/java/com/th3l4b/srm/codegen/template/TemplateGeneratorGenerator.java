package com.th3l4b.srm.codegen.template;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

import com.th3l4b.common.text.IndentedWriter;
import com.th3l4b.common.text.codegen.JavaEscape;
import com.th3l4b.srm.codegen.template.description.INamesEntry;
import com.th3l4b.srm.codegen.template.description.ITemplate;
import com.th3l4b.srm.codegen.template.description.TemplateUnit;
import com.th3l4b.srm.codegen.template.description.tree.ICodeNode;
import com.th3l4b.srm.codegen.template.description.tree.ISubstitutionNode;
import com.th3l4b.srm.codegen.template.description.tree.ITemplateNode;
import com.th3l4b.srm.codegen.template.description.tree.ITextNode;
import com.th3l4b.srm.codegen.template.runtime.AbstractTemplateGenerator;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IModel;

public class TemplateGeneratorGenerator {

	protected void content(Collection<ITemplateNode> nodes, PrintWriter out)
			throws Exception {
		for (ITemplateNode node : nodes) {
			content(node, out);
		}
	};

	protected void content(ITemplateNode node, PrintWriter out)
			throws Exception {
		if (node instanceof ITextNode) {
			ITextNode tn = (ITextNode) node;
			out.print("out.print(");
			JavaEscape.javaTextQuoted(tn.getText(), out);
			out.println(");");
		} else if (node instanceof ISubstitutionNode) {
			ISubstitutionNode sn = (ISubstitutionNode) node;
			out.print("out.print(");
			out.print(sn.getSubstitution());
			out.println(");");
		} else if (node instanceof ICodeNode) {
			ICodeNode cn = (ICodeNode) node;
			out.println(cn.getCode());
		} else {
			throw new IllegalArgumentException(
					"Don't know what to do with node of class: "
							+ node.getClass());
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
		content(template.getFileName(), iiout);
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
		content(template.getContent(), iiout);
		iout.println("}");
		iiout.flush();
		iout.flush();
		out.println("}");
	}
}
