package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.srm.model.runtime.IReferenceRuntime;

public abstract class AbstractReferenceRuntime extends AbstractFieldRuntime
		implements IReferenceRuntime {

	private String _targetType;

	private INamedPropertied _reverse = new DefaultNamed();

	public String getTargetType() {
		return _targetType;
	}

	public void setTargetType(String targetType) {
		_targetType = targetType;
	}

	@Override
	public String targetType() throws Exception {
		return _targetType;
	}

	@Override
	public INamedPropertied reverse() throws Exception {
		return _reverse;
	}

}
