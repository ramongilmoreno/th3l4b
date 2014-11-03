package com.th3l4b.srm.codegen.template.execution;

import java.io.PrintWriter;
import java.util.Map;

import com.th3l4b.srm.model.base.IModel;

public class TemplateExecutionContextFilter implements ITemplateExecutionContext {

	ITemplateExecutionContext _delegate;
	
	public TemplateExecutionContextFilter () {
	}
	
	public TemplateExecutionContextFilter (ITemplateExecutionContext delegate) {
		setDelegate(delegate);
	}
	
	public Map<String, Object> getObjects() throws Exception {
		return _delegate.getObjects();
	}

	public PrintWriter getOut() throws Exception {
		return _delegate.getOut();
	}

	public IModel getModel() throws Exception {
		return _delegate.getModel();
	}

	public void setModel(IModel model) throws Exception {
		_delegate.setModel(model);
	}

	public void setOut(PrintWriter out) throws Exception {
		_delegate.setOut(out);
	}

	public Object getCurrent() throws Exception {
		return _delegate.getCurrent();
	}

	public void setCurrent(Object current) throws Exception {
		_delegate.setCurrent(current);
	}

	public ITemplateExecutionContext getParent() throws Exception {
		return _delegate.getParent();
	}

	public void setParent(ITemplateExecutionContext parent) throws Exception {
		_delegate.setParent(parent);
	}

	public ITemplateExecutionContext getDelegate() {
		return _delegate;
	}
	
	public void setDelegate(ITemplateExecutionContext delegate) {
		_delegate = delegate;
	}

}
