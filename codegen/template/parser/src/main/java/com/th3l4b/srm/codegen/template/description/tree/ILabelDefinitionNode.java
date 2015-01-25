package com.th3l4b.srm.codegen.template.description.tree;

public interface ILabelDefinitionNode extends ITemplateNode {
	void setLabel (String label) throws Exception;
	String getLabel () throws Exception;
	void setContents (String contents) throws Exception;
	String getContents () throws Exception;
}
