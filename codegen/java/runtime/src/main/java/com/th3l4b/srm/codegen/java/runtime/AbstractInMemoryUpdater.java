package com.th3l4b.srm.codegen.java.runtime;

import java.util.Map;

import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractInMemoryUpdater extends AbstractUpdater {

	protected abstract Map<IIdentifier, IInstance> getMap() throws Exception;

	protected void persist(IInstance entity) throws Exception {
		// Persist the new value to the map
		getMap().put(entity.coordinates().getIdentifier(), entity);
	}

	@Override
	protected void update(IInstance newEntity, IInstance oldEntity)
			throws Exception {
		persist(newEntity);
	}

	@Override
	protected void insert(IInstance entity) throws Exception {
		persist(entity);
	}

}
