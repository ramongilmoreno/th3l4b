package com.th3l4b.srm.model.runtime;

public interface IInstance {
	ICoordinates coordinates () throws Exception;
	boolean empty () throws Exception;
}
