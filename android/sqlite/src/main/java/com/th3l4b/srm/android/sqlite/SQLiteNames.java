package com.th3l4b.srm.android.sqlite;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.srm.model.runtime.ModelNames;

public class SQLiteNames extends ModelNames {
	
	private static final String PREFIX = SQLiteNames.class.getPackage().getName()
			+ ".names";
	public static final String PROPERTY_IDENTIFIER = PREFIX + ".identifier";

	public String name(final INamedPropertied item) throws Exception {
		return getPropertyOrDefaultValue(PROPERTY_IDENTIFIER, item,
				new StringGetter() {
					@Override
					public String get() throws Exception {
						return SQLiteNames.super.name(item);
					}
				});
	}
}
