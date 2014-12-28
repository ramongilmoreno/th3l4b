package com.th3l4b.srm.model.runtime.implementation;

import java.util.HashMap;
import java.util.Map;

import com.th3l4b.srm.model.runtime.IRuntimeEntity;
import com.th3l4b.srm.model.runtime.IRuntimeModel;

public abstract class AbstractRuntimeModel implements IRuntimeModel {

	private Map<String, IRuntimeEntityFactory> _factories = new HashMap<String, IRuntimeEntityFactory>();
	
	protected void add (String type, IRuntimeEntityFactory factory) {
		_factories.put(type, factory);
	}

	@Override
	public IRuntimeEntity create(String entityClass) throws Exception {
		return _factories.get(entityClass).create();
	}

}
