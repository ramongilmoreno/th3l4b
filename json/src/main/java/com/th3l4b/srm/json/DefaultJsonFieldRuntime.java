package com.th3l4b.srm.json;

import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.srm.model.runtime.IFieldRuntime;

public class DefaultJsonFieldRuntime extends DefaultNamed implements
		IJsonFieldRuntime {

	private IFieldRuntime _runtime;
	private String _field;

	public DefaultJsonFieldRuntime(IFieldRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
		_field = JsonUtils.NAMES.name(runtime);
	}

	@Override
	public IFieldRuntime runtime() throws Exception {
		return _runtime;
	}

	@Override
	public String json() throws Exception {
		return _field;
	}

}
