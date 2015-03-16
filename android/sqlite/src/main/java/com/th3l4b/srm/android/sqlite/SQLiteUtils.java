package com.th3l4b.srm.android.sqlite;

import com.th3l4b.srm.codegen.java.runtime.DefaultIdentifierFieldRuntime;
import com.th3l4b.srm.codegen.java.runtime.DefaultStatusFieldRuntime;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class SQLiteUtils {

	public static final SQLiteNames NAMES = new SQLiteNames();

	public static ISQLiteFieldRuntime FIELD_RUNTIME_ID;
	public static ISQLiteFieldRuntime FIELD_RUNTIME_STATUS;

	static {
		try {
			DefaultIdentifierFieldRuntime idr = new DefaultIdentifierFieldRuntime(
					ISQLiteConstants.FIELD_ID);
			idr.getProperties().put(SQLiteNames.PROPERTY_IDENTIFIER,
					ISQLiteConstants.FIELD_ID);
			FIELD_RUNTIME_ID = new DefaultSQLiteFieldRuntime(idr, 0);
			DefaultStatusFieldRuntime statusr = new DefaultStatusFieldRuntime(
					ISQLiteConstants.FIELD_STATUS);
			statusr.getProperties().put(SQLiteNames.PROPERTY_IDENTIFIER,
					ISQLiteConstants.FIELD_STATUS);
			FIELD_RUNTIME_STATUS = new DefaultSQLiteFieldRuntime(statusr, 1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static ISQLiteModelRuntime create(IModelRuntime model)
			throws Exception {
		DefaultSQLiteModelRuntime mmr = new DefaultSQLiteModelRuntime(model);
		for (IEntityRuntime er : model) {
			DefaultSQLiteEntityRuntime mer = new DefaultSQLiteEntityRuntime(er);
			int i = 2; // Make room for id and status columns
			for (IFieldRuntime fr : er) {
				DefaultSQLiteFieldRuntime mfr = new DefaultSQLiteFieldRuntime(
						fr, i++);
				mer.add(mfr);
			}
			mmr.add(mer);
		}

		return mmr;
	}

	public static String[] createSQL(IModelRuntime model) throws Exception {
		String[] r = new String[model.size()];
		StringBuilder sb = new StringBuilder();
		SQLiteNames names = new SQLiteNames();
		int i = 0;
		for (IEntityRuntime er : model) {
			sb.setLength(0);
			sb.append("CREATE TABLE ");
			sb.append(names.name(er));
			sb.append(" ( ");
			sb.append(ISQLiteConstants.FIELD_ID);
			sb.append(" TEXT NOT NULL PRIMARY KEY, ");
			sb.append(ISQLiteConstants.FIELD_STATUS);
			sb.append(" TEXT NOT NULL");
			for (IFieldRuntime fr : er) {
				sb.append(", ");
				sb.append(ISQLiteConstants.PREFIX_FIELDS);
				sb.append(names.name(fr));
				sb.append(" TEXT");
			}
			sb.append(" )");
			r[i++] = sb.toString();
		}
		return r;
	}

}