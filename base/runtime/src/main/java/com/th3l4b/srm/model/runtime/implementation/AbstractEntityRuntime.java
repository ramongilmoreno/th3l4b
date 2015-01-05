package com.th3l4b.srm.model.runtime.implementation;

import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;

public abstract class AbstractEntityRuntime extends
		DefaultNamedContainer<IFieldRuntime> implements IEntityRuntime {
}
