package com.th3l4b.srm.sample.mongo.junit;

import java.net.ServerSocket;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.th3l4b.srm.mongo.MongoRuntime;
import com.th3l4b.srm.mongo.MongoUtils;
import com.th3l4b.srm.sample.base.AbstractModelTest;
import com.th3l4b.srm.sample.base.generated.NoPersistenceSampleRuntime;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class MongoTest extends AbstractModelTest {

	static int _port;
	static MongodExecutable _mongodExecutable;
	static MongoClient _mongoClient;
	static DB _db;
	static MongoRuntime _mongoRuntime;

	@BeforeClass
	public static void before() throws Exception {
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
		_db = _mongoClient.getDB(UUID.randomUUID().toString());

		// Apply indexes
		_mongoRuntime = new MongoRuntime(new NoPersistenceSampleRuntime()) {
			@Override
			protected DB getDB() throws Exception {
				return _db;
			}
		};
		MongoUtils.ensureIndexes(_mongoRuntime.getMongoModel(), _db);

	}

	@AfterClass
	public static void after() throws Exception {
		_mongodExecutable.stop();
	}

	@Override
	protected SampleModelUtils createModelUtils() throws Exception {
		return new SampleModelUtils(_mongoRuntime);
	}
}
