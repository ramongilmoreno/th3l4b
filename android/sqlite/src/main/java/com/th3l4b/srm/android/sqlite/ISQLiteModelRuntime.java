package com.th3l4b.srm.android.sqlite;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public interface ISQLiteModelRuntime extends INamedPropertied,
		IContainer<ISQLiteEntityRuntime> {
	IModelRuntime runtime() throws Exception;
}
