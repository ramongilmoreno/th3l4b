package com.th3l4b.srm.model.runtime.implementation;

import java.util.Map;

import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractInMemoryFinder implements IFinder {
	
	protected abstract AbstractModelRuntime getRuntimeModel () throws Exception;
	
	protected abstract Map<IIdentifier, IInstance> getMap () throws Exception;

	@Override
	public IInstance find(IIdentifier id) throws Exception {
		Map<IIdentifier, IInstance> map = getMap();
		if (!map.containsKey(id)) {
//			IInstance r = getRuntimeModel().create(id.getType());
//			r.coordinates().setIdentifier(id);
//			r.coordinates().setStatus(EntityStatus.Unknown);
//			return r;
			throw new UnsupportedOperationException("Not implemented");
		} else {
			return map.get(id);
		}
	}
}
