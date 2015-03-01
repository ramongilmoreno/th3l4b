package com.th3l4b.srm.mongo;

import com.th3l4b.common.data.named.DefaultContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class DefaultMongoModelRuntime extends DefaultContainer<IMongoEntityRuntime> implements IMongoModelRuntime {
	
	private IModelRuntime _runtime;

	public DefaultMongoModelRuntime (IModelRuntime runtime) {
		_runtime = runtime;
	}

	@Override
	public IModelRuntime runtime() throws Exception {
		return _runtime;
	}

}
