package com.th3l4b.srm.mongo;

public interface IMongoConstants {
	
	// Not to be confused with "_id" as this is not the ObjectId key. It is the
	// IIdentifier key.
	public static final String FIELD_ID = "id";
	
	public static final String FIELD_STATUS = "status";

	public static final String FIELD_FIELDS = "fields";

	public static final String PREFIX_TABLES = "t_";

	public static final String PREFIX_FIELDS = "f_";
}
