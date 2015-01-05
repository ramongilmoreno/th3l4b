package com.th3l4b.srm.model.runtime;

import java.util.Collection;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.IContainer;

public interface IModelRuntime extends INamedPropertied,
		IContainer<IEntityRuntime> {
	IFinder finder () throws Exception;
	void update(Collection<IInstance> entities) throws Exception;
}
