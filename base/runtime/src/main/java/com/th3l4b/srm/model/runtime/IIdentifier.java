package com.th3l4b.srm.model.runtime;

public interface IIdentifier {
	String getKey () throws Exception;
	void setKey (String key) throws Exception;
	String getType () throws Exception;
	void setType (String type) throws Exception;
}
