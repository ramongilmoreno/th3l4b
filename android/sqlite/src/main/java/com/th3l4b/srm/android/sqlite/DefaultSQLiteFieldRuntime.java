package com.th3l4b.srm.android.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

import java.util.Map;

public class DefaultSQLiteFieldRuntime extends DefaultNamed implements
		ISQLiteFieldRuntime {

	private IFieldRuntime _runtime;
	private int _columnIndex;
	private String _columnName;

	public DefaultSQLiteFieldRuntime(IFieldRuntime runtime, int columnIndex)
			throws Exception {
		setName(runtime.getName());
		_runtime = runtime;
        Map<String, String> properties = runtime.getProperties();
        if (properties.containsKey(SQLiteNames.PROPERTY_IDENTIFIER)) {
            _columnName = properties.get(SQLiteNames.PROPERTY_IDENTIFIER);
        } else {
            _columnName = ISQLiteConstants.PREFIX_FIELDS + SQLiteUtils.NAMES.name(runtime);
        }
		_columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return _columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		_columnIndex = columnIndex;
	}

	@Override
	public int columnIndex() throws Exception {
		return _columnIndex;
	}

	public String getColumnName() {
		return _columnName;
	}

	public void setColumnName(String columnName) {
		_columnName = columnName;
	}

	@Override
	public String columnName() throws Exception {
		return _columnName;
	}

	@Override
	public IFieldRuntime runtime() throws Exception {
		return _runtime;
	}

	@Override
	public void apply(Cursor cursor, IInstance instance) throws Exception {
		int i = columnIndex();
		if (!cursor.isNull(i)) {
			String v = cursor.getString(i);
			runtime().set(v, instance);
		}
	}

	@Override
	public void apply(IInstance instance, ContentValues values)
			throws Exception {
		IFieldRuntime r = runtime();
		if (r.isSet(instance)) {
			String v = r.get(instance);
			values.put(columnName(), v);
		}
	}

}
