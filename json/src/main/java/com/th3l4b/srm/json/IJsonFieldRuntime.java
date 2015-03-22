package com.th3l4b.srm.json;

import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.runtime.IFieldRuntime;

public interface IJsonFieldRuntime extends INamed {
	IFieldRuntime runtime() throws Exception;
	String json() throws Exception;
}
