package com.th3l4b.srm.android.sqlite;

import com.th3l4b.srm.model.runtime.ModelNames;

public class SQLiteNames extends ModelNames {

	private static final String PREFIX = SQLiteNames.class.getPackage()
			.getName() + ".names";
	public static final String PROPERTY_NAME = PREFIX + ".name";

	protected String customNameProperty() {
		return PROPERTY_NAME;
	}
}
