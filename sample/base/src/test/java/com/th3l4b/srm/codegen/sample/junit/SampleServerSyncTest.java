package com.th3l4b.srm.codegen.sample.junit;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.entities.IEntity1;
import com.th3l4b.srm.sample.base.generated.inmemory.AbstractSampleInMemoryRuntime;
import com.th3l4b.srm.sync.client.ClientUpdateTracker;
import com.th3l4b.srm.sync.client.generated.inmemory.AbstractClientSyncInMemoryRuntime;
import com.th3l4b.srm.sync.server.SyncServer;
import com.th3l4b.srm.sync.server.SyncServer.SyncResult;
import com.th3l4b.srm.sync.server.generated.ServerSyncModelUtils;
import com.th3l4b.srm.sync.server.generated.entities.IMerge;
import com.th3l4b.srm.sync.server.generated.entities.IStatus;
import com.th3l4b.srm.sync.server.generated.inmemory.AbstractServerSyncInMemoryRuntime;
import com.th3l4b.srm.sync.server.graph.DirectedGraphFromPersistence;
import com.th3l4b.srm.sync.server.persistence.ISyncServerPersistence;
import com.th3l4b.srm.sync.server.persistence.SRMBasedSyncServerPersistence;

public class SampleServerSyncTest {

	class Client {

		public String _id = UUID.randomUUID().toString();
		public Map<IIdentifier, IInstance> _map;
		public AbstractSampleInMemoryRuntime _runtime;
		public Map<IIdentifier, IInstance> _mapForRepository;
		public IRuntime _repository;
		public ClientUpdateTracker _tracker;
		public SampleModelUtils _utils;

		public Client() throws Exception {
			_map = new LinkedHashMap<IIdentifier, IInstance>();
			_runtime = new AbstractSampleInMemoryRuntime() {
				@Override
				protected Map<IIdentifier, IInstance> getMap() throws Exception {
					return _map;
				}
			};
			_mapForRepository = new LinkedHashMap<IIdentifier, IInstance>();
			_repository = new AbstractClientSyncInMemoryRuntime() {
				@Override
				protected Map<IIdentifier, IInstance> getMap() throws Exception {
					return _mapForRepository;
				}
			};
			_tracker = new ClientUpdateTracker(_runtime, _repository);
			_utils = new SampleModelUtils(_tracker.getTracked());
		}

	}

	class Server {

		public Map<IIdentifier, IInstance> _map;
		public SyncServer _syncServer;
		public IRuntime _repository;
		public ServerSyncModelUtils _utils;
		public ISyncServerPersistence _persistence;

		public Server() throws Exception {
			_map = new LinkedHashMap<IIdentifier, IInstance>();
			_repository = new AbstractServerSyncInMemoryRuntime() {
				@Override
				protected Map<IIdentifier, IInstance> getMap() throws Exception {
					return _map;
				}
			};
			_utils = new ServerSyncModelUtils(_repository);
			_persistence = new SRMBasedSyncServerPersistence(_repository,
					SampleModelUtils.RUNTIME);
			_syncServer = new SyncServer(_persistence);
		}

		public void register(Client client) throws Exception {
			_persistence.createClient(client._id);
		}

		public void sync(Client client) throws Exception {
			SyncResult discovered = _syncServer.discover(client._id,
					client._tracker.pendingUpdates()._changes);
			client._tracker.sync(discovered._updates);
		}

	}

	private void log() {
		log("=========================================================");
	}

	private void log(String msg) {
		if (SyncServer.LOG) {
			System.out.println(msg);
		}
	}

	@Test
	public void test() throws Exception {
		Server server = new Server();
		Client client1 = new Client();
		Client client2 = new Client();
		Client client3 = new Client();

		// Create an entity
		int i = 0;
		registerAndCreateEntityAndSync(client1, "Hello", server, ++i);
		registerAndCreateEntityAndSync(client2, "World", server, ++i);
		registerAndCreateEntityAndSync(client3, "!", server, ++i);

		// Resync in reverse order: server 3 has nothing to do, server 2 a
		// little and server 1 needs to collect all changes
		resync(client3, server, i);
		resync(client2, server, i);
		resync(client1, server, i);

		// Resync again to check no status from the first phase are used again
		// (resyncing twice caused an NPE during tests)
		resync(client1, server, i);
		resync(client2, server, i);
		resync(client3, server, i);
	}

	private void registerAndCreateEntityAndSync(Client client, String text,
			Server server, int entity1Count) throws Exception {
		log("registerAndCreateEntityAndSync on server: " + client._id);
		server.register(client);
		IEntity1 e1 = client._utils.createEntity1();
		e1.setField11(text);
		client._utils.update(Collections.<IInstance> singleton(e1));
		server.sync(client);

		Assert.assertEquals(entity1Count, client._utils.finder().allEntity1()
				.size());
		log();
	}

	private void resync(Client client, Server server, int entity1Count)
			throws Exception {
		log("resync on server: " + client._id);
		server.sync(client);
		Assert.assertEquals(entity1Count, client._utils.finder().allEntity1()
				.size());
		log();
	}

	private void ensureStatusExistsDeleted(Server server, String status)
			throws Exception {
		IStatus found = server._utils.finder().findStatus(status);
		ICoordinates coordinates = found.coordinates();
		if (coordinates.getStatus() == EntityStatus.Unknown) {
			coordinates.setStatus(EntityStatus.ToDelete);
			coordinates.getIdentifier().setKey(status);
			server._utils.update(Collections.<IInstance> singleton(found));
		}
	}

	private void addLink(Server server, String from, String to)
			throws Exception {
		ensureStatusExistsDeleted(server, from);
		ensureStatusExistsDeleted(server, to);
		IMerge merge = server._utils.createMerge();
		merge.setFrom(from);
		merge.setTo(to);
		server._utils.update(Collections.<IInstance> singleton(merge));
	}

	@Test
	public void testFunnySyncTree() throws Exception {
		// Populate the tree in this way:
		// 0 -> 1 -> 2 -> 3 -> 5
		// and
		// 1 -> 4 -> 5
		// This makes a graph from 1 to 5 y two different ways
		// The ignore path is from 3 upwards
		// Hopefully the process shall stop at 1 or nearby to prevent going up
		// unnecessary
		Server server = new Server();
		addLink(server, "-2", "-1");
		addLink(server, "-1", "0");
		addLink(server, "0", "1");
		addLink(server, "1", "2");
		addLink(server, "2", "3");
		addLink(server, "1", "4");
		addLink(server, "4", "5");
		addLink(server, "3", "5");

		SyncServer ss = new SyncServer(server._persistence);
		DirectedGraphFromPersistence data = new DirectedGraphFromPersistence(
				server._persistence);
		List<String> sorted = ss.parentStatuses("5", "3", data);
		for (String forbidden : new String[] { "0", "1", "2", "3" }) {
			Assert.assertFalse("Status " + forbidden + " cannot be applied",
					sorted.contains(forbidden));
		}
	}
}
