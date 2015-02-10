package com.th3l4b.srm.codegen.java.base;

import java.util.ArrayList;
import java.util.Collection;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.srm.codegen.java.runtime.DefaultReverseRelationship;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IField;
import com.th3l4b.srm.model.base.IModel;
import com.th3l4b.srm.model.base.IReference;
import com.th3l4b.srm.model.runtime.IReverseRelationship;

public class ReverseInspector {
	public static Collection<IReverseRelationship> reverse(IModel model)
			throws Exception {
		ArrayList<IReverseRelationship> r = new ArrayList<IReverseRelationship>();
		for (IEntity source : model) {
			for (IField f : source) {
				if (f instanceof IReference) {
					IReference ref = (IReference) f;
					INamedPropertied reverse = ref.getReverse();
					DefaultReverseRelationship rr = new DefaultReverseRelationship(
							source.getName(), f.getName(), ref.getTarget(),
							reverse.getName());
					DefaultNamed.copy(reverse, rr);
					r.add(rr);
				}
			}
		}
		return r;
	}
}
