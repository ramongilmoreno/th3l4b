package com.th3l4b.srm.codegen.template.description;

import java.util.Collection;

import com.th3l4b.srm.codegen.template.description.nodes.ITemplateNode;

public interface ITemplate {
	String getTemplateName () throws Exception;
	void setTemplateName (String name) throws Exception;
	TemplateUnit getTemplateUnit () throws Exception;
	void setTemplateUnit (TemplateUnit unit) throws Exception;
	Collection<INamesEntry> getNames () throws Exception;
	Collection<ITemplateNode> getFileName () throws Exception;
	Collection<ITemplateNode> getContent () throws Exception;
}
