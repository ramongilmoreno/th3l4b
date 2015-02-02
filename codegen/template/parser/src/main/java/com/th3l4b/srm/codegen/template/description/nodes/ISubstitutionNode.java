package com.th3l4b.srm.codegen.template.description.nodes;

public interface ISubstitutionNode extends ITemplateNode {
	String getSubstitution () throws Exception;
	void setSubstitution (String substitution) throws Exception;
}
