package com.th3l4b.srm.cassandra;

import com.th3l4b.srm.model.runtime.ModelNames;

public class CassandraNames extends ModelNames {

	private static final String PREFIX = CassandraNames.class.getPackage()
			.getName() + ".names";
	public static final String PROPERTY_IDENTIFIER = PREFIX + ".identifier";

	protected String customIdentifierProperty() {
		return PROPERTY_IDENTIFIER;
	}
}
