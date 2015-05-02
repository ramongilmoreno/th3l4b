package com.th3l4b.srm.codegen.java.base;

import com.th3l4b.srm.model.runtime.ModelNames;

public class JavaNames extends ModelNames {

	private static final String PREFIX = JavaNames.class.getPackage().getName()
			+ ".names";
	public static final String PROPERTY_NAME = PREFIX + ".name";

	protected String customNameProperty() {
		return PROPERTY_NAME;
	}
}
