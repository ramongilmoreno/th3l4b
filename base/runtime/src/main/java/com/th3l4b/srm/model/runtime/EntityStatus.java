package com.th3l4b.srm.model.runtime;

public enum EntityStatus {
	Saved, Deleted, Unknown, Ignore, ToModify(true), ToDelete(true);
	
	boolean _action = false;
	
	EntityStatus () {
		this(false);
	}
	
	EntityStatus (boolean action) {
		_action = action;
	}
	
	public boolean isAction() {
		return _action;
	}
}
