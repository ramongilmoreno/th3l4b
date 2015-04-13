package com.th3l4b.srm.codegen.java.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IReverseRelationship;

public abstract class AbstractInMemoryFinder implements IFinder {

	protected abstract IModelRuntime model() throws Exception;

	protected abstract IReverse reverse() throws Exception;

	protected abstract Map<IIdentifier, IInstance> getMap() throws Exception;

	@Override
	public Collection<IInstance> all(String type) throws Exception {
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		for (IInstance i : getMap().values()) {
			ICoordinates coordinates = i.coordinates();
			if ((coordinates.getStatus() == EntityStatus.Saved)
					&& NullSafe.equals(coordinates.getIdentifier().getType(),
							type)) {
				r.add(i);
			}
		}
		return r;
	}

	@Override
	public IInstance find(IIdentifier id) throws Exception {
		Map<IIdentifier, IInstance> map = getMap();
		if (!map.containsKey(id)) {
			IEntityRuntime er = model().get(id.getType());
			IInstance r = er.create();
			r.coordinates().setIdentifier(id);
			r.coordinates().setStatus(EntityStatus.Unknown);
			return r;
		} else {
			IInstance found = map.get(id);
			return found;
		}
	}

	@Override
	public Collection<IInstance> references(IIdentifier id, String relationship)
			throws Exception {
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		IReverseRelationship rr = reverse().get(id.getType()).get(relationship);
		String type = rr.getSourceType();
		IFieldRuntime fr = model().get(type).get(rr.getField());
		String key = id.getKey();
		for (IInstance e : getMap().values()) {
			if (NullSafe.equals(e.type(), type)) {
				if (NullSafe.equals(key, fr.get(e))) {
					if (NullSafe.equals(e.coordinates().getStatus(),
							EntityStatus.Saved)) {
						r.add(e);
					}
				}
			}
		}
		return r;
	}
}
