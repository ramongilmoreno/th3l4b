package com.th3l4b.srm.codegen.java.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IEntitiesRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractInMemoryFinder implements IFinder {

	protected abstract IEntitiesRuntime entities() throws Exception;

	protected abstract Map<IIdentifier, IInstance> getMap() throws Exception;

	@Override
	public Collection<IInstance> all(String type) throws Exception {
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		for (IInstance i : getMap().values()) {
			if (NullSafe
					.equals(i.coordinates().getIdentifier().getType(), type)) {
				r.add(i);
			}
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	protected <T> void all(Class<T> clazz, Collection<T> r) throws Exception {
		for (IInstance i : getMap().values()) {
			if (clazz.isAssignableFrom(i.getClass())) {
				r.add((T) i);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T find(String type, String id, Class<T> clazz)
			throws Exception {
		DefaultIdentifier di = new DefaultIdentifier(type, id);
		return (T) getMap().get(di);
	}

	@Override
	public IInstance find(IIdentifier id) throws Exception {
		Map<IIdentifier, IInstance> map = getMap();
		if (!map.containsKey(id)) {
			IEntityRuntime er = entities().get(id.getType());
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
	public Collection<IInstance> find(IIdentifier id, String relationship)
			throws Exception {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
