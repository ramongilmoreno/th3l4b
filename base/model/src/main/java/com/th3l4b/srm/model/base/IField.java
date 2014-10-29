package com.th3l4b.srm.model.base;

import com.th3l4b.common.data.named.INamed;

public interface IField extends INamed {
	String getType () throws Exception;
	void setType (String type) throws Exception;
	boolean isReference () throws Exception;
	void setReference (boolean isReference) throws Exception;
}
