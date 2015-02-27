package com.th3l4b.srm.mongo;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.srm.model.runtime.ModelNames;

public class MongoNames extends ModelNames {
	
	private static final String PREFIX = MongoNames.class.getPackage().getName()
			+ ".names";
	public static final String PROPERTY_IDENTIFIER = PREFIX + ".identifier";

	public String name(final INamedPropertied item) throws Exception {
		return getPropertyOrDefaultValue(PROPERTY_IDENTIFIER, item,
				new StringGetter() {
					@Override
					public String get() throws Exception {
						return MongoNames.super.name(item);
					}
				});
	}
}
