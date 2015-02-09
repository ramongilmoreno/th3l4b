package com.th3l4b.srm.codegen.java.runtime;

import java.util.HashMap;
import java.util.Map;

import com.th3l4b.common.data.named.DefaultContainer;
import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IReverseRelationship;

public class DefaultReverse implements IReverse {

	Map<String, IContainer<IReverseRelationship>> _contents = new HashMap<String, IContainer<IReverseRelationship>>();
	IContainer<IReverseRelationship> _empty = new DefaultContainer<IReverseRelationship>();

	protected IContainer<IReverseRelationship> create(String name, boolean force) {
		if (_contents.containsKey(name)) {
			return _contents.get(name);
		} else {
			if (force) {
				DefaultContainer<IReverseRelationship> r = new DefaultContainer<IReverseRelationship>();
				_contents.put(name, r);
				return r;
			} else {
				return null;
			}
		}
	}

	@Override
	public void add(IReverseRelationship reverseRelationship) throws Exception {
		create(reverseRelationship.getTargetType(), true).add(
				reverseRelationship);
	}

	@Override
	public void remove(IReverseRelationship reverseRelationship)
			throws Exception {
		IContainer<IReverseRelationship> c = create(
				reverseRelationship.getTargetType(), false);
		if (c != null) {
			c.remove(reverseRelationship.getName());
		}

	}

	@Override
	public IContainer<IReverseRelationship> get(String targetType)
			throws Exception {
		IContainer<IReverseRelationship> c = create(targetType, false);
		if (c != null) {
			return c;
		} else {
			return _empty;
		}
	}
}
