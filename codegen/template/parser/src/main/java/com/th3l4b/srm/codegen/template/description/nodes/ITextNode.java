package com.th3l4b.srm.codegen.template.description.nodes;

public interface ITextNode extends ITemplateNode {
	String getText () throws Exception;
	void setText (String text) throws Exception;
}
