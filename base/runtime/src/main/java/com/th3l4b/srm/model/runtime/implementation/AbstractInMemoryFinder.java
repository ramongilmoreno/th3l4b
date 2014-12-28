package com.th3l4b.srm.model.runtime.implementation;

import java.util.Map;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IRuntimeEntity;

public abstract class AbstractInMemoryFinder implements IFinder {
	
	protected abstract AbstractRuntimeModel getRuntimeModel () throws Exception;
	
	protected abstract Map<IIdentifier, IRuntimeEntity> getMap () throws Exception;

	@Override
	public IRuntimeEntity find(IIdentifier id) throws Exception {
		Map<IIdentifier, IRuntimeEntity> map = getMap();
		if (!map.containsKey(id)) {
			IRuntimeEntity r = getRuntimeModel().create(id.getType());
			r.coordinates().setIdentifier(id);
			r.coordinates().setStatus(EntityStatus.Unknown);
			return r;
		} else {
			return map.get(id);
		}
	}
}
