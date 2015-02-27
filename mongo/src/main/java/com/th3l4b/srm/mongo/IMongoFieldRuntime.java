package com.th3l4b.srm.mongo;

import com.mongodb.DBObject;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public interface IMongoFieldRuntime extends INamed {
	String field () throws Exception;
	IFieldRuntime runtime () throws Exception;
	void apply(DBObject o, IInstance instance) throws Exception;
	void apply(IInstance instance, DBObject o) throws Exception;
}