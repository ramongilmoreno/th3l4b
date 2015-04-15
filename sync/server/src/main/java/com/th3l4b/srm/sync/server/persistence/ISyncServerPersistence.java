package com.th3l4b.srm.sync.server.persistence;

import java.util.Collection;

import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.sync.server.persistence.actions.IMoveToOtherStatusAction;
import com.th3l4b.srm.sync.server.persistence.actions.INewStatusAction;

public interface ISyncServerPersistence {
	
	Collection<String> liveStatuses () throws Exception;
	
	String newStatus(INewStatusAction action) throws Exception;

	void moveToOtherStatus(IMoveToOtherStatusAction action) throws Exception;

	/**
	 * @return The name of the initial status of the client
	 */
	String createClient(String id) throws Exception;

	/**
	 * @return Null if the client does not exist. Existing clients shall not
	 *         keep and unexisting status.
	 */
	String clientStatus(String id) throws Exception;

	Collection<String> statusesThisStatusDependsOn(String to) throws Exception;

	Collection<String> statusesThatDependsOnThisStatus(String from)
			throws Exception;

	Collection<IInstance> updates(String status) throws Exception;
	
	IModelRuntime modelRuntime () throws Exception;
}
