package com.th3l4b.srm.codegen.java.base;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.srm.codegen.template.runtime.CodegenNames;

public class JavaNames extends CodegenNames {

	private static final String PREFIX = JavaNames.class.getPackage().getName()
			+ ".names";
	public static final String PROPERTY_IDENTIFIER = PREFIX + ".identifier";

	public String name(final INamedPropertied item) throws Exception {
		return getPropertyOrDefaultValue(PROPERTY_IDENTIFIER, item,
				new StringGetter() {
					@Override
					public String get() throws Exception {
						return JavaNames.super.name(item);
					}
				});
	}

}
