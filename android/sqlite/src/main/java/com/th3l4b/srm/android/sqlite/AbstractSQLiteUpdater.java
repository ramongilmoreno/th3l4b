package com.th3l4b.srm.android.sqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.th3l4b.srm.codegen.java.runtime.AbstractUpdater;
import com.th3l4b.srm.model.runtime.IInstance;

public abstract class AbstractSQLiteUpdater extends AbstractUpdater {

	protected abstract SQLiteDatabase getDatabase() throws Exception;

	protected abstract ISQLiteModelRuntime sqliteModel() throws Exception;

	@Override
	protected void insert(IInstance entity) throws Exception {
		ISQLiteEntityRuntime mer = sqliteModel().get(
				entity.coordinates().getIdentifier().getType());
		ContentValues values = new ContentValues();
		mer.apply(entity, values);
		getDatabase().insert(mer.table(), null, values);
	}

	@Override
	protected void update(IInstance newEntity, IInstance oldEntity)
			throws Exception {
		ISQLiteEntityRuntime mer = sqliteModel().get(
				newEntity.coordinates().getIdentifier().getType());
		ContentValues values = new ContentValues();
		mer.apply(newEntity, values);
		getDatabase()
				.update(mer.table(),
						values,
						ISQLiteConstants.FIELD_ID + " = ?",
						new String[] { newEntity.coordinates().getIdentifier()
								.getKey() });
	}
}
