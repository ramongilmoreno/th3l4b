package com.th3l4b.srm.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.th3l4b.srm.codegen.java.runtime.AbstractUpdater;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractMongoUpdater extends AbstractUpdater {

	protected abstract DB getDB() throws Exception;

	protected abstract IMongoModelRuntime mongoModel() throws Exception;

	@Override
	protected void insert(IInstance entity) throws Exception {
		IMongoEntityRuntime mer = mongoModel().get(
				entity.coordinates().getIdentifier().getType());
		DBCollection collection = getDB().getCollection(mer.collection());
		BasicDBObject o = new BasicDBObject();
		mer.apply(entity, o);
		collection.insert(o);
	}

	@Override
	protected void update(IInstance newEntity, IInstance oldEntity)
			throws Exception {

		IMongoEntityRuntime mer = mongoModel().get(
				newEntity.coordinates().getIdentifier().getType());
		DBCollection collection = getDB().getCollection(mer.collection());

		// Obtain previous object id
		BasicDBObject old = new BasicDBObject(IMongoConstants.FIELD_ID,
				newEntity.coordinates().getIdentifier().getKey());
		DBObject found = collection.findOne(old);
		BasicDBObject o = new BasicDBObject();
		mer.apply(newEntity, o);

		collection.update(found, o);
	}
}
