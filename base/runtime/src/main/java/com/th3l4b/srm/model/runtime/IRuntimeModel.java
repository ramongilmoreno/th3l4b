package com.th3l4b.srm.model.runtime;

import java.util.Collection;

public interface IRuntimeModel {
	IRuntimeEntity create (String entityClass) throws Exception;
	void update (Collection<IRuntimeEntity> entities) throws Exception;
}
