package com.th3l4b.srm.json;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IInstance;

public class Parser {

	private JsonParser _jackson;
	private IJsonModelRuntime _runtime;

	public Parser(IJsonModelRuntime runtime, Reader input) throws Exception {
		_jackson = new JsonFactory().createParser(input);
		_runtime = runtime;
	}

	public JsonParser getJackson() {
		return _jackson;
	}

	public IJsonModelRuntime getRuntime() {
		return _runtime;
	}

	public static class Result {
		public IInstance _one;
		public Collection<IInstance> _many;
	}

	public Result parse(boolean acceptOne, boolean acceptMany) throws Exception {
		return parse(acceptOne, acceptMany, null, null);
	}

	public Result parse(boolean acceptOne, boolean acceptMany,
			String defaultType, String defaultId) throws Exception {
		Result r = new Result();
		ArrayList<IInstance> many = new ArrayList<IInstance>();
		if (acceptMany) {
			r._many = many;
		}
		HashMap<String, String> map = new HashMap<String, String>();

		// http://www.studytrails.com/java/json/java-jackson-json-streaming.jsp
		JsonParser parser = getJackson();
		while (!parser.isClosed()) {
			JsonToken token = parser.nextToken();
			if (token == null) {
				// EOF
				break;
			} else if (JsonToken.START_ARRAY.equals(token)) {
				if (!acceptMany) {
					throw new IllegalArgumentException(
							"JSON Array input was not expected");
				}
				while ((token = parser.nextToken()) != null) {
					if (JsonToken.START_OBJECT.equals(token)) {
						many.add(parseObject(map, defaultType, defaultId));
					} else if (JsonToken.END_ARRAY.equals(token)) {
						parser.close();
						break;
					} else {
						throw new IllegalArgumentException(
								"JSON Array end was expected");
					}
				}
			} else if (JsonToken.START_OBJECT.equals(token)) {
				if (!acceptOne) {
					throw new IllegalArgumentException(
							"JSON Object input was not expected");
				}
				r._one = parseObject(map, defaultType, defaultId);
				parser.close();
			} else {
				throw new IllegalArgumentException("Unexpected JSON token: "
						+ token);
			}
		}
		return r;
	}

	private IInstance parseObject(Map<String, String> map, String defaultType,
			String defaultId) throws Exception {
		map.clear();
		JsonParser parser = getJackson();
		String id = defaultId;
		String type = defaultType;
		EntityStatus status = null;
		try {
			while (true) {
				JsonToken token = parser.nextToken();
				if ((token == null) || JsonToken.END_OBJECT.equals(token)) {
					break;
				} else if (JsonToken.FIELD_NAME.equals(token)) {
					String f = parser.getText();
					token = parser.nextToken();
					if (token == null) {
						break;
					} else if (JsonToken.START_OBJECT.equals(token)) {
						// Only valid if field is fields
						if (NullSafe.equals(f, IJsonConstants.FIELD_FIELDS)) {
							parseFields(map);
						} else {
							throw new IllegalStateException(
									"Inner object in field: " + f
											+ ", expecting "
											+ IJsonConstants.FIELD_FIELDS);
						}
					} else if (JsonToken.VALUE_STRING.equals(token)
							|| JsonToken.VALUE_NULL.equals(token)) {
						String v = parser.getText();
						if (NullSafe.equals(f, IJsonConstants.FIELD_ID)) {
							id = v;
						} else if (NullSafe
								.equals(f, IJsonConstants.FIELD_TYPE)) {
							type = v;
						} else if (NullSafe.equals(f,
								IJsonConstants.FIELD_STATUS)) {
							status = Enum.valueOf(EntityStatus.class, v);
						} else {
							throw new IllegalStateException("Unknown field: "
									+ f + " and value: " + v);
						}
					} else {
						throw new IllegalStateException(
								"Unexpected non string value: " + token
										+ " at field: " + f);
					}
				}

			}

			if (type != null) {
				IJsonEntityRuntime jer = getRuntime().get(type);
				if (jer == null) {
					throw new IllegalStateException("Unknown type: " + type);
				}
				IInstance r = jer.runtime().create();
				ICoordinates coordinates = r.coordinates();
				coordinates.getIdentifier().setKey(id);
				coordinates.setStatus(status);

				// Apply fields
				for (Map.Entry<String, String> e : map.entrySet()) {
					IJsonFieldRuntime jfr = jer.getByJsonName(e.getKey());
					if (jfr == null) {
						throw new IllegalStateException(
								"Could not find runtime for field: "
										+ e.getKey());
					}
					jfr.runtime().set(e.getValue(), r);
				}

				return r;
			} else {
				throw new IllegalStateException("Could not find type field");
			}
		} catch (IllegalStateException ise) {
			throw new IllegalStateException("Failed to parse type: " + type
					+ ", id: " + id + ", values: " + map, ise);
		}
	}

	private void parseFields(Map<String, String> map) throws Exception {
		JsonParser parser = getJackson();
		map.clear();
		while (true) {
			JsonToken token = parser.nextToken();
			if ((token == null) || JsonToken.END_OBJECT.equals(token)) {
				return;
			} else if (JsonToken.FIELD_NAME.equals(token)) {
				String f = parser.getText();
				token = parser.nextToken();

				if (JsonToken.VALUE_STRING.equals(token)
						|| JsonToken.VALUE_NULL.equals(token)) {
					String v = parser.getText();
					map.put(f, v);
				} else {
					throw new IllegalStateException(
							"Could not find value of field: " + f + ", was: "
									+ token);
				}
			}

		}
	}

	public void close() throws Exception {
		_jackson.close();
		_jackson = null;
	}
}
