package com.th3l4b.srm.codegen.template.execution;

public class ChangeCurrent extends ChangeParent {

	private Object _current;

	public ChangeCurrent(Object current, ITemplateExecutionContext delegate)
			throws Exception {
		super(delegate);
		setCurrent(current);
	}

	@Override
	public void setCurrent(Object current) throws Exception {
		_current = current;
	}

	@Override
	public Object getCurrent() throws Exception {
		return _current;
	}
}
