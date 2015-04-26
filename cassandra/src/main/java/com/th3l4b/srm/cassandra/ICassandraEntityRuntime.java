package com.th3l4b.srm.cassandra;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Update.Assignments;
import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public interface ICassandraEntityRuntime extends INamed,
		IContainer<ICassandraFieldRuntime> {
	String table() throws Exception;

	void apply(Row row, IInstance instance) throws Exception;

	/**
	 * Applies all set fields, status and id to the insert
	 */
	Insert apply(IInstance instance, Insert insert) throws Exception;

	/**
	 * Applies all set fields, status if indicated and never the id.
	 */
	Assignments apply(IInstance instance, boolean applyStatus,
			Assignments assignments) throws Exception;

	IEntityRuntime runtime() throws Exception;
}
