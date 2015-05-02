package com.th3l4b.srm.cassandra;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Update.Assignments;
import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public class DefaultCassandraEntityRuntime extends
		DefaultNamedContainer<ICassandraFieldRuntime> implements
		ICassandraEntityRuntime {

	private IEntityRuntime _runtime;
	private String _table;

	public DefaultCassandraEntityRuntime(IEntityRuntime runtime)
			throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
		_table = CassandraUtils.NAMES.name(runtime,
				ICassandraConstants.PREFIX_TABLES);
	}

	@Override
	public String table() throws Exception {
		return _table;
	}

	@Override
	public void apply(Row row, IInstance instance) throws Exception {
		CassandraUtils.FIELD_RUNTIME_ID.apply(row, instance);
		CassandraUtils.FIELD_RUNTIME_STATUS.apply(row, instance);
		for (ICassandraFieldRuntime f : this) {
			f.apply(row, instance);
		}
	}

	@Override
	public Insert apply(IInstance instance, Insert insert) throws Exception {
		insert = CassandraUtils.FIELD_RUNTIME_ID.apply(instance, insert);
		insert = CassandraUtils.FIELD_RUNTIME_STATUS.apply(instance, insert);
		for (ICassandraFieldRuntime f : this) {
			if (f.runtime().isSet(instance)) {
				insert = f.apply(instance, insert);
			}
		}
		return insert;
	}

	public Assignments apply(IInstance newEntity, boolean applyStatus,
			Assignments assignments) throws Exception {
		// Check if an update of the status is needed
		if (applyStatus) {
			assignments = CassandraUtils.FIELD_RUNTIME_STATUS.apply(newEntity,
					assignments);
		}

		// Set fields
		for (ICassandraFieldRuntime cfr : this) {
			IFieldRuntime fr = cfr.runtime();
			if (fr.isSet(newEntity)) {
				assignments = cfr.apply(newEntity, assignments);
			}
		}
		return assignments;
	}

	@Override
	public IEntityRuntime runtime() throws Exception {
		return _runtime;
	}
}
