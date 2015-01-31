package com.th3l4b.srm.model.runtime;

import com.th3l4b.common.data.named.INamed;
import com.th3l4b.common.data.propertied.IPropertied;

/**
 * @deprecated Util some types support is built. Meanwhile only string fields.
 */
public interface IType<T> extends INamed, IPropertied {
	T parse(String text) throws Exception;
	String toString(T value) throws Exception;
	Class<T> getClazz() throws Exception;
}
