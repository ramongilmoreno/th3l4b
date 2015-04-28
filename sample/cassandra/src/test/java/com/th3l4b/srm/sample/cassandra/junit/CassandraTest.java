package com.th3l4b.srm.sample.cassandra.junit;

import java.util.UUID;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import com.th3l4b.srm.cassandra.CassandraRuntime;
import com.th3l4b.srm.cassandra.CassandraUtils;
import com.th3l4b.srm.sample.base.AbstractModelTest;
import com.th3l4b.srm.sample.base.generated.NoPersistenceSampleRuntime;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;

/**
 * This test cannot be run in Eclipse, probably due to inconsistent composition
 * of classpath (Gradle headless build+unittesting does it properly). It can,
 * however, be started as a regular main application targeting a Docker-based
 * Cassandra instance (see docker-cassandra-start/stop.sh scripts).
 */
public class CassandraTest extends AbstractModelTest {

	static String _host = "127.0.0.1";

	/**
	 * Set to -1 to use the default port (no withPort() call to the builder).
	 */
	static int _port = 9142;

	static Session _session;

	@BeforeClass
	public static void startEmbeddedServer() throws Exception {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
		start();
	}

	@AfterClass
	public static void stopEmbeddedServer() throws Exception {
		stop();
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}

	public static void start() throws Exception {
		Builder builder = Cluster.builder().addContactPoint(_host);
		if (_port != -1) {
			builder = builder.withPort(_port);
		}
		Cluster cluster = builder.build();
		Session s1 = cluster.connect();
		// http://stackoverflow.com/questions/16940595/how-to-create-keyspace-in-cassandra-using-java-class
		String name = "KS" + UUID.randomUUID().toString().replaceAll("-", "");
		s1.execute("create keyspace "
				+ name
				+ " WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3}");
		s1.close();
		_session = cluster.connect(name);

		// Create tables and indexes
		for (String statement : CassandraUtils
				.createSQL(SampleModelUtils.RUNTIME)) {
			_session.execute(statement);
		}

	}

	public static void stop() throws Exception {
		_session.close();
	}

	@Override
	protected SampleModelUtils createModelUtils() throws Exception {
		CassandraRuntime runtime = new CassandraRuntime(
				new NoPersistenceSampleRuntime()) {
			@Override
			protected Session getSession() throws Exception {
				return _session;
			}
		};

		return new SampleModelUtils(runtime);
	}

	public static void main(String[] args) throws Exception {
		// Allow default port when launched as main function
		_port = -1;
		start();
		new CassandraTest().testAll();
		stop();
		System.out.println("Finished");
		// Finish all threads and quit JVM
		System.exit(0);
	}
}
