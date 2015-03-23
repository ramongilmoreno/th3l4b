package com.th3l4b.srm.rest;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.th3l4b.common.text.ITextConstants;
import com.th3l4b.srm.codegen.java.runtime.DefaultIdentifier;
import com.th3l4b.srm.json.Generator;
import com.th3l4b.srm.json.IJsonModelRuntime;
import com.th3l4b.srm.json.Parser;
import com.th3l4b.srm.json.Parser.Result;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IRuntime;

@SuppressWarnings("serial")
public abstract class AbstractRESTServlet extends HttpServlet {

	protected abstract IRuntime getRuntime(HttpServletRequest req,
			HttpServletResponse resp) throws Exception;

	protected abstract IJsonModelRuntime getJsonRuntime(HttpServletRequest req,
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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String[] coordinates = split(req);
			String defaultType = null;
			String defaultId = null;
			boolean isMany = false;
			switch (coordinates.length) {
			case 0:
				isMany = true;
				break;
			case 1:
				defaultType = coordinates[0];
				isMany = true;
				break;
			case 2:
				defaultType = coordinates[0];
				defaultId = coordinates[1];
				isMany = false;
				break;
			default:
				throw new IllegalArgumentException(
						"Unsupported number of coordinates in REST path: "
								+ coordinates.length);
			}

			IJsonModelRuntime jr = getJsonRuntime(req, resp);
			Result input = new Parser(jr, req.getReader()).parse(true, isMany,
					defaultType, defaultId);

			// Prepare updates
			ArrayList<IInstance> updates = new ArrayList<IInstance>();
			if (input._one != null) {
				updates.add(input._one);
			}
			updates.addAll(input._many);

			// Fix statuses
			for (IInstance i : updates) {
				ICoordinates c = i.coordinates();
				if (c.getStatus() == null) {
					c.setStatus(EntityStatus.Unknown);
				}
			}

			// Proceed with update
			Collection<IInstance> result = getRuntime(req, resp).updater()
					.update(updates);

			// Reply updates
			setupJsonResponse(resp);
			Generator generator = new Generator(jr, resp.getWriter());
			generator.write(result);
			generator.close();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			IRuntime runtime = getRuntime(req, resp);
			String[] split = split(req);
			Object r = null;
			if (split.length > 0) {
				String type = URLDecoder.decode(split[0], ITextConstants.UTF_8);
				IEntityRuntime er = runtime.model().get(type);
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
						r = runtime.finder()
								.references(id, reverseRelationship);
					}

				}
			}

			if (r == null) {
				throw new IllegalArgumentException("Not a valid REST request");
			}
			setupJsonResponse(resp);
			Generator g = new Generator(getJsonRuntime(req, resp),
					resp.getWriter());
			if (r instanceof Collection<?>) {
				@SuppressWarnings("unchecked")
				Collection<IInstance> col = (Collection<IInstance>) r;
				g.write(col);
			} else {
				IInstance instance = (IInstance) r;
				g.write(instance);
			}
			g.close();

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void setupJsonResponse(HttpServletResponse resp) {
		resp.setContentType("application/json");
		resp.setCharacterEncoding(ITextConstants.UTF_8);
	}
}
