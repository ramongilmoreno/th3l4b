package com.th3l4b.srm.sync.server.persistence.actions;

public interface IMoveToOtherStatusAction extends IBaseAction {

	/**
	 * Status for the client
	 */
	String status () throws Exception;

}
