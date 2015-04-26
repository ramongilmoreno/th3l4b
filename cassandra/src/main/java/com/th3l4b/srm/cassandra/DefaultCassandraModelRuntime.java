package com.th3l4b.srm.cassandra;

import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class DefaultCassandraModelRuntime extends
		DefaultNamedContainer<ICassandraEntityRuntime> implements
		ICassandraModelRuntime {

	private IModelRuntime _runtime;

	public DefaultCassandraModelRuntime(IModelRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
	}

	@Override
	public IModelRuntime runtime() throws Exception {
		return _runtime;
	}

}
