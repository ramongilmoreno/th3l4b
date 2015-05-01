package com.th3l4b.srm.android.sqlite;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.th3l4b.common.data.named.DefaultNamedContainer;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public class DefaultSQLiteEntityRuntime extends
		DefaultNamedContainer<ISQLiteFieldRuntime> implements
		ISQLiteEntityRuntime {

	private IEntityRuntime _runtime;
	private String _table;

	public DefaultSQLiteEntityRuntime(IEntityRuntime runtime) throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
		Map<String, String> properties = runtime.getProperties();
		if (properties.containsKey(SQLiteNames.PROPERTY_IDENTIFIER)) {
			_table = properties.get(SQLiteNames.PROPERTY_IDENTIFIER);
		} else {
			_table = ISQLiteConstants.PREFIX_TABLES
					+ SQLiteUtils.NAMES.name(runtime);
		}
	}

	@Override
	public String table() throws Exception {
		return _table;
	}

	@Override
	public void apply(Cursor cursor, IInstance instance) throws Exception {
		SQLiteUtils.FIELD_RUNTIME_ID.apply(cursor, instance);
		SQLiteUtils.FIELD_RUNTIME_STATUS.apply(cursor, instance);
		for (ISQLiteFieldRuntime f : this) {
			f.apply(cursor, instance);
		}
	}

	@Override
	public void apply(IInstance instance, ContentValues o) throws Exception {
		SQLiteUtils.FIELD_RUNTIME_ID.apply(instance, o);
		SQLiteUtils.FIELD_RUNTIME_STATUS.apply(instance, o);
		for (ISQLiteFieldRuntime f : this) {
			f.apply(instance, o);
		}
	}

	@Override
	public IEntityRuntime runtime() throws Exception {
		return _runtime;
	}
}
