package com.th3l4b.srm.model.runtime;

public interface IRuntimeEntity {
	ICoordinates coordinates () throws Exception;
	String entityType () throws Exception;
	String[] fields () throws Exception;
//	String get (String field) throws Exception;
//	void set (String field, String value) throws Exception;
//	boolean isSet (String field) throws Exception;
//	void unSet (String field) throws Exception;
}
