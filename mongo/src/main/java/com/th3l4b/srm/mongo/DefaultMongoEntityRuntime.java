package com.th3l4b.srm.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public class DefaultMongoEntityRuntime extends
		DefaultNamedContainer<IMongoFieldRuntime> implements
		IMongoEntityRuntime {

	private IEntityRuntime _runtime;
	private String _collection;

	public DefaultMongoEntityRuntime(IEntityRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
		_collection = MongoUtils.NAMES.customIdentifier(runtime,
				IMongoConstants.PREFIX_TABLES);
	}

	@Override
	public String collection() throws Exception {
		return _collection;
	}

	@Override
	public void apply(DBObject o, IInstance instance) throws Exception {
		MongoUtils.FIELD_RUNTIME_ID.apply(o, instance);
		MongoUtils.FIELD_RUNTIME_STATUS.apply(o, instance);

		// Load subfields
		Object fields = o.get(IMongoConstants.FIELD_FIELDS);
		if (fields instanceof DBObject) {
			DBObject o2 = (DBObject) fields;
			for (IMongoFieldRuntime f : this) {
				f.apply(o2, instance);
			}
		}
	}

	@Override
	public void apply(IInstance instance, DBObject o) throws Exception {
		MongoUtils.FIELD_RUNTIME_ID.apply(instance, o);
		MongoUtils.FIELD_RUNTIME_STATUS.apply(instance, o);

		// Apply subfields
		BasicDBObject o2 = new BasicDBObject();
		for (IMongoFieldRuntime f : this) {
			f.apply(instance, o2);
		}
		if (!o2.isEmpty()) {
			o.put(IMongoConstants.FIELD_FIELDS, o2);
		}
	}

	@Override
	public IEntityRuntime runtime() throws Exception {
		return _runtime;
	}
}
