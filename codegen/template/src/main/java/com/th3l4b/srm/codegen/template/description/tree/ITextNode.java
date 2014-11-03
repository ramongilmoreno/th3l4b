package com.th3l4b.srm.codegen.template.description.tree;

public interface ITextNode extends ITemplateNode {
	String getSubstitution () throws Exception;
	void setSubstitution (String substitution) throws Exception;
}
