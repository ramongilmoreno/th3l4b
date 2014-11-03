package com.th3l4b.srm.codegen.template.execution;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.th3l4b.srm.model.base.IModel;

public class DefaultTemplateExecutionContext implements
		ITemplateExecutionContext {

	private Map<String, Object> _objects = new LinkedHashMap<String, Object>();
	private PrintWriter _out;
	private IModel _model;
	private Object _current;
	private ITemplateExecutionContext _parent;

	@Override
	public Map<String, Object> getObjects() throws Exception {
		return _objects;
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
