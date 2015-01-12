package com.th3l4b.srm.codegen.java.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IReverseRelationship;

public class DefaultReverse implements IReverse {

	Map<String, Collection<IReverseRelationship>> _contents = new HashMap<String, Collection<IReverseRelationship>>();

	protected Collection<IReverseRelationship> create(String name, boolean force) {
		if (_contents.containsKey(name)) {
			return _contents.get(name);
		} else {
			if (force) {
				ArrayList<IReverseRelationship> r = new ArrayList<IReverseRelationship>();
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
		Collection<IReverseRelationship> c = create(
				reverseRelationship.getTargetType(), false);
		if (c != null) {
			c.remove(reverseRelationship);
		}

	}

	@Override
	public Collection<IReverseRelationship> get(String targetType)
			throws Exception {
		Collection<IReverseRelationship> c = create(targetType, false);
		if (c != null) {
			return c;
		} else {
			return Collections.<IReverseRelationship> emptyList();
		}
	}
}
