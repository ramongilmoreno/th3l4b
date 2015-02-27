package com.th3l4b.srm.mongo;

import com.mongodb.DBObject;
import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public interface IMongoEntityRuntime extends INamed, IContainer<IMongoFieldRuntime> {
	String collection() throws Exception;
	void apply(DBObject o, IInstance instance) throws Exception;
	void apply(IInstance instance, DBObject o) throws Exception;
	IEntityRuntime runtime () throws Exception;
}
