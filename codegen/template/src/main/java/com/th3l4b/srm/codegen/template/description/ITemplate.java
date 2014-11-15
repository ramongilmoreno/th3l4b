package com.th3l4b.srm.codegen.template.description;

import java.util.Collection;

import com.th3l4b.srm.codegen.template.description.tree.ITemplateNode;

public interface ITemplate {
	TemplateUnit getTemplateUnit () throws Exception;
	void setTemplateUnit (TemplateUnit unit) throws Exception;
	Collection<INamesEntry> getNames () throws Exception;
	ITemplateNode getFileNameRoot () throws Exception;
	ITemplateNode getContentRoot () throws Exception;
}
