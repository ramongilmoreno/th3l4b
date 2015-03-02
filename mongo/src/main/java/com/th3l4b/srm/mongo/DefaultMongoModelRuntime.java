package com.th3l4b.srm.mongo;

import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class DefaultMongoModelRuntime extends DefaultNamedContainer<IMongoEntityRuntime> implements IMongoModelRuntime {
	
	private IModelRuntime _runtime;

	public DefaultMongoModelRuntime(IModelRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
	}

	@Override
	public IModelRuntime runtime() throws Exception {
		return _runtime;
	}

}
