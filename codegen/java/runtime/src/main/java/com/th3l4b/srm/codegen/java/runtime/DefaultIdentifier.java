package com.th3l4b.srm.codegen.java.runtime;

import java.util.UUID;

import com.th3l4b.srm.model.runtime.IIdentifier;

public class DefaultIdentifier implements IIdentifier {

	private String _key;
	private String _type;

	public DefaultIdentifier(String type, String key) throws Exception {
		setType(type);
		setKey(key);
	}
	
	public DefaultIdentifier(String type) throws Exception {
		setType(type);
		setKey(UUID.randomUUID().toString());
	}

	@Override
	public String getKey() throws Exception {
		return _key;
	}

	@Override
	public void setKey(String key) throws Exception {
		_key = key;
	}

	@Override
	public String getType() throws Exception {
		return _type;
	}

	@Override
	public void setType(String type) throws Exception {
		_type = type;
	}

}
