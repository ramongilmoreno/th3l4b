package com.th3l4b.srm.model.runtime;

import com.th3l4b.common.data.INamedPropertied;

public interface IFieldRuntime extends INamedPropertied {
	String get (IInstance instance) throws Exception;
	void set (String value, IInstance instance) throws Exception;
	boolean isSet (IInstance instance) throws Exception;
	void unSet (IInstance instance) throws Exception;
}
