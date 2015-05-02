package com.th3l4b.srm.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update.Assignments;
import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public class DefaultCassandraFieldRuntime extends DefaultNamed implements
		ICassandraFieldRuntime {

	private IFieldRuntime _runtime;
	private String _columnName;

	public DefaultCassandraFieldRuntime(IFieldRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
		_columnName = CassandraUtils.NAMES.name(runtime,
				ICassandraConstants.PREFIX_FIELDS);
	}

	@Override
	public String column() throws Exception {
		return _columnName;
	}

	public String getColumnName() {
		return _columnName;
	}

	public void setColumnName(String columnName) {
		_columnName = columnName;
	}

	@Override
	public IFieldRuntime runtime() throws Exception {
		return _runtime;
	}

	@Override
	public void apply(Row row, IInstance instance) throws Exception {
		String name = column();
		if (!row.isNull(name)) {
			String v = row.getString(name);
			runtime().set(v, instance);
		}
	}

	@Override
	public void apply(IInstance instance, BoundStatement values)
			throws Exception {
		IFieldRuntime r = runtime();
		if (r.isSet(instance)) {
			String v = r.get(instance);
			values.setString(column(), v);
		}
	}

	@Override
	public Insert apply(IInstance instance, Insert insert) throws Exception {
		return insert.value(column(), runtime().get(instance));
	}

	@Override
	public Assignments apply(IInstance instance, Assignments assignments)
			throws Exception {
		return assignments.and(QueryBuilder.set(column(),
				runtime().get(instance)));

	}

}
