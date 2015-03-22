package com.th3l4b.srm.json;

import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.runtime.IEntityRuntime;

public interface IJsonEntityRuntime extends INamed,
		IContainer<IJsonFieldRuntime> {
	IEntityRuntime runtime() throws Exception;
	IJsonFieldRuntime getByJsonName (String name) throws Exception;
}
