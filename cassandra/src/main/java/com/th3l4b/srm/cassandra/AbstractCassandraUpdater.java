package com.th3l4b.srm.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
import com.datastax.driver.core.querybuilder.Update.Assignments;
import com.datastax.driver.core.querybuilder.Update.Where;
import com.th3l4b.srm.codegen.java.runtime.AbstractUpdater;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractCassandraUpdater extends AbstractUpdater
		implements ICassandraConstants {

	protected abstract Session getSession() throws Exception;

	protected abstract ICassandraModelRuntime cassandraModel() throws Exception;

	@Override
	protected void insert(IInstance instance) throws Exception {
		ICassandraEntityRuntime cer = cassandraModel().get(
				instance.coordinates().getIdentifier().getType());
		Insert insert = QueryBuilder.insertInto(cer.table());
		insert = cer.apply(instance, insert);
		getSession().execute(insert);
	}

	@Override
	protected void update(IInstance newEntity, IInstance oldEntity)
			throws Exception {
		ICassandraEntityRuntime cer = cassandraModel().get(
				newEntity.coordinates().getIdentifier().getType());

		Update update = QueryBuilder.update(cer.table());
		Assignments assignments = update.with();
		assignments = cer.apply(newEntity,
				newEntity.coordinates().getStatus() != oldEntity.coordinates()
						.getStatus(), assignments);
		Where where = assignments.where(QueryBuilder.eq(
				ICassandraConstants.FIELD_ID, CassandraUtils.FIELD_RUNTIME_ID
						.runtime().get(newEntity)));
		getSession().execute(where);
	}
}
