package com.th3l4b.srm.model.runtime.implementation;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IRuntimeEntity;

public abstract class AbstractRuntimeEntity implements IRuntimeEntity {

	private ICoordinates _coordinates;

	private ICoordinates createCoordinates() throws Exception {
		DefaultCoordinates r = new DefaultCoordinates();
		r.setIdentifier(new DefaultIdentifier(entityType()));
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
