package com.th3l4b.srm.android.sqlite;

import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class DefaultSQLiteModelRuntime extends
		DefaultNamedContainer<ISQLiteEntityRuntime> implements
		ISQLiteModelRuntime {

	private IModelRuntime _runtime;

	public DefaultSQLiteModelRuntime(IModelRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
	}

	@Override
	public IModelRuntime runtime() throws Exception {
		return _runtime;
	}

}
