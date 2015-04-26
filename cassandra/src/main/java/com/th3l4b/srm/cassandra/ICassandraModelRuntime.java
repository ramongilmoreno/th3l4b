package com.th3l4b.srm.cassandra;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public interface ICassandraModelRuntime extends INamedPropertied,
		IContainer<ICassandraEntityRuntime> {
	IModelRuntime runtime() throws Exception;
}
