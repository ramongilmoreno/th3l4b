package com.th3l4b.srm.json;

import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IEntityRuntime;

public class DefaultJsonEntityRuntime extends DefaultNamedContainer<IJsonFieldRuntime> implements IJsonEntityRuntime {
	
	private IEntityRuntime _runtime;

	public DefaultJsonEntityRuntime (IEntityRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
	}

	@Override
	public IEntityRuntime runtime() throws Exception {
		return _runtime;
	}
}
