package com.th3l4b.srm.android.sqlite;

import com.th3l4b.srm.model.runtime.IModelRuntime;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractSQLiteOpenHelper extends SQLiteOpenHelper {

	public AbstractSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public AbstractSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	protected abstract IModelRuntime runtime() throws Exception;

	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			String[] statements = SQLiteUtils.createSQL(runtime());
			for (String s : statements) {
				database.execSQL(s);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// Does nothing
	}
}
