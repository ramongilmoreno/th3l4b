package com.th3l4b.srm.sync.server.persistence.actions;

import java.util.Collection;

public interface IBaseAction {
	
	/**
	 * Id of the client to whom this action refers.
	 */
	String client () throws Exception;

	Collection<String> statusesToDelete() throws Exception;
}
