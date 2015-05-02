package com.th3l4b.srm.mongo;

import com.th3l4b.srm.model.runtime.ModelNames;

public class MongoNames extends ModelNames {

	private static final String PREFIX = MongoNames.class.getPackage()
			.getName() + ".names";
	public static final String PROPERTY_NAME = PREFIX + ".name";

	protected String customNameProperty() {
		return PROPERTY_NAME;
	}
}
