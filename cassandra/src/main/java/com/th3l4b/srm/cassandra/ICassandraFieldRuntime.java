package com.th3l4b.srm.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Update.Assignments;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public interface ICassandraFieldRuntime extends INamed {
	String column() throws Exception;
	IFieldRuntime runtime () throws Exception;
	void apply(Row cursor, IInstance instance) throws Exception;
	void apply(IInstance instance, BoundStatement values) throws Exception;
	Insert apply(IInstance instance, Insert insert) throws Exception;
	Assignments apply(IInstance instance, Assignments update) throws Exception;
}
