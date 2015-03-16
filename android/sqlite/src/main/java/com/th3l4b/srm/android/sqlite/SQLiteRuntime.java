package com.th3l4b.srm.android.sqlite;

import android.database.sqlite.SQLiteDatabase;

import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;
import com.th3l4b.srm.model.runtime.RuntimeFilter;

public abstract class SQLiteRuntime extends RuntimeFilter {

	public SQLiteRuntime() {
	}

	public SQLiteRuntime(IRuntime delegated) {
		super(delegated);
	}

	protected abstract SQLiteDatabase getDatabase() throws Exception;

	protected abstract ISQLiteModelRuntime sqliteModel() throws Exception;

	ISQLiteModelRuntime _mongoRuntime;
	private IFinder _finder;
	private IUpdater _updater;

	protected ISQLiteModelRuntime getSQLiteModel() throws Exception {
		if (_mongoRuntime == null) {
			_mongoRuntime = createSQLiteModel();
		}
		return _mongoRuntime;
	}

	private ISQLiteModelRuntime createSQLiteModel() throws Exception {
		return SQLiteUtils.create(model());
	}

	@Override
	public IFinder finder() throws Exception {
		if (_finder == null) {
			_finder = createFinder();
		}
		return _finder;
	}

	protected IFinder createFinder() throws Exception {
		return new AbstractSQLiteFinder() {

			@Override
			protected IReverse reverse() throws Exception {
				return SQLiteRuntime.this.reverse();
			}

			@Override
			protected SQLiteDatabase getDatabase() throws Exception {
				return SQLiteRuntime.this.getDatabase();
			}

			@Override
			protected ISQLiteModelRuntime sqliteModel() throws Exception {
				return SQLiteRuntime.this.sqliteModel();
			}
		};
	}

	@Override
	public IUpdater updater() throws Exception {
		if (_updater == null) {
			_updater = createUpdater();
		}
		return _updater;
	}

	protected IUpdater createUpdater() throws Exception {
		return new AbstractSQLiteUpdater() {
			@Override
			protected SQLiteDatabase getDatabase() throws Exception {
				return SQLiteRuntime.this.getDatabase();
			}

			@Override
			protected ISQLiteModelRuntime sqliteModel() throws Exception {
				return SQLiteRuntime.this.sqliteModel();
			}

			@Override
			protected IFinder finder() throws Exception {
				return SQLiteRuntime.this.finder();
			}

			@Override
			protected IModelRuntime model() throws Exception {
				return SQLiteRuntime.this.model();
			}

		};
	}
}
