package com.th3l4b.srm.sync.server.persistence.actions;

public class DefaultMoveToOtherStatusAction extends DefaultBaseAction implements
		IMoveToOtherStatusAction {

	private String _status;

	public String getStatus() {
		return _status;
	}

	public void setStatus(String status) {
		_status = status;
	}

	@Override
	public String status() throws Exception {
		return _status;
	}

}
