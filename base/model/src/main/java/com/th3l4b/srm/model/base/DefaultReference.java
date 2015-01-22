package com.th3l4b.srm.model.base;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.DefaultNamed;

public class DefaultReference extends DefaultField implements IReference {

	private String _target;
	private INamedPropertied _reverse = new DefaultNamed();

	@Override
	public String getTarget() throws Exception {
		return _target;
	}

	@Override
	public void setTarget(String target) throws Exception {
		_target = target;
	}

	@Override
	public INamedPropertied getReverse() throws Exception {
		return _reverse;
	}

}
