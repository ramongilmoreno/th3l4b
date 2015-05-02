package com.th3l4b.srm.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.th3l4b.srm.codegen.java.runtime.DefaultIdentifierFieldRuntime;
import com.th3l4b.srm.codegen.java.runtime.DefaultStatusFieldRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IReferenceRuntime;

public class MongoUtils {

	public static final MongoNames NAMES = new MongoNames();

	public static IMongoFieldRuntime FIELD_RUNTIME_ID;
	public static IMongoFieldRuntime FIELD_RUNTIME_STATUS;

	static {
		try {
			DefaultIdentifierFieldRuntime idr = new DefaultIdentifierFieldRuntime(
					IMongoConstants.FIELD_ID);
			idr.getProperties().put(MongoNames.PROPERTY_NAME,
					IMongoConstants.FIELD_ID);
			FIELD_RUNTIME_ID = new DefaultMongoFieldRuntime(idr);
			DefaultStatusFieldRuntime statusr = new DefaultStatusFieldRuntime(
					IMongoConstants.FIELD_STATUS);
			statusr.getProperties().put(MongoNames.PROPERTY_NAME,
					IMongoConstants.FIELD_STATUS);
			FIELD_RUNTIME_STATUS = new DefaultMongoFieldRuntime(statusr);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static IMongoModelRuntime create(IModelRuntime model)
			throws Exception {
		DefaultMongoModelRuntime mmr = new DefaultMongoModelRuntime(model);
		for (IEntityRuntime er : model) {
			DefaultMongoEntityRuntime mer = new DefaultMongoEntityRuntime(er);
			for (IFieldRuntime fr : er) {
				DefaultMongoFieldRuntime mfr = new DefaultMongoFieldRuntime(fr);
				mer.add(mfr);
			}
			mmr.add(mer);
		}

		return mmr;
	}

	public static void ensureIndexes(IMongoModelRuntime mmr, DB db)
			throws Exception {
		for (IMongoEntityRuntime mer : mmr) {
			DBCollection c = db.getCollection(mer.collection());

			{
				// Create index on id and status
				BasicDBObject bdbo = new BasicDBObject();
				bdbo.put(FIELD_RUNTIME_ID.field(), 1);
				bdbo.put(FIELD_RUNTIME_STATUS.field(), 1);
				c.ensureIndex(bdbo);
			}
			for (IMongoFieldRuntime mfr : mer) {
				IFieldRuntime fr = mfr.runtime();
				if (fr instanceof IReferenceRuntime) {
					BasicDBObject bdbo = new BasicDBObject();
					bdbo.put(IMongoConstants.FIELD_FIELDS + "." + mfr.field(),
							1);
					bdbo.put(FIELD_RUNTIME_STATUS.field(), 1);
					c.ensureIndex(bdbo);
				}
			}
		}

	}

}