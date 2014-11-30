package com.th3l4b.srm.model.runtime;

public interface IFinder {
	IRuntimeEntity find(IIdentifier id) throws Exception;
	IRuntimeEntity find(String entityType, String id) throws Exception;
}
