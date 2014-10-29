package com.th3l4b.common.data.named;

import com.th3l4b.common.data.propertied.DefaultPropertied;
import com.th3l4b.common.data.propertied.IPropertied;

public class DefaultNamed extends DefaultPropertied implements INamed,
		IPropertied {

	private String _name;

	@Override
	public String getName() throws Exception {
		return _name;
	}

	@Override
	public void setName(String name) throws Exception {
		_name = name;
	}

}
