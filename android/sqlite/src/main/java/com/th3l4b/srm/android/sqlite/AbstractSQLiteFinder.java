package com.th3l4b.srm.android.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IReverseRelationship;

public abstract class AbstractSQLiteFinder implements IFinder {

	protected abstract SQLiteDatabase getDatabase() throws Exception;

	protected abstract ISQLiteModelRuntime sqliteModel() throws Exception;

	protected abstract IReverse reverse() throws Exception;

	private Map<String, String[]> _columns = new HashMap<String, String[]>();

	protected String[] columns(ISQLiteEntityRuntime mer) throws Exception {
		String n = mer.getName();
		if (_columns.containsKey(n)) {
			return _columns.get(n);
		} else {
			// Compose list of columns
			String[] r = new String[mer.size() + 2];
            int i = 0;
            r[i++] = ISQLiteConstants.FIELD_ID;
            r[i++] = ISQLiteConstants.FIELD_STATUS;
			for (ISQLiteFieldRuntime fr : mer) {
				r[i++] = fr.columnName();
			}
			_columns.put(n, r);
			return r;
		}
	}

	@Override
	public Collection<IInstance> all(String type) throws Exception {
		ISQLiteEntityRuntime mer = sqliteModel().get(type);
		IEntityRuntime er = mer.runtime();
		IInstance instance = er.create();
		instance.coordinates().setStatus(EntityStatus.Saved);
		Cursor cursor = getDatabase().query(
				mer.table(),
				columns(mer),
                ISQLiteConstants.FIELD_STATUS + " = ?",
                new String[] {
                        SQLiteUtils.FIELD_RUNTIME_STATUS.runtime()
                                .get(instance) }, null, null, null);
		cursor.moveToFirst();
		try {
			ArrayList<IInstance> r = new ArrayList<IInstance>();
			while (!cursor.isAfterLast()) {
				IInstance i = er.create();
				mer.apply(cursor, instance);
				r.add(i);
                cursor.moveToNext();
			}
			return r;
		} finally {
			cursor.close();
		}
	}

	@Override
	public IInstance find(IIdentifier id) throws Exception {
		ISQLiteEntityRuntime mer = sqliteModel().get(id.getType());
		IEntityRuntime er = mer.runtime();
		IInstance instance = er.create();
		ICoordinates coordinates = instance.coordinates();
		coordinates.setIdentifier(id);
		Cursor cursor = getDatabase().query(
				mer.table(),
				columns(mer),
				ISQLiteConstants.FIELD_ID + " = ?",
				new String[] { SQLiteUtils.FIELD_RUNTIME_ID.runtime().get(instance) },
                null, null, null);
		cursor.moveToFirst();
		try {
			while (!cursor.isAfterLast()) {
				mer.apply(cursor, instance);
				return instance;
			}
		} finally {
			cursor.close();
		}

		// None found
		coordinates.setStatus(EntityStatus.Unknown);
		return instance;
	}

	@Override
	public Collection<IInstance> references(IIdentifier id, String relationship)
			throws Exception {
		IReverseRelationship rr = reverse().get(id.getType()).get(relationship);
		ISQLiteEntityRuntime mer = sqliteModel().get(rr.getSourceType());
		IEntityRuntime er = mer.runtime();
		IInstance instance = er.create();
		instance.coordinates().setStatus(EntityStatus.Saved);
		String columnName = mer.get(rr.getField()).columnName();
		Cursor cursor = getDatabase().query(
				mer.table(),
				columns(mer),
				columnName + " = ? and " + ISQLiteConstants.FIELD_STATUS
						+ " = ?",
				new String[] {
                        // Get id key instead of using runtime, as this id is not of the right type.
						id.getKey(),
						SQLiteUtils.FIELD_RUNTIME_STATUS.runtime()
								.get(instance) }, null, null, null);
		cursor.moveToFirst();
		try {
			ArrayList<IInstance> r = new ArrayList<IInstance>();
			while (!cursor.isAfterLast()) {
				IInstance i = er.create();
				mer.apply(cursor, i);
				r.add(i);
                cursor.moveToNext();
            }
			return r;
		} finally {
			cursor.close();
		}
	}
}
