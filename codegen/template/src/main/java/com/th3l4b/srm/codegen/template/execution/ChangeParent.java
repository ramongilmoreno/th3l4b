package com.th3l4b.srm.codegen.template.execution;

public class ChangeParent extends TemplateExecutionContextFilter {

	private ITemplateExecutionContext _parent;

	public ChangeParent(ITemplateExecutionContext parent) throws Exception {
		super(parent);
		setParent(_parent);
	}

	@Override
	public ITemplateExecutionContext getParent() throws Exception {
		return _parent;
	}

	@Override
	public void setParent(ITemplateExecutionContext parent) throws Exception {
		_parent = parent;
	}

}