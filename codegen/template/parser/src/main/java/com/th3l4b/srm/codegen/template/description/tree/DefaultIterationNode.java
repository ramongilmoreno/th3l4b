package com.th3l4b.srm.codegen.template.description.tree;

public class DefaultIterationNode extends DefaultTemplateNode implements
		IIterationNode {

	private IterationType _it;
	private String _parameter;

	public DefaultIterationNode(IterationType it, String parameter)
			throws Exception {
		setIteration(it);
		setParameterName(parameter);
	}

	@Override
	public IterationType getIteration() throws Exception {
		return _it;
	}

	@Override
	public void setIteration(IterationType it) throws Exception {
		_it = it;
	}

	@Override
	public String getParameterName() throws Exception {
		return _parameter;
	}

	@Override
	public void setParameterName(String parameter) throws Exception {
		_parameter = parameter;
	}

}
