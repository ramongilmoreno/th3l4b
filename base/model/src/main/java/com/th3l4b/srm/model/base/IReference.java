package com.th3l4b.srm.model.base;

import com.th3l4b.common.data.INamedPropertied;

public interface IReference extends IField {
	String getTarget() throws Exception;
	void setTarget(String type) throws Exception;
	INamedPropertied getReverse () throws Exception;
}
