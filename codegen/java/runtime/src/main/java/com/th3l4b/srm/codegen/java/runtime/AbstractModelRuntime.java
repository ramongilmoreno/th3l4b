package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;

public abstract class AbstractModelRuntime extends DefaultNamed implements
		IModelRuntime {

	protected IContainer<IEntityRuntime> _entities;
	protected IFinder _finder;
	protected IUpdater _updater;

	protected abstract IContainer<IEntityRuntime> createEntities()
			throws Exception;

	@Override
	public IContainer<IEntityRuntime> entities() throws Exception {
		if (_entities == null) {
			_entities = createEntities();
		}
		return _entities;
	}

	protected abstract IFinder createFinder() throws Exception;

	@Override
	public IFinder finder() throws Exception {
		if (_finder == null) {
			_finder = createFinder();
		}
		return _finder;
	}

	protected abstract IUpdater createUpdater() throws Exception;

	@Override
	public IUpdater updater() throws Exception {
		if (_updater == null) {
			_updater = createUpdater();
		}
		return _updater;
	}
}
