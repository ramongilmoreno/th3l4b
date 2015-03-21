package com.th3l4b.srm.json;

import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public interface IJsonModelRuntime extends IContainer<IJsonEntityRuntime> {
	IModelRuntime runtime () throws Exception;
}
