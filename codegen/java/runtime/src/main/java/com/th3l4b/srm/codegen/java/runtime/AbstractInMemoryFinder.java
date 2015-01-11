package com.th3l4b.srm.codegen.java.runtime;

import java.util.Map;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractInMemoryFinder implements IFinder {
	
	protected abstract DefaultEntitiesRuntime getModelRuntime () throws Exception;
	
	protected abstract Map<IIdentifier, IInstance> getMap () throws Exception;

	@Override
	public IInstance find(IIdentifier id) throws Exception {
		Map<IIdentifier, IInstance> map = getMap();
		if (!map.containsKey(id)) {
			IEntityRuntime er = getModelRuntime().get(id.getType());
			IInstance r = er.create();
			r.coordinates().setIdentifier(id);
			r.coordinates().setStatus(EntityStatus.Unknown);
			return r;
		} else {
			return map.get(id);
		}
	}
}
