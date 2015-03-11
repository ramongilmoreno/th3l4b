package com.th3l4b.srm.android.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public interface ISQLiteFieldRuntime extends INamed {
	int columnIndex() throws Exception;
	String columnName() throws Exception;
	IFieldRuntime runtime () throws Exception;
	void apply(Cursor cursor, IInstance instance) throws Exception;
	void apply(IInstance instance, ContentValues values) throws Exception;
}
