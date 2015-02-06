package com.th3l4b.srm.codegen.java.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IEntitiesRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IUpdater;

public abstract class AbstractInMemoryUpdater implements IUpdater {

	protected abstract Map<IIdentifier, IInstance> getMap() throws Exception;

	protected abstract IEntitiesRuntime entities() throws Exception;

	protected abstract IFinder finder() throws Exception;

	@Override
	public Collection<IInstance> update(Collection<IInstance> entities)
			throws Exception {
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		for (IInstance e : entities) {
			if (!e.coordinates().getStatus().isAction()) {
				continue;
			}
			IInstance found = finder().find(e.coordinates().getIdentifier());
			if (found.coordinates().getStatus() == EntityStatus.Unknown) {
				r.add(insert(e));
			} else {
				r.add(update(e, found));
			}
		}

		return r;
	}

	protected IInstance insert(IInstance entity) throws Exception {
		return internalUpdate(entity, null);
	}

	protected IInstance update(IInstance newEntity, IInstance oldEntity)
			throws Exception {
		return internalUpdate(newEntity, oldEntity);
	}

	protected IInstance internalUpdate(IInstance newEntity, IInstance oldEntity)
			throws Exception {
		// Decide result
		EntityStatus newStatus = null;
		EntityStatus action = newEntity.coordinates().getStatus();
		switch (action) {
		case ToSave:
			newStatus = EntityStatus.Saved;
			break;
		case ToDelete:
			newStatus = EntityStatus.Deleted;
			break;
		case ToMerge:
			if ((oldEntity != null)
					&& (oldEntity.coordinates().getStatus() == EntityStatus.Deleted)) {
				newStatus = EntityStatus.Deleted;
			} else {
				newStatus = EntityStatus.Saved;
			}
			break;
		default:
			throw new IllegalStateException(
					"Do not know how to handle status: " + action);
		}

		// Create a copy and apply changes
		IEntityRuntime er = entities().get(
				newEntity.coordinates().getIdentifier().getType());
		IInstance copy = er.create();
		if (oldEntity != null) {
			er.copy(oldEntity, copy);
		}
		er.copy(newEntity, copy);
		er.unSetNulls(copy);
		copy.coordinates().setStatus(newStatus);

		// Persist the new value to the map
		getMap().put(copy.coordinates().getIdentifier(), copy);

		// Return a copy of the copy
		IInstance copy2 = er.create();
		er.copy(copy, copy2);
		return copy2;
	}
}
