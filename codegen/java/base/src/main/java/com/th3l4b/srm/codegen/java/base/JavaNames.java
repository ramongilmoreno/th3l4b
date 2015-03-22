package com.th3l4b.srm.codegen.java.base;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.srm.model.runtime.ModelNames;

public class JavaNames extends ModelNames {

	private static final String PREFIX = JavaNames.class.getPackage().getName()
			+ ".names";
	public static final String PROPERTY_IDENTIFIER = PREFIX + ".identifier";

	public String name(INamedPropertied item) throws Exception {
		// Name is actually an identifier in the Java context
		return identifier(item);
	}

	public String identifier(final INamedPropertied item) throws Exception {
		return getPropertyOrDefaultValue(PROPERTY_IDENTIFIER, item,
				new StringGetter() {
					@Override
					public String get() throws Exception {
						return JavaNames.super.identifier(item);
					}
				});
	}

}
