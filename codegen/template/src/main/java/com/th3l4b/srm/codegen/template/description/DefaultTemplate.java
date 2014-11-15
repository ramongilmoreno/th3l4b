package com.th3l4b.srm.codegen.template.description;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import com.th3l4b.srm.codegen.template.description.tree.DefaultTemplateNode;
import com.th3l4b.srm.codegen.template.description.tree.ITemplateNode;

public class DefaultTemplate implements ITemplate {

	private TemplateUnit _templateUnit;
	private Collection<INamesEntry> _names = new ArrayList<INamesEntry>();
	private ITemplateNode _fileNameRoot = new DefaultTemplateNode();
	private ITemplateNode _contentRoot = new DefaultTemplateNode();

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
	public ITemplateNode getFileNameRoot() throws Exception {
		return _fileNameRoot;
	}

	@Override
	public ITemplateNode getContentRoot() throws Exception {
		return _contentRoot;
	}

	public static void print(ITemplate template, PrintWriter out)
			throws Exception {
		out.print("unit: ");
		out.println(template.getTemplateUnit().name());
		out.print("names: ");
		boolean first = true;
		for (INamesEntry name : template.getNames()) {
			if (first) {
				first = false;
			} else {
				out.print(",");
			}
			out.print("" + name.getClazz() + " " + name.getName());
		}
		out.println();

		out.print("file: ");
		DefaultTemplateNode.print(template.getFileNameRoot(), out);
		out.println();
		out.println("content:");
		DefaultTemplateNode.print(template.getContentRoot(), out);
	}

}
