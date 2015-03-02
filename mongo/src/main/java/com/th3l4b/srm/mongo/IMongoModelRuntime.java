package com.th3l4b.srm.mongo;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public interface IMongoModelRuntime extends INamedPropertied,
		IContainer<IMongoEntityRuntime> {
	IModelRuntime runtime() throws Exception;
}
