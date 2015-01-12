package com.th3l4b.srm.model.runtime;

public interface IReverseRelationship {
	String getSourceType () throws Exception;
	void setSourceType (String sourceType) throws Exception;
	String getTargetType () throws Exception;
	void setTargetType (String targetType) throws Exception;
	String getField() throws Exception;
	void setField(String fieldName) throws Exception;
}
