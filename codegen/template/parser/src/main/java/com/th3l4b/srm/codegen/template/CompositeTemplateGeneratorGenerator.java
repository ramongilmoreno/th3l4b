package com.th3l4b.srm.codegen.template;

import java.io.PrintWriter;

import com.th3l4b.common.text.IndentedWriter;
import com.th3l4b.srm.codegen.template.description.ITemplate;
import com.th3l4b.srm.codegen.template.runtime.CompositeTemplateGenerator;
import com.th3l4b.srm.codegen.template.runtime.ITemplateGenerator;

public class CompositeTemplateGeneratorGenerator {

	public static final String CLASS_NAME = "All";

	public void content(Iterable<ITemplate> templates, String pkg,
			PrintWriter out) throws Exception {
		out.println("package " + pkg + ";");
		out.println();
		out.println("public class " + CLASS_NAME + " extends "
				+ CompositeTemplateGenerator.class.getName() + " implements "
				+ ITemplateGenerator.class.getName() + " {");

		out.println();

		PrintWriter iout = IndentedWriter.get(out);
		PrintWriter iiout = IndentedWriter.get(iout);

		iout.println("public " + CLASS_NAME + " () throws "
				+ Exception.class.getName() + " {");
		iiout.println("super();");
		for (ITemplate t : templates) {
			iiout.println("add( new " + pkg + "." + t.getTemplateName()
					+ "());");
		}

		iout.println("}");
		out.println("}");

		iiout.flush();
		iout.flush();
		iout.flush();
	}
}
