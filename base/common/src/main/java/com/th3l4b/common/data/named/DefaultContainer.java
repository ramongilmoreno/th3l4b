package com.th3l4b.common.data.named;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.th3l4b.common.data.propertied.DefaultPropertied;
import com.th3l4b.common.data.propertied.IPropertied;

public class DefaultContainer<N extends INamed> extends DefaultPropertied
		implements IContainer<N>, IPropertied {

	LinkedHashMap<String, N> _map = new LinkedHashMap<String, N>();

	@Override
	public Iterator<N> iterator() {
		return _map.values().iterator();
	}

	@Override
	public N get(String name) throws Exception {
		return _map.get(name);
	}

	@Override
	public void add(N named) throws Exception {
		if (_map.containsKey(named.getName())) {
			throw new IllegalArgumentException("Name already in container: "
					+ named.getName());
		}
		_map.put(named.getName(), named);
	}

	@Override
	public void remove(String name) throws Exception {
		if (!_map.containsKey(name)) {
			throw new IllegalArgumentException("Name not in container: " + name);
		}
		_map.remove(name);
	}

	@Override
	public boolean contains(String name) throws Exception {
		return _map.containsKey(name);
	}

}
