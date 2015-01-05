package com.th3l4b.srm.model.runtime;

public interface IFinder {
	IInstance find(IIdentifier id) throws Exception;
}
