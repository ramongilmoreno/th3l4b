package com.th3l4b.srm.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.th3l4b.srm.model.runtime.IModelRuntime;

@SuppressWarnings("serial")
public abstract class AbstractRESTServlet extends HttpServlet {

	protected abstract IModelRuntime getRuntime(HttpServletRequest req,
			HttpServletResponse resp) throws Exception;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.service(req, resp);
	}

}
