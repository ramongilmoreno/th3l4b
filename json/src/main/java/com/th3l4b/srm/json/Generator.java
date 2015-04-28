package com.th3l4b.srm.json;

import java.io.Writer;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public class Generator implements IJsonConstants {

	private JsonGenerator _jackson;
	private IJsonModelRuntime _runtime;

	public Generator(IJsonModelRuntime runtime, Writer out) throws Exception {
		_jackson = new JsonFactory().createGenerator(out);
		_runtime = runtime;
	}

	public JsonGenerator getJackson() {
		return _jackson;
	}

	public IJsonModelRuntime getRuntime() {
		return _runtime;
	}

	public void write(Collection<IInstance> instances) throws Exception {
		// http://www.studytrails.com/java/json/java-jackson-json-streaming.jsp
		JsonGenerator jackson = getJackson();
		jackson.writeStartArray();
		for (IInstance i : instances) {
			write(i);
		}
		jackson.writeEndArray();
	}

	/**
	 * @param instance
	 *            Not null
	 */
	public void write(IInstance instance) throws Exception {
		// http://www.studytrails.com/java/json/java-jackson-json-streaming.jsp
		JsonGenerator generator = getJackson();
		generator.writeStartObject();
		generator.writeFieldName(FIELD_TYPE);
		generator.writeString(instance.coordinates().getIdentifier().getType());
		generator.writeFieldName(FIELD_ID);
		generator.writeString(instance.coordinates().getIdentifier().getKey());
		generator.writeFieldName(FIELD_STATUS);
		generator.writeString(instance.coordinates().getStatus().toString());

		if (!instance.empty()) {
			generator.writeFieldName(FIELD_FIELDS);
			generator.writeStartObject();
			IJsonEntityRuntime jer = getRuntime().get(
					instance.coordinates().getIdentifier().getType());
			for (IJsonFieldRuntime jfr : jer) {
				IFieldRuntime fr = jfr.runtime();
				if (fr.isSet(instance)) {
					String value = fr.get(instance);
					generator.writeFieldName(jfr.json());
					generator.writeString(value);
				}
			}
			generator.writeEndObject();
		}
		generator.writeEndObject();
	}

	public void close() throws Exception {
		_jackson.close();
		_jackson = null;
	}
}
