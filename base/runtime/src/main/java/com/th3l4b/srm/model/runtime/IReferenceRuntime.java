package com.th3l4b.srm.model.runtime;

import com.th3l4b.common.data.INamedPropertied;

public interface IReferenceRuntime extends IFieldRuntime {
	String targetType() throws Exception;
	INamedPropertied reverse () throws Exception;
}
