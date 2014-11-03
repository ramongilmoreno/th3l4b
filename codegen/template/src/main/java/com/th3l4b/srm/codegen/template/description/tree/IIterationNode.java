package com.th3l4b.srm.codegen.template.description.tree;

public interface IIterationNode {
	IterationType getIteration () throws Exception;
	void setIteration (IterationType iteration) throws Exception;
	String getParameterName () throws Exception;
	void setParameterName (String parameter) throws Exception;
}
