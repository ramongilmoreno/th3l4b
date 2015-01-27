package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.srm.model.runtime.IModelRuntime;

public class DefaultModelUtils {
	
	IModelRuntime _modelRuntime;
	
	public DefaultModelUtils () {
	}
	
	public DefaultModelUtils (IModelRuntime modelRuntime) {
		_modelRuntime = modelRuntime;
	}
	
	public IModelRuntime getModelRuntime() {
		return _modelRuntime;
	}
	
	public void setModelRuntime(IModelRuntime modelRuntime) {
		_modelRuntime = modelRuntime;
	}

}
