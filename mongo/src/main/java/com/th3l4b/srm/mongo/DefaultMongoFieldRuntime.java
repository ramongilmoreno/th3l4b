package com.th3l4b.srm.mongo;

import com.mongodb.DBObject;
import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public class DefaultMongoFieldRuntime extends DefaultNamed implements
		IMongoFieldRuntime {

	private IFieldRuntime _runtime;
	private String _field;

	public DefaultMongoFieldRuntime(IFieldRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
		_field = MongoUtils.NAMES.name(runtime,
				IMongoConstants.PREFIX_FIELDS);
	}

	public String getField() {
		return _field;
	}

	public void setField(String field) {
		_field = field;
	}

	@Override
	public String field() throws Exception {
		return _field;
	}

	@Override
	public IFieldRuntime runtime() throws Exception {
		return _runtime;
	}

	@Override
	public void apply(DBObject o, IInstance instance) throws Exception {
		String f = field();
		if (o.containsField(f)) {
			Object v = o.get(f);
			if (v instanceof String) {
				String sv = (String) v;
				runtime().set(sv, instance);
			}
		}
	}

	@Override
	public void apply(IInstance instance, DBObject o) throws Exception {
		IFieldRuntime r = runtime();
		if (r.isSet(instance)) {
			String v = r.get(instance);
			o.put(field(), v);
		}
	}

}
