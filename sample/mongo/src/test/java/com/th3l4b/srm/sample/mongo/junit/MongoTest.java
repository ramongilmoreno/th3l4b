package com.th3l4b.srm.sample.mongo.junit;

import java.net.ServerSocket;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IUpdater;
import com.th3l4b.srm.mongo.MongoRuntime;
import com.th3l4b.srm.mongo.MongoUtils;
import com.th3l4b.srm.sample.base.AbstractModelTest;
import com.th3l4b.srm.sample.base.generated.AbstractSampleRuntime;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class MongoTest extends AbstractModelTest {

	private int _port;
	private MongodExecutable _mongodExecutable;
	private MongoClient _mongoClient;

	@Before
	public void before() throws Exception {
		// http://blog.yohanliyanage.com/2012/11/integration-testing-mongodb-spring-data/

		// Find a free port
		ServerSocket ss = new ServerSocket(0);
		_port = ss.getLocalPort();
		ss.close();

		// https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo
		MongodStarter starter = MongodStarter.getDefaultInstance();
		IMongodConfig mongodConfig = new MongodConfigBuilder()
				.version(Version.Main.PRODUCTION)
				.net(new Net(_port, Network.localhostIsIPv6())).build();
		_mongodExecutable = starter.prepare(mongodConfig);
		_mongodExecutable.start();
		_mongoClient = new MongoClient("localhost", _port);

	}

	@After
	public void after() throws Exception {
		_mongodExecutable.stop();
	}

	@Override
	protected SampleModelUtils createModelUtils() throws Exception {
		final DB db = _mongoClient.getDB(UUID.randomUUID().toString());
		AbstractSampleRuntime asr = new AbstractSampleRuntime() {
			@Override
			protected IFinder createFinder() throws Exception {
				throw new UnsupportedOperationException();
			}

			@Override
			protected IUpdater createUpdater() throws Exception {
				throw new UnsupportedOperationException();
			}
		};
		MongoRuntime mongoRuntime = new MongoRuntime(asr) {
			@Override
			protected DB getDB() throws Exception {
				return db;
			}
		};
		
		// Apply indexes
		MongoUtils.ensureIndexes(mongoRuntime.getMongoModel(), db);
		
		// Return result
		return new SampleModelUtils(mongoRuntime);
	}
}
