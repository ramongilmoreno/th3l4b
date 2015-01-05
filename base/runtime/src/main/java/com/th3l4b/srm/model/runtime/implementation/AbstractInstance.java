package com.th3l4b.srm.model.runtime.implementation;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractInstance implements IInstance {

	private ICoordinates _coordinates;

	private ICoordinates createCoordinates() throws Exception {
		DefaultCoordinates r = new DefaultCoordinates();
		r.setIdentifier(new DefaultIdentifier(type()));
		r.setStatus(EntityStatus.ToCreate);
		return r;
	}

	@Override
	public ICoordinates coordinates() throws Exception {
		if (_coordinates == null) {
			_coordinates = createCoordinates();
		}
		return _coordinates;
	}
}
