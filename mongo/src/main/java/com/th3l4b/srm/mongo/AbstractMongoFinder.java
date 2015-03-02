package com.th3l4b.srm.mongo;

import java.util.ArrayList;
import java.util.Collection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IReverseRelationship;

public abstract class AbstractMongoFinder implements IFinder {

	protected abstract IMongoModelRuntime mongoModel() throws Exception;

	protected abstract IReverse reverse() throws Exception;

	protected abstract DB getDB() throws Exception;

	@Override
	public Collection<IInstance> all(String type) throws Exception {
		IMongoEntityRuntime mer = mongoModel().get(type);
		IEntityRuntime er = mer.runtime();
		DBCollection collection = getDB().getCollection(mer.collection());
		BasicDBObject bo = new BasicDBObject(IMongoConstants.FIELD_STATUS,
				EntityStatus.Saved.name());
		DBCursor cursor = collection.find(bo);
		try {
			ArrayList<IInstance> r = new ArrayList<IInstance>();
			while (cursor.hasNext()) {
				DBObject o = cursor.next();
				IInstance i = er.create();
				mer.apply(o, i);
				r.add(i);
			}
			return r;
		} finally {
			cursor.close();
		}
	}

	@Override
	public IInstance find(IIdentifier id) throws Exception {
		IMongoEntityRuntime mer = mongoModel().get(id.getType());
		DBCollection collection = getDB().getCollection(mer.collection());
		DBObject found = collection.findOne(new BasicDBObject(
				IMongoConstants.FIELD_ID, id.getKey()));
		IInstance r = mer.runtime().create();
		if (found != null) {
			mer.apply(found, r);
		} else {
			ICoordinates c = r.coordinates();
			c.setIdentifier(id);
			c.setStatus(EntityStatus.Unknown);
		}
		return r;
	}

	@Override
	public Collection<IInstance> references(IIdentifier id, String relationship)
			throws Exception {
		IReverseRelationship rr = reverse().get(id.getType()).get(relationship);
		IMongoEntityRuntime mer = mongoModel().get(rr.getSourceType());
		BasicDBObject of = new BasicDBObject(mer.get(rr.getField()).field(),
				id.getKey());
		BasicDBObject o = new BasicDBObject(IMongoConstants.FIELD_FIELDS, of);
		DBCursor find = getDB().getCollection(mer.collection()).find(o);
		IEntityRuntime er = mer.runtime();
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		while (find.hasNext()) {
			IInstance i = er.create();
			mer.apply(find.next(), i);
			r.add(i);
		}
		return r;
	}

}
