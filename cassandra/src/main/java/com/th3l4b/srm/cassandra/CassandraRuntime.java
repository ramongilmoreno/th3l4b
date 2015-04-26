package com.th3l4b.srm.cassandra;

import com.datastax.driver.core.Session;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;
import com.th3l4b.srm.model.runtime.RuntimeFilter;

public abstract class CassandraRuntime extends RuntimeFilter {

	public CassandraRuntime() {
	}

	public CassandraRuntime(IRuntime delegated) {
		super(delegated);
	}
	
	protected abstract Session getSession() throws Exception;

	ICassandraModelRuntime _sqliteRuntime;
	private IFinder _finder;
	private IUpdater _updater;

	protected ICassandraModelRuntime cassandraModel() throws Exception {
		if (_sqliteRuntime == null) {
			_sqliteRuntime = createSqliteModel();
		}
		return _sqliteRuntime;
	}

	private ICassandraModelRuntime createSqliteModel() throws Exception {
		return CassandraUtils.create(model());
	}

	@Override
	public IFinder finder() throws Exception {
		if (_finder == null) {
			_finder = createFinder();
		}
		return _finder;
	}

	protected IFinder createFinder() throws Exception {
		return new AbstractCassandraFinder() {

			@Override
			protected IReverse reverse() throws Exception {
				return CassandraRuntime.this.reverse();
			}

			@Override
			protected Session getSession() throws Exception {
				return CassandraRuntime.this.getSession();
			}

			@Override
			protected ICassandraModelRuntime cassandraModel() throws Exception {
				return CassandraRuntime.this.cassandraModel();
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
		return new AbstractCassandraUpdater() {
			@Override
			protected Session getSession() throws Exception {
				return CassandraRuntime.this.getSession();
			}

			@Override
			protected ICassandraModelRuntime cassandraModel() throws Exception {
				return CassandraRuntime.this.cassandraModel();
			}

			@Override
			protected IFinder finder() throws Exception {
				return CassandraRuntime.this.finder();
			}

			@Override
			protected IModelRuntime model() throws Exception {
				return CassandraRuntime.this.model();
			}

		};
	}
}
