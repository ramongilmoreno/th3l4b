package com.th3l4b.srm.cassandra;

import com.th3l4b.srm.codegen.java.runtime.DefaultIdentifierFieldRuntime;
import com.th3l4b.srm.codegen.java.runtime.DefaultStatusFieldRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class CassandraUtils implements ICassandraConstants {

	public static final CassandraNames NAMES = new CassandraNames();

	public static ICassandraFieldRuntime FIELD_RUNTIME_ID;
	public static ICassandraFieldRuntime FIELD_RUNTIME_STATUS;

	static {
		try {
			DefaultIdentifierFieldRuntime idr = new DefaultIdentifierFieldRuntime(
					FIELD_ID);
			idr.getProperties().put(CassandraNames.PROPERTY_IDENTIFIER,
					FIELD_ID);
			FIELD_RUNTIME_ID = new DefaultCassandraFieldRuntime(idr);
			DefaultStatusFieldRuntime statusr = new DefaultStatusFieldRuntime(
					FIELD_STATUS);
			statusr.getProperties().put(CassandraNames.PROPERTY_IDENTIFIER,
					FIELD_STATUS);
			FIELD_RUNTIME_STATUS = new DefaultCassandraFieldRuntime(statusr);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static ICassandraModelRuntime create(IModelRuntime model)
			throws Exception {
		DefaultCassandraModelRuntime mmr = new DefaultCassandraModelRuntime(
				model);
		for (IEntityRuntime er : model) {
			DefaultCassandraEntityRuntime mer = new DefaultCassandraEntityRuntime(
					er);
			for (IFieldRuntime fr : er) {
				DefaultCassandraFieldRuntime mfr = new DefaultCassandraFieldRuntime(
						fr);
				mer.add(mfr);
			}
			mmr.add(mer);
		}

		return mmr;
	}

	public static String[] createSQL(IModelRuntime model) throws Exception {
		String[] r = new String[model.size()];
		StringBuilder sb = new StringBuilder();
		CassandraNames names = new CassandraNames();
		int i = 0;
		for (IEntityRuntime er : model) {
			sb.setLength(0);
			sb.append("CREATE TABLE ");
			sb.append(names.name(er));
			sb.append(" ( ");
			sb.append(FIELD_ID);
			sb.append(" TEXT NOT NULL PRIMARY KEY, ");
			sb.append(FIELD_STATUS);
			sb.append(" TEXT NOT NULL");
			for (IFieldRuntime fr : er) {
				sb.append(", ");
				sb.append(PREFIX_FIELDS);
				sb.append(names.name(fr));
				sb.append(" TEXT");
			}
			sb.append(" )");
			r[i++] = sb.toString();
		}
		return r;
	}

}