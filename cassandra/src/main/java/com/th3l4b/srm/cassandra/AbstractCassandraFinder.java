package com.th3l4b.srm.cassandra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IReverseRelationship;

public abstract class AbstractCassandraFinder implements IFinder {

	protected abstract Session getSession() throws Exception;

	protected abstract ICassandraModelRuntime cassandraModel() throws Exception;

	protected abstract IReverse reverse() throws Exception;

	private Map<String, String[]> _columns = new HashMap<String, String[]>();

	protected String[] columns(ICassandraEntityRuntime mer) throws Exception {
		String n = mer.getName();
		if (_columns.containsKey(n)) {
			return _columns.get(n);
		} else {
			// Compose list of columns
			String[] r = new String[mer.size() + 2];
			int i = 0;
			r[i++] = ICassandraConstants.FIELD_ID;
			r[i++] = ICassandraConstants.FIELD_STATUS;
			for (ICassandraFieldRuntime fr : mer) {
				r[i++] = fr.column();
			}
			_columns.put(n, r);
			return r;
		}
	}

	@Override
	public Collection<IInstance> all(String type) throws Exception {
		ICassandraEntityRuntime cer = cassandraModel().get(type);
		Where where = QueryBuilder
				.select()
				.all()
				.from(cer.table())
				.where(QueryBuilder.eq(ICassandraConstants.FIELD_STATUS,
						EntityStatus.Saved.toString()));
		ResultSet rs = getSession().execute(where);
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		for (Row row : rs) {
			IInstance instance = cer.runtime().create();
			cer.apply(row, instance);
			r.add(instance);
		}
		return r;
	}

	@Override
	public IInstance find(IIdentifier id) throws Exception {
		ICassandraEntityRuntime cer = cassandraModel().get(id.getType());
		Where where = QueryBuilder
				.select()
				.all()
				.from(cer.table())
				.where(QueryBuilder.eq(
						CassandraUtils.FIELD_RUNTIME_ID.column(), id.getKey()));
		ResultSet rs = getSession().execute(where);
		IInstance unknown = cer.runtime().create();
		ICoordinates coordinates = unknown.coordinates();
		coordinates.getIdentifier().setKey(id.getKey());
		coordinates.getIdentifier().setType(id.getType());
		coordinates.setStatus(EntityStatus.Unknown);
		for (Row row : rs) {
			IInstance instance = cer.runtime().create();
			cer.apply(row, instance);
			return instance;
		}
		return unknown;
	}

	@Override
	public Collection<IInstance> references(IIdentifier id, String relationship)
			throws Exception {
		IReverseRelationship rr = reverse().get(id.getType()).get(relationship);
		ICassandraEntityRuntime cer = cassandraModel().get(rr.getSourceType());
		Where where = QueryBuilder.select().from(cer.table())
				.allowFiltering().where();
		where = where.and(QueryBuilder.eq(
				CassandraUtils.FIELD_RUNTIME_STATUS.column(),
				EntityStatus.Saved.toString()));
		where = where.and(QueryBuilder.eq(cer.get(rr.getField()).column(),
				id.getKey()));
		ResultSet rs = getSession().execute(where);
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		for (Row row : rs) {
			IInstance instance = cer.runtime().create();
			cer.apply(row, instance);
			r.add(instance);
		}
		return r;
	}
}
