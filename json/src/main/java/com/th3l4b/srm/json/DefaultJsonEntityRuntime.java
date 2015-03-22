package com.th3l4b.srm.json;

import java.util.HashMap;
import java.util.Map;

import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IEntityRuntime;

public class DefaultJsonEntityRuntime extends
		DefaultNamedContainer<IJsonFieldRuntime> implements IJsonEntityRuntime {

	private IEntityRuntime _runtime;
	
	private Map<String, IJsonFieldRuntime> _byJsonName = new HashMap<String, IJsonFieldRuntime>();

	public DefaultJsonEntityRuntime(IEntityRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
	}

	@Override
	public IEntityRuntime runtime() throws Exception {
		return _runtime;
	}
	
	@Override
	public void add(IJsonFieldRuntime named) throws Exception {
		super.add(named);
		_byJsonName.put(named.json(), named);
	}
	
	
	@Override
	public void remove(String name) throws Exception {
		super.remove(name);
		_byJsonName.remove(name);
	}

	@Override
	public IJsonFieldRuntime getByJsonName(String name) throws Exception {
		return _byJsonName.get(name);
	}
}
