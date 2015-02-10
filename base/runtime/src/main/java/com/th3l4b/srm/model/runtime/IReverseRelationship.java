package com.th3l4b.srm.model.runtime;

import com.th3l4b.common.data.INamedPropertied;

public interface IReverseRelationship extends INamedPropertied {
	String getSourceType () throws Exception;
	void setSourceType (String sourceType) throws Exception;
	String getTargetType () throws Exception;
	void setTargetType (String targetType) throws Exception;
	String getField() throws Exception;
	void setField(String fieldName) throws Exception;
}
