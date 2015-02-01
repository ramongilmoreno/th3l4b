package com.th3l4b.srm.codegen.template.description.tree;

public interface ICodeNode extends ITemplateNode {
	String getCode () throws Exception;
	void setCode (String code) throws Exception;
}
