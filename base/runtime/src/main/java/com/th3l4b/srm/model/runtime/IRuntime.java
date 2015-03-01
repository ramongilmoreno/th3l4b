package com.th3l4b.srm.model.runtime;

import com.th3l4b.common.data.INamedPropertied;

public interface IRuntime extends INamedPropertied {
	IEntitiesRuntime entities () throws Exception;
	IReverse reverse () throws Exception;
	IFinder finder () throws Exception;
	IUpdater updater () throws Exception;
}
