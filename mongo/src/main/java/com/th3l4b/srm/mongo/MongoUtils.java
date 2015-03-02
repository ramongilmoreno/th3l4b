package com.th3l4b.srm.mongo;

import com.th3l4b.srm.codegen.java.runtime.DefaultIdentifierFieldRuntime;
import com.th3l4b.srm.codegen.java.runtime.DefaultStatusFieldRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class MongoUtils {

	public static final MongoNames NAMES = new MongoNames();

	public static IMongoFieldRuntime FIELD_RUNTIME_ID;
	public static IMongoFieldRuntime FIELD_RUNTIME_STATUS;

	static {
		try {
			DefaultIdentifierFieldRuntime idr = new DefaultIdentifierFieldRuntime(
					IMongoConstants.FIELD_ID);
			idr.getProperties().put(MongoNames.PROPERTY_IDENTIFIER,
					IMongoConstants.FIELD_ID);
			FIELD_RUNTIME_ID = new DefaultMongoFieldRuntime(idr);
			DefaultStatusFieldRuntime statusr = new DefaultStatusFieldRuntime(
					IMongoConstants.FIELD_STATUS);
			statusr.getProperties().put(MongoNames.PROPERTY_IDENTIFIER,
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

}