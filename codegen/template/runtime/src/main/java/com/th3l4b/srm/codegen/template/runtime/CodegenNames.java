package com.th3l4b.srm.codegen.template.runtime;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.propertied.IPropertied;
import com.th3l4b.common.text.codegen.TextUtils;

public class CodegenNames {

	public interface StringGetter {
		String get() throws Exception;
	}

	private static final String PREFIX = CodegenNames.class.getPackage()
			.getName() + ".names";
	public static final String PROPERTY_IDENTIFIER = PREFIX + ".identifier";

	protected String getPropertyOrDefaultValue(String property,
			IPropertied propertied, StringGetter defaultValue) throws Exception {
		if (propertied.getProperties().containsKey(property)) {
			return propertied.getProperties().get(property);
		} else {
			return defaultValue.get();
		}
	}

	public String name (final INamedPropertied named) throws Exception {
		return getPropertyOrDefaultValue(PROPERTY_IDENTIFIER, named,
				new StringGetter() {
					@Override
					public String get() throws Exception {
						return TextUtils.cIdentifier(named.getName());
					}
				});
	}
}
