package com.th3l4b.srm.codegen.template.description.tree;

public interface ISubstitutionNode extends ITemplateNode {
	String getText () throws Exception;
	void setText (String text) throws Exception;
}
