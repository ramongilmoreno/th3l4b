package com.th3l4b.common.data.named;

import java.util.Iterator;

public class DefaultNamedContainer<N extends INamed> extends DefaultNamed
		implements IContainer<N> {

	DefaultContainer<N> _delegated = new DefaultContainer<N>();

	public Iterator<N> iterator() {
		return _delegated.iterator();
	}

	public N get(String name) throws Exception {
		return _delegated.get(name);
	}

	public void add(N named) throws Exception {
		_delegated.add(named);
	}

	public void remove(String name) throws Exception {
		_delegated.remove(name);
	}

	public boolean contains(String name) throws Exception {
		return _delegated.contains(name);
	}
}
