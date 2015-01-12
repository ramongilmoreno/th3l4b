package com.th3l4b.srm.codegen.java.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IUpdater;

public abstract class AbstractInMemoryModelRuntime implements IUpdater {

	protected abstract Map<IIdentifier, IInstance> getMap() throws Exception;
	
	protected abstract IContainer<IEntityRuntime> entities () throws Exception;
	
	protected abstract IFinder finder () throws Exception;

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
		IEntityRuntime er = entities().get(newEntity.coordinates().getIdentifier()
				.getType());
		IInstance copy = er.create();
		if (oldEntity != null) {
			er.copy(oldEntity, copy);
		}
		er.copy(newEntity, copy);
		er.unSetNulls(copy);

		EntityStatus newStatus = null;
		EntityStatus oldStatus = copy.coordinates().getStatus();
		switch (oldStatus) {
		case ToDelete:
			newStatus = EntityStatus.Deleted;
			break;
		case ToModify:
			newStatus = EntityStatus.Saved;
			break;
		default:
			throw new IllegalStateException(
					"Do not know how to handle status: " + oldStatus);
		}
		copy.coordinates().setStatus(newStatus);

		getMap().put(copy.coordinates().getIdentifier(), copy);

		// Return a copy of the copy
		IInstance copy2 = er.create();
		er.copy(copy, copy2);
		return copy2;
	}
}