package com.th3l4b.srm.model.runtime;

import java.util.Collection;

public interface IFinder {
	Collection<IInstance> all (String type) throws Exception;
	IInstance find(IIdentifier id) throws Exception;
	Collection<IInstance> references(IIdentifier id, String relationship)
			throws Exception;
}
