package com.th3l4b.common.data.named;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.propertied.DefaultPropertied;

public class DefaultNamed extends DefaultPropertied implements INamedPropertied {

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
