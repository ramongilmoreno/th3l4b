package com.th3l4b.srm.json;

import java.io.Writer;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IRuntime;

public class Generator {

	private static final String FIELD_FIELDS = "fields";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_ID = "id";
	private static final String FIELD_TYPE = "type";
	private JsonGenerator _jackson;
	private IRuntime _runtime;

	public Generator(IRuntime runtime, Writer out) throws Exception {
		_jackson = new JsonFactory().createGenerator(out);
		_runtime = runtime;
	}
	
	public JsonGenerator getJackson() {
		return _jackson;
	}
	
	public IRuntime getRuntime() {
		return _runtime;
	}
	
	public void write (Collection<IInstance> instances) throws Exception {
		// http://www.studytrails.com/java/json/java-jackson-json-streaming.jsp
	    JsonGenerator jackson = getJackson();
		jackson.writeStartArray();
		for (IInstance i: instances) {
			write(i);
		}
		jackson.writeEndArray();
	}
	
	/**
	 * @param instance Not null
	 */
	public void write (IInstance instance) throws Exception {
		// http://www.studytrails.com/java/json/java-jackson-json-streaming.jsp
	    JsonGenerator generator = getJackson();
		generator.writeStartObject();
		generator.writeFieldName(FIELD_TYPE);
		generator.writeString(instance.type());
		generator.writeFieldName(FIELD_ID);
		generator.writeString(instance.coordinates().getIdentifier().getKey());
		generator.writeFieldName(FIELD_STATUS);
		generator.writeString(instance.coordinates().getStatus().toString());
		generator.writeFieldName(FIELD_FIELDS);
		generator.writeStartObject();
		IEntityRuntime er = getRuntime().entities().get(instance.type());
		for (IFieldRuntime fr : er) {
			if (fr.isSet(instance)) {
				String value = fr.get(instance);
				generator.writeFieldName(fr.getName());
				generator.writeString(value);
			}
		}
		generator.writeEndObject();
		generator.writeEndObject();
	}
	
	public void close() throws Exception {
		_jackson.close();
		_jackson = null;
	}
}
