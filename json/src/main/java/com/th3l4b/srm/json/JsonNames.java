package com.th3l4b.srm.json;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.srm.model.runtime.ModelNames;

/**
 * JSON names are not converted to identifiers.
 */
public class JsonNames extends ModelNames {
	
	private static final String PREFIX = JsonNames.class.getPackage().getName()
			+ ".names";
	public static final String PROPERTY_NAME = PREFIX + ".name";

	protected String customNameProperty() {
		return PROPERTY_NAME;
	}
	
	@Override
	protected String identifier(INamedPropertied named) throws Exception {
		return named.getName();
	}
}
