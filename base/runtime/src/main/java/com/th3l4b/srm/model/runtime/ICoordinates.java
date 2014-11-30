package com.th3l4b.srm.model.runtime;

public interface ICoordinates {
	EntityStatus getStatus() throws Exception;
	void setStatus(EntityStatus status) throws Exception;
	IIdentifier getIdentifier() throws Exception;
	void setIdentifier(IIdentifier identifier) throws Exception;
}
