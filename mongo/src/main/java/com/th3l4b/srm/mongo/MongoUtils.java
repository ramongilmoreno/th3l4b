package com.th3l4b.srm.mongo;

import com.th3l4b.srm.codegen.java.runtime.DefaultIdentifierFieldRuntime;
import com.th3l4b.srm.codegen.java.runtime.DefaultStatusFieldRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IRuntime;

public class MongoUtils {

	public static final MongoNames NAMES = new MongoNames();

	public static IMongoFieldRuntime FIELD_RUNTIME_ID;
	public static IMongoFieldRuntime FIELD_RUNTIME_STATUS;

	static {
		try {
			FIELD_RUNTIME_ID = new DefaultMongoFieldRuntime(
					new DefaultIdentifierFieldRuntime(IMongoConstants.FIELD_ID));
			FIELD_RUNTIME_STATUS = new DefaultMongoFieldRuntime(
					new DefaultStatusFieldRuntime(IMongoConstants.FIELD_STATUS));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static IMongoModelRuntime create(IRuntime runtime)
			throws Exception {
		DefaultMongoModelRuntime mmr = new DefaultMongoModelRuntime(runtime);
		for (IEntityRuntime er : runtime.entities()) {
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