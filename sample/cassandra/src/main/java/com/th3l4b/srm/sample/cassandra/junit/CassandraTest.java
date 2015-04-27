package com.th3l4b.srm.sample.cassandra.junit;

import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.th3l4b.srm.cassandra.CassandraRuntime;
import com.th3l4b.srm.cassandra.CassandraUtils;
import com.th3l4b.srm.sample.base.AbstractModelTest;
import com.th3l4b.srm.sample.base.generated.NoPersistenceSampleRuntime;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;

public class CassandraTest extends AbstractModelTest {

	@Override
	protected SampleModelUtils createModelUtils() throws Exception {
		Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1")
				.build();
		Session s1 = cluster.connect();
		// http://stackoverflow.com/questions/16940595/how-to-create-keyspace-in-cassandra-using-java-class
		String name = "KS" + UUID.randomUUID().toString().replaceAll("-", "");
		s1.execute("create keyspace "
				+ name
				+ " WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3}");
		s1.close();
		final Session session = cluster.connect(name);

		// Create tables
		NoPersistenceSampleRuntime sr = new NoPersistenceSampleRuntime();
		for (String statement : CassandraUtils.createSQL(sr)) {
			session.execute(statement);
		}
		CassandraRuntime runtime = new CassandraRuntime(sr) {
			@Override
			protected Session getSession() throws Exception {
				return session;
			}
		};

		return new SampleModelUtils(runtime);
	}

	public static void main (String[] args) throws Exception {
		new CassandraTest().testAll();
		System.out.println("Finished");
		
		// Finish all threads and quit JVM
		System.exit(0);
	}
}
