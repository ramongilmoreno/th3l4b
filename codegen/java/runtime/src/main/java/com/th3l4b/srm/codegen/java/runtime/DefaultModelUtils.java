package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.srm.model.runtime.IRuntime;

public class DefaultModelUtils {
	
	IRuntime _runtime;
	
	public DefaultModelUtils () {
	}
	
	public DefaultModelUtils (IRuntime modelRuntime) {
		_runtime = modelRuntime;
	}
	
	public IRuntime getRuntime() {
		return _runtime;
	}
	
	public void setRuntime(IRuntime runtime) {
		_runtime = runtime;
	}

}
