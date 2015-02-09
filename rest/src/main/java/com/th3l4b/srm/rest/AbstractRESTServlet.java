package com.th3l4b.srm.rest;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.th3l4b.common.text.ITextConstants;
import com.th3l4b.srm.codegen.java.runtime.DefaultIdentifier;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;

@SuppressWarnings("serial")
public abstract class AbstractRESTServlet extends HttpServlet {

	protected abstract IModelRuntime getRuntimeModel(HttpServletRequest req,
			HttpServletResponse resp) throws Exception;

	private String[] split(HttpServletRequest request) throws Exception {
		// Returns up to 3 strings with this format:
		// http://stackoverflow.com/questions/4278083/how-to-get-request-uri-without-context-path
		String pathInfo = request.getPathInfo();
		if (pathInfo == null) {
			return new String[0];
		} else {
			String[] split = pathInfo.split("/");
			String[] r = new String[split.length - 1];
			System.arraycopy(split, 1, r, 0, split.length - 1);
			return r;
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			IModelRuntime runtime = getRuntimeModel(req, resp);
			String[] split = split(req);
			Object r = null;
			if (split.length > 0) {
				String type = URLDecoder.decode(split[0], ITextConstants.UTF_8);
				IEntityRuntime er = runtime.entities().get(type);
				if (er == null) {
					throw new IllegalArgumentException("Unknown entity: "
							+ type);
				}

				if (split.length == 1) {
					r = runtime.finder().all(type);
				} else {
					String key = URLDecoder.decode(split[1],
							ITextConstants.UTF_8);
					DefaultIdentifier id = new DefaultIdentifier(type, key);
					if (split.length == 2) {
						r = runtime.finder().find(id);
					} else {
						// Find a reverse relationship
						String reverseRelationship = split[2];
						r = runtime.finder().find(id, reverseRelationship);
					}

				}
			}

			if (r == null) {
				throw new IllegalArgumentException("Not a valid REST request");
			}

			// http://www.studytrails.com/java/json/java-jackson-json-streaming.jsp
			JsonFactory factory = new JsonFactory();
			resp.setContentType("application/json");
			resp.setCharacterEncoding(ITextConstants.UTF_8);
			JsonGenerator generator = factory.createGenerator(resp.getWriter());
			if (r instanceof Collection<?>) {
				@SuppressWarnings("unchecked")
				Collection<IInstance> col = (Collection<IInstance>) r;
				generator.writeStartArray();
				for (IInstance instance : col) {
					serialize(instance, runtime, generator);
				}
				generator.writeEndArray();

			} else {
				IInstance instance = (IInstance) r;
				serialize(instance, runtime, generator);
			}
			generator.close();

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void serialize(IInstance instance, IModelRuntime runtime,
			JsonGenerator generator) throws Exception {
		generator.writeStartObject();
		generator.writeFieldName("_Type");
		generator.writeString(instance.type());
		generator.writeFieldName("_Id");
		generator.writeString(instance.coordinates().getIdentifier().getKey());
		generator.writeFieldName("_Status");
		generator.writeString(instance.coordinates().getStatus().toString());
		IEntityRuntime er = runtime.entities().get(instance.type());
		for (IFieldRuntime fr : er) {
			if (fr.isSet(instance)) {
				String value = fr.get(instance);
				generator.writeFieldName(fr.getName());
				generator.writeString(value);
			}
		}

		generator.writeEndObject();
	}

}
