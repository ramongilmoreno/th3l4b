package com.th3l4b.srm.sync.server.persistence.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.th3l4b.srm.model.runtime.IInstance;

public class DefaultNewStatusAction extends DefaultBaseAction implements INewStatusAction {

	private Collection<IInstance> _updates = new ArrayList<IInstance>();
	private Collection<String> _statusesForNewStatus = new HashSet<String>();
	
	public Collection<IInstance> getUpdates() {
		return _updates;
	}

	public void setUpdates(Collection<IInstance> updates) {
		_updates = updates;
	}

	public Collection<String> getStatusesForNewStatus() {
		return _statusesForNewStatus;
	}

	public void setStatusesForNewStatus(Collection<String> statusesForNewStatus) {
		_statusesForNewStatus = statusesForNewStatus;
	}

	@Override
	public Collection<String> statusesForNewStatus() throws Exception {
		return _statusesForNewStatus;
	}

	@Override
	public Collection<IInstance> updates() throws Exception {
		return _updates;
	}

}
