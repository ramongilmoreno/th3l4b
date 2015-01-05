package com.th3l4b.srm.model.runtime;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.IContainer;

public interface IEntityRuntime extends INamedPropertied, IContainer<IFieldRuntime> {
	IInstance create () throws Exception;
}
