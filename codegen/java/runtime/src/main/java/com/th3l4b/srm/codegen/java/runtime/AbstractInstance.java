package com.th3l4b.srm.codegen.java.runtime;

import java.io.Serializable;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IInstance;

@SuppressWarnings("serial")
public abstract class AbstractInstance implements IInstance, Serializable {

	private ICoordinates _coordinates;
	
	protected abstract String type () throws Exception;

	private ICoordinates createCoordinates() throws Exception {
		DefaultCoordinates r = new DefaultCoordinates();
		r.setIdentifier(new DefaultIdentifier(type()));
		r.setStatus(EntityStatus.ToMerge);
		return r;
	}

	@Override
	public ICoordinates coordinates() throws Exception {
		if (_coordinates == null) {
			_coordinates = createCoordinates();
		}
		return _coordinates;
	}

	@Override
	public String toString() {
		try {
			return NullSafe.toString(coordinates());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
