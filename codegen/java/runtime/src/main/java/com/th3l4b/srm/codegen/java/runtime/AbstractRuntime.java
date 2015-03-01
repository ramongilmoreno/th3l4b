package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IUpdater;

public abstract class AbstractRuntime extends DefaultNamed implements IRuntime {

	protected IModelRuntime _entities;
	protected IFinder _finder;
	protected IUpdater _updater;
	protected IReverse _reverse;

	protected abstract IModelRuntime createModel() throws Exception;

	@Override
	public IModelRuntime model() throws Exception {
		if (_entities == null) {
			_entities = createModel();
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

	protected abstract IReverse createReverse() throws Exception;

	@Override
	public IReverse reverse() throws Exception {
		if (_reverse == null) {
			_reverse = createReverse();
		}
		return _reverse;
	}

}
