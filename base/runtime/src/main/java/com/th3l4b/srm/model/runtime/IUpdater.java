package com.th3l4b.srm.model.runtime;

import java.util.Collection;

public interface IUpdater {
	Collection<IInstance> update(Collection<IInstance> entities)
			throws Exception;
}
