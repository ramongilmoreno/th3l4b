package com.th3l4b.srm.codegen.template.execution;

import com.th3l4b.common.data.NullSafe;

public class ChangeCurrent extends ChangeParent {

	private String _name;
	private Object _current;

	public ChangeCurrent(String name, Object current, ITemplateExecutionContext delegate)
			throws Exception {
		super(delegate);
		_name = name;
		_current = current;
	}
	
	@Override
	public Object getObject(String name) throws Exception {
		if (NullSafe.equals(_name, name)) {
			return _current;
		} else {
			return super.getObject(name);
		}
	}
	
	@Override
	public void setObject(String name, Object object) throws Exception {
		_name = name;
		_current = object;
	}

}
