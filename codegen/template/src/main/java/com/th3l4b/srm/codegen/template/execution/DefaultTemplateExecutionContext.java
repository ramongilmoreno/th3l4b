package com.th3l4b.srm.codegen.template.execution;

import java.io.PrintWriter;

import com.th3l4b.srm.model.base.IModel;

public class DefaultTemplateExecutionContext implements
		ITemplateExecutionContext {

	private String _name;
	private Object _object;

	private PrintWriter _out;
	private IModel _model;
	private Object _current;
	private ITemplateExecutionContext _parent;

	@Override
	public Object getObject(String name) throws Exception {
		return _name.equals(name) ? _object : null;
	}

	@Override
	public void setObject(String name, Object object) throws Exception {
		_name = name;
		_object = object;
	}

	@Override
	public PrintWriter getOut() throws Exception {
		return _out;
	}

	@Override
	public IModel getModel() throws Exception {
		return _model;
	}

	@Override
	public void setModel(IModel model) throws Exception {
		_model = model;
	}

	@Override
	public void setOut(PrintWriter out) throws Exception {
		_out = out;
	}

	@Override
	public Object getCurrent() throws Exception {
		return _current;
	}

	@Override
	public void setCurrent(Object current) throws Exception {
		_current = current;
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
