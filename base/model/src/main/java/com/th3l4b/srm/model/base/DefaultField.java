package com.th3l4b.srm.model.base;

import com.th3l4b.common.data.named.DefaultNamed;

public class DefaultField extends DefaultNamed implements IField {

	private String _type;
	private boolean _isReference;
	
	@Override
	public String getType() throws Exception {
	return _type;
	}

	@Override
	public void setType(String type) throws Exception {
		_type = type;
	}

	@Override
	public boolean isReference() throws Exception {
	return _isReference;
	}

	@Override
	public void setReference(boolean isReference) throws Exception {
		_isReference = isReference;
	}

}
