package com.th3l4b.srm.model.runtime;

import java.util.Collection;

public class FinderFilter implements IFinder {

	IFinder _delegated;

	public FinderFilter() {
	}

	public FinderFilter(IFinder delegated) {
		_delegated = delegated;
	}

	public IFinder getDelegated() {
		return _delegated;
	}

	public void setDelegated(IFinder delegated) {
		_delegated = delegated;
	}

	public Collection<IInstance> all(String type) throws Exception {
		return _delegated.all(type);
	}

	public IInstance find(IIdentifier id) throws Exception {
		return _delegated.find(id);
	}

	public Collection<IInstance> references(IIdentifier id, String relationship)
			throws Exception {
		return _delegated.references(id, relationship);
	}

}
