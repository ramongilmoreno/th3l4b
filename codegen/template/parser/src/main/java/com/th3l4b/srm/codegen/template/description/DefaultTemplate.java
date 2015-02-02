package com.th3l4b.srm.codegen.template.description;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import com.th3l4b.srm.codegen.template.description.tree.TemplateNodeUtils;
import com.th3l4b.srm.codegen.template.description.tree.ITemplateNode;

public class DefaultTemplate implements ITemplate {

	private String _templateName;
	private TemplateUnit _templateUnit;
	private Collection<INamesEntry> _names = new ArrayList<INamesEntry>();
	private Collection<ITemplateNode> _fileName = new ArrayList<ITemplateNode>();
	private Collection<ITemplateNode> _content = new ArrayList<ITemplateNode>();

	@Override
	public String getTemplateName() throws Exception {
		return _templateName;
	}

	@Override
	public void setTemplateName(String name) throws Exception {
		_templateName = name;
	}

	@Override
	public TemplateUnit getTemplateUnit() throws Exception {
		return _templateUnit;
	}

	@Override
	public void setTemplateUnit(TemplateUnit unit) throws Exception {
		_templateUnit = unit;
	}

	@Override
	public Collection<INamesEntry> getNames() throws Exception {
		return _names;
	}

	@Override
	public Collection<ITemplateNode> getFileName () throws Exception {
		return _fileName;
	}
	
	@Override
	public Collection<ITemplateNode> getContent () throws Exception {
		return _content;
	}

	public static void print(ITemplate template, PrintWriter out)
			throws Exception {
		out.println("#");
		out.println("# " + template.getTemplateName());
		out.println("#");
		out.println();
		out.print("unit: ");
		out.println(template.getTemplateUnit().name());
		out.print("names: ");
		boolean first = true;
		for (INamesEntry name : template.getNames()) {
			if (first) {
				first = false;
			} else {
				out.print(", ");
			}
			out.print("" + name.getClazz() + " " + name.getName());
		}
		out.println();

		out.print("file: ");
		TemplateNodeUtils.print(template.getFileName(), out);
		out.println();
		out.println("content:");
		TemplateNodeUtils.print(template.getContent(), out);
	}

}
