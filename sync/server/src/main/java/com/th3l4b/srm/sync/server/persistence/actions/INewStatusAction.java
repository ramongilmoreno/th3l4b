package com.th3l4b.srm.sync.server.persistence.actions;

import java.util.Collection;

import com.th3l4b.srm.model.runtime.IInstance;

public interface INewStatusAction extends IBaseAction {

	/**
	 * If {@link #statusToCreate()} is not null, the new status will be linked
	 * to these statuses.
	 */
	Collection<String> statusesForNewStatus() throws Exception;

	/**
	 * Updates of thew new status
	 */
	Collection<IInstance> updates() throws Exception;
}
