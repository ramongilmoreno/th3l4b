package com.th3l4b.srm.mongo;

import com.th3l4b.common.data.named.DefaultContainer;
import com.th3l4b.srm.model.runtime.IRuntime;

public class DefaultMongoModelRuntime extends DefaultContainer<IMongoEntityRuntime> implements IMongoModelRuntime {
	
	private IRuntime _runtime;

	public DefaultMongoModelRuntime (IRuntime runtime) {
		_runtime = runtime;
	}

	@Override
	public IRuntime runtime() throws Exception {
		return _runtime;
	}

}
