package com.th3l4b.srm.codegen.java.runtime;

import java.util.Collection;

import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;

public class DefaultModelUtils {

	IRuntime _runtime;

	public DefaultModelUtils() {
	}

	public DefaultModelUtils(IRuntime modelRuntime) {
		_runtime = modelRuntime;
	}

	public IRuntime runtime() {
		return _runtime;
	}

	public IRuntime getRuntime() {
		return _runtime;
	}

	public void setRuntime(IRuntime runtime) {
		_runtime = runtime;
	}

	public IUpdater updater() throws Exception {
		return runtime().updater();
	}

	public Collection<IInstance> update(Collection<IInstance> entities)
			throws Exception {
		return updater().update(entities);
	}
}
