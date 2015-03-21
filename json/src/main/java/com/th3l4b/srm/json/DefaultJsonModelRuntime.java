package com.th3l4b.srm.json;

import com.th3l4b.common.data.named.DefaultContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class DefaultJsonModelRuntime extends
		DefaultContainer<IJsonEntityRuntime> implements IJsonModelRuntime {

	private IModelRuntime _runtime;

	public DefaultJsonModelRuntime(IModelRuntime runtime) throws Exception {
		_runtime = runtime;
	}

	@Override
	public IModelRuntime runtime() throws Exception {
		return _runtime;
	}
}
