package com.th3l4b.srm.model.runtime;

import java.util.Map;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.propertied.IPropertied;
import com.th3l4b.common.text.codegen.TextUtils;

public class ModelNames {

	public interface StringGetter {
		String get() throws Exception;
	}

	private static final String PREFIX = ModelNames.class.getPackage()
			.getName() + ".names";

	public static final String PROPERTY_ALIAS = PREFIX + ".alias";
	public static final String PROPERTY_IDENTIFIER = PREFIX + ".identifier";

	protected String getPropertyOrDefaultValue(String property,
			IPropertied propertied, StringGetter defaultValue) throws Exception {
		if (propertied.getProperties().containsKey(property)) {
			return propertied.getProperties().get(property);
		} else {
			return defaultValue.get();
		}
	}
	
	protected String customIdentifierProperty () { return PROPERTY_IDENTIFIER; }
	
	/**
	 * Checks if the propertied has set the {@link #customIdentifierProperty()} and if true
	 * returns that value. if not, the base identifier is given prefixes with the prefix.
	 */
	public String customIdentifier (INamedPropertied name, String prefix) throws Exception {
		String p = customIdentifierProperty();
		Map<String, String> ps = name.getProperties();
		if (ps.containsKey(p)) {
			return ps.get(p);
		} else {
			return prefix + identifier(name);
		}
	}

	public String name(final INamedPropertied named) throws Exception {
		return getPropertyOrDefaultValue(PROPERTY_ALIAS, named,
				new StringGetter() {
					@Override
					public String get() throws Exception {
						return named.getName();
					}
				});
	}

	public String identifier(final INamedPropertied named) throws Exception {
		return getPropertyOrDefaultValue(PROPERTY_IDENTIFIER, named,
				new StringGetter() {
					@Override
					public String get() throws Exception {
						return getPropertyOrDefaultValue(PROPERTY_ALIAS, named,
								new StringGetter() {
									@Override
									public String get() throws Exception {
										return TextUtils.cIdentifier(named
												.getName());
									}
								});
					}
				});
	}
}
