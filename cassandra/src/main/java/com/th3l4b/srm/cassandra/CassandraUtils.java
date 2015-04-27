package com.th3l4b.srm.cassandra;

import java.util.ArrayList;

import com.th3l4b.srm.codegen.java.runtime.DefaultIdentifierFieldRuntime;
import com.th3l4b.srm.codegen.java.runtime.DefaultStatusFieldRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IReferenceRuntime;
import com.th3l4b.srm.model.runtime.IRuntime;

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

	public static String[] createSQL(IRuntime runtime) throws Exception {
		ArrayList<String> r = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		ICassandraModelRuntime cmr = create(runtime.model());
		for (ICassandraEntityRuntime cer : cmr) {
			sb.setLength(0);
			sb.append("CREATE TABLE ");
			sb.append(cer.table());
			sb.append(" ( ");
			sb.append(CassandraUtils.FIELD_RUNTIME_ID.column());
			sb.append(" TEXT PRIMARY KEY, ");
			sb.append(CassandraUtils.FIELD_RUNTIME_STATUS.column());
			sb.append(" TEXT ");
			for (ICassandraFieldRuntime cfr : cer) {
				sb.append(", ");
				sb.append(cfr.column());
				sb.append(" TEXT");
			}
			sb.append(" )");
			r.add(sb.toString());

			sb.setLength(0);
			sb.append("CREATE INDEX ON ");
			sb.append(cer.table());
			sb.append(" ( ");
			sb.append(CassandraUtils.FIELD_RUNTIME_STATUS.column());
			sb.append(" )");
			r.add(sb.toString());

			// Create index on references
			for (ICassandraFieldRuntime cfr : cer) {
				IFieldRuntime fr = cfr.runtime();
				if (fr instanceof IReferenceRuntime) {
					sb.setLength(0);
					sb.append("CREATE INDEX ON ");
					sb.append(cer.table());
					sb.append(" ( ");
					sb.append(cfr.column());
					sb.append(" )");
					r.add(sb.toString());
				}
			}
		}

		return r.toArray(new String[r.size()]);
	}

}