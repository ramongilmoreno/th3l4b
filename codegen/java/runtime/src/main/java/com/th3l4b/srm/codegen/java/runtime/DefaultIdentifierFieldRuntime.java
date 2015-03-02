package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.srm.model.runtime.IInstance;

public class DefaultIdentifierFieldRuntime extends AbstractFieldRuntime {

	public DefaultIdentifierFieldRuntime() {
		super();
	}

	public DefaultIdentifierFieldRuntime(String name) throws Exception {
		super();
		setName(name);
	}

	@Override
	public String get(IInstance instance) throws Exception {
		return instance.coordinates().getIdentifier().getKey();
	}

	@Override
	public void set(String value, IInstance instance) throws Exception {
		instance.coordinates().getIdentifier().setKey(value);
	}

	@Override
	public boolean isSet(IInstance instance) throws Exception {
		return true;
	}

	@Override
	public void unSet(IInstance instance) throws Exception {
		// Does nothing
	}

}
