package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractEntityRuntime extends
		DefaultNamedContainer<IFieldRuntime> implements IEntityRuntime {

	@Override
	public void copy(IInstance source, IInstance target) throws Exception {
		internalCopy(source, target, true);
	}

	@Override
	public void apply(IInstance source, IInstance target) throws Exception {
		internalCopy(source, target, false);
	}

	private void internalCopy(IInstance source, IInstance target, boolean copy)
			throws Exception {
		for (IFieldRuntime fr : this) {
			if (fr.isSet(source)) {
				fr.set(fr.get(source), target);
			} else {
				if (copy) {
					fr.set(null, target);
					fr.unSet(target);
				}
			}
		}
		ICoordinates sc = source.coordinates();
		ICoordinates tc = target.coordinates();
		tc.setStatus(sc.getStatus());

		IIdentifier si = sc.getIdentifier();
		IIdentifier ti = tc.getIdentifier();
		ti.setType(si.getType());
		ti.setKey(si.getKey());
	}
	
	@Override
	public void subtract(IInstance from, IInstance fields) throws Exception {
		for (IFieldRuntime fr : this) {
			if (fr.isSet(fields)) {
				fr.unSet(from);
			}
		}
	}

	@Override
	public void unSetNulls(IInstance entity) throws Exception {
		for (IFieldRuntime fr : this) {
			if (fr.isSet(entity) && (fr.get(entity) == null)) {
				fr.unSet(entity);
			}
		}
	}
}
