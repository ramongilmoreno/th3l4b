package com.th3l4b.srm.sync.server.persistence.actions;

import java.util.Collection;
import java.util.HashSet;

public class DefaultBaseAction implements IBaseAction {

	private Collection<String> _statusesToDelete = new HashSet<String>();
	private String _client;

	public Collection<String> getStatusesToDelete() {
		return _statusesToDelete;
	}

	public void setStatusesToDelete(Collection<String> statusesToDelete) {
		_statusesToDelete = statusesToDelete;
	}

	public String getClient() {
		return _client;
	}

	public void setClient(String client) {
		_client = client;
	}

	@Override
	public String client() throws Exception {
		return _client;
	}

	@Override
	public Collection<String> statusesToDelete() throws Exception {
		return _statusesToDelete;
	}

}
