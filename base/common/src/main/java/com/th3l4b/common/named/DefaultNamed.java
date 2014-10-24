package com.th3l4b.common.named;

public class DefaultNamed implements INamed {

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
