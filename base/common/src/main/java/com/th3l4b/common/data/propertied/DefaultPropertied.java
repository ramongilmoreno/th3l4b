package com.th3l4b.common.data.propertied;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultPropertied implements IPropertied {

	Map<String, String> _properties = new LinkedHashMap<String, String>();
	Map<String, Object> _attributes = new LinkedHashMap<String, Object>();

	@Override
	public Map<String, String> getProperties() throws Exception {
		return _properties;
	}

	@Override
	public Map<String, Object> getAttributes() throws Exception {
		return _attributes;
	}

	public static void copy(IPropertied from, IPropertied to) throws Exception {
		to.getProperties().clear();
		to.getAttributes().clear();
		to.getProperties().putAll(from.getProperties());
		to.getAttributes().putAll(from.getAttributes());
	}

}