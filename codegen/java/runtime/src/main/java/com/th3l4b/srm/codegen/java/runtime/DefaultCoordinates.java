package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IIdentifier;

public class DefaultCoordinates implements ICoordinates {

	private EntityStatus _status;
	private IIdentifier _identifier;

	@Override
	public EntityStatus getStatus() throws Exception {
		return _status;
	}

	@Override
	public void setStatus(EntityStatus status) throws Exception {
		_status = status;
	}

	@Override
	public IIdentifier getIdentifier() throws Exception {
		return _identifier;
	}

	@Override
	public void setIdentifier(IIdentifier identifier) throws Exception {
		_identifier = identifier;
	}

	@Override
	public String toString() {
		return "" + _identifier + " - " + _status;
	}
}
