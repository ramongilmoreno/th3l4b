package com.th3l4b.srm.codegen.template.execution;

import java.io.PrintWriter;

import com.th3l4b.srm.model.base.IModel;

public interface ITemplateExecutionContext {
	Object getObject (String name) throws Exception;
	void setObject (String name, Object object) throws Exception;
	PrintWriter getOut () throws Exception;
	IModel getModel () throws Exception;
	void setModel (IModel model) throws Exception;
	void setOut (PrintWriter out) throws Exception;
	Object getCurrent () throws Exception;
	void setCurrent (Object current) throws Exception;
	ITemplateExecutionContext getParent () throws Exception;
	void setParent (ITemplateExecutionContext parent) throws Exception;
}
