package com.th3l4b.srm.codegen.template.description.tree;

/**
 * @deprecated
 */
public interface IIterationNode extends ITemplateNode {
	IterationType getIteration() throws Exception;
	void setIteration(IterationType iteration) throws Exception;
	String getParameterName() throws Exception;
	void setParameterName(String parameter) throws Exception;
}
