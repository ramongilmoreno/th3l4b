package com.th3l4b.srm.json;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.srm.model.runtime.ModelNames;

public class JsonNames extends ModelNames {
	
	private static final String PREFIX = JsonNames.class.getPackage().getName()
			+ ".names";
	public static final String PROPERTY_NAME = PREFIX + ".name";

	public String name(final INamedPropertied item) throws Exception {
		return getPropertyOrDefaultValue(PROPERTY_NAME, item,
				new StringGetter() {
					@Override
					public String get() throws Exception {
						return JsonNames.super.name(item);
					}
				});
	}
	
	public String identifier(INamedPropertied item) throws Exception {
		// Identifier is actually a name in the JSON context
		return name(item);
	}
}
