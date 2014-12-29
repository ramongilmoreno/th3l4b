package com.th3l4b.srm.model.base;

import com.th3l4b.common.data.named.DefaultNamed;

public class DefaultField extends DefaultNamed implements IField {

	private String _target;
	
	@Override
	public String getTarget() throws Exception {
	return _target;
	}

	@Override
	public void setTarget(String target) throws Exception {
		_target = target;
	}

}
