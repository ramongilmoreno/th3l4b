package com.th3l4b.srm.mongo;

import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.srm.model.runtime.IRuntime;

public interface IMongoModelRuntime extends IContainer<IMongoEntityRuntime> {
	IRuntime runtime () throws Exception;
}
