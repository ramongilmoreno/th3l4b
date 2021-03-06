package com.th3l4b.srm.sample.war;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.th3l4b.srm.json.IJsonModelRuntime;
import com.th3l4b.srm.json.JsonUtils;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.rest.AbstractRESTServlet;
import com.th3l4b.srm.sample.base.SampleData;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.inmemory.AbstractSampleInMemoryRuntime;

@SuppressWarnings("serial")
public class SampleRESTServlet extends AbstractRESTServlet {
	
    private static IJsonModelRuntime JSON_RUNTIME;

	@Override
	protected IJsonModelRuntime getJsonRuntime(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		if (JSON_RUNTIME == null) {
			JSON_RUNTIME = JsonUtils.runtime(SampleModelUtils.RUNTIME);
		}
		return JSON_RUNTIME;
	}
	
	@Override
	protected IRuntime getRuntime (HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		HttpSession session = req.getSession(true);
		String attributeName = getClass().getName();
		@SuppressWarnings("unchecked")
		Map<IIdentifier, IInstance> data = (Map<IIdentifier, IInstance>) session
				.getAttribute(attributeName);
		if (data == null) {
			// Create data and populate of sample data
			data = new LinkedHashMap<IIdentifier, IInstance>();
			final Map<IIdentifier, IInstance> fdata = data;
			AbstractSampleInMemoryRuntime tofill = new AbstractSampleInMemoryRuntime() {
				@Override
				protected Map<IIdentifier, IInstance> getMap() throws Exception {
					return fdata;
				}
			};
			new SampleData().fill(tofill);
			session.setAttribute(attributeName, data);
		}
		
		// Return in memory runitme
		final Map<IIdentifier, IInstance> fdata = data;
		return new AbstractSampleInMemoryRuntime() {
			@Override
			protected Map<IIdentifier, IInstance> getMap() throws Exception {
				return fdata;
			}
		};
	}
}
