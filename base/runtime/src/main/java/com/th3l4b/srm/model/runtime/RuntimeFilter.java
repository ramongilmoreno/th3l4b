package com.th3l4b.srm.model.runtime;

import java.util.Map;

public class RuntimeFilter implements IRuntime {
	
	private IRuntime _delegated;
	
	public String getName() throws Exception {
		return _delegated.getName();
	}

	public Map<String, String> getProperties() throws Exception {
		return _delegated.getProperties();
	}

	public void setName(String name) throws Exception {
		_delegated.setName(name);
	}

	public IModelRuntime model() throws Exception {
		return _delegated.model();
	}

	public Map<String, Object> getAttributes() throws Exception {
		return _delegated.getAttributes();
	}

	public IReverse reverse() throws Exception {
		return _delegated.reverse();
	}

	public IFinder finder() throws Exception {
		return _delegated.finder();
	}

	public IUpdater updater() throws Exception {
		return _delegated.updater();
	}

	public RuntimeFilter () {
	}
	
	public RuntimeFilter (IRuntime delegated) {
		_delegated = delegated;
	}
	
	public IRuntime getDelegated() {
		return _delegated;
	}
	
	public void setDelegated(IRuntime delegated) {
		_delegated = delegated;
	}

}
