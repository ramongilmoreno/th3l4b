package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.common.lang.EnumUtils;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IInstance;

public class DefaultStatusFieldRuntime extends AbstractFieldRuntime {
	
	public DefaultStatusFieldRuntime () {
		super();
	}
	
	public DefaultStatusFieldRuntime(String name) throws Exception {
		super();
		setName(name);
	}

	@Override
	public String get(IInstance instance) throws Exception {
		return instance.coordinates().getStatus().name();
	}

	@Override
	public void set(String value, IInstance instance) throws Exception {
		EntityStatus s = EnumUtils.failSafeParse(value, EntityStatus.class,
				EntityStatus.Unknown);
		instance.coordinates().setStatus(s);
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
