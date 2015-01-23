package com.th3l4b.srm.base.parser;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IField;
import com.th3l4b.srm.model.base.IModel;
import com.th3l4b.srm.model.base.IReference;

public class ModelTester {
	public static void test(IModel model) throws Exception {
		// Check for references names
		for (IEntity e : model) {
			for (IField f : e) {
				if (f instanceof IReference) {
					IReference r = (IReference) f;
					for (IEntity e2 : model) {
						if (NullSafe.equals(r.getTarget(), e2.getName())) {
							for (IField f2 : e2) {
								if (f2 instanceof IReference) {
									IReference r2 = (IReference) f2;
									if (NullSafe.equals(r2.getReverse()
											.getName(), f.getName())) {
										throw new IllegalStateException(
												"Field ["
														+ f.getName()
														+ "] of entity ["
														+ e.getName()
														+ "] is same as reverse of relationship ["
														+ f2.getName()
														+ "] of entity ["
														+ e2.getName() + "]");
									}

								}
							}
						}
					}
				}
			}
		}
	}
}
