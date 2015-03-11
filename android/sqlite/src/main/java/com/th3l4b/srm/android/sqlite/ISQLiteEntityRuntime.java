package com.th3l4b.srm.android.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IInstance;

public interface ISQLiteEntityRuntime extends INamed, IContainer<ISQLiteFieldRuntime> {
	String table() throws Exception;
	void apply(Cursor cursor, IInstance instance) throws Exception;
	void apply(IInstance instance, ContentValues values) throws Exception;
	IEntityRuntime runtime () throws Exception;
}
