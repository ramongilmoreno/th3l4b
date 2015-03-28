package com.th3l4b.srm.mongo;

import com.mongodb.DB;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IReverse;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;
import com.th3l4b.srm.model.runtime.RuntimeFilter;

public abstract class MongoRuntime extends RuntimeFilter {

	public MongoRuntime() {
	}

	public MongoRuntime(IRuntime delegated) {
		super(delegated);
	}

	protected abstract DB getDB() throws Exception;

	IMongoModelRuntime _mongoRuntime;
	private IFinder _finder;
	private IUpdater _updater;

	public IMongoModelRuntime getMongoModel() throws Exception {
		if (_mongoRuntime == null) {
			_mongoRuntime = createMongoModel();
		}
		return _mongoRuntime;
	}

	private IMongoModelRuntime createMongoModel() throws Exception {
		return MongoUtils.create(model());
	}

	@Override
	public IFinder finder() throws Exception {
		if (_finder == null) {
			_finder = createFinder();
		}
		return _finder;
	}

	protected IFinder createFinder() throws Exception {
		return new AbstractMongoFinder() {

			@Override
			protected IReverse reverse() throws Exception {
				return MongoRuntime.this.reverse();
			}

			@Override
			protected IMongoModelRuntime mongoModel() throws Exception {
				return MongoRuntime.this.getMongoModel();
			}

			@Override
			protected DB getDB() throws Exception {
				return MongoRuntime.this.getDB();
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
		return new AbstractMongoUpdater() {

			@Override
			protected IMongoModelRuntime mongoModel() throws Exception {
				return MongoRuntime.this.getMongoModel();
			}

			@Override
			protected DB getDB() throws Exception {
				return MongoRuntime.this.getDB();
			}

			@Override
			protected IFinder finder() throws Exception {
				return MongoRuntime.this.finder();
			}

			@Override
			protected IModelRuntime model() throws Exception {
				return MongoRuntime.this.model();
			}

		};
	}
}
