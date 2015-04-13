package com.th3l4b.srm.codegen.sample.junit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

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
import com.th3l4b.srm.sync.server.generated.entities.IClient;
import com.th3l4b.srm.sync.server.generated.entities.IStatus;
import com.th3l4b.srm.sync.server.generated.inmemory.AbstractServerSyncInMemoryRuntime;

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
		private ServerSyncModelUtils _utils;

		public Server() throws Exception {
			_map = new LinkedHashMap<IIdentifier, IInstance>();
			_repository = new AbstractServerSyncInMemoryRuntime() {
				@Override
				protected Map<IIdentifier, IInstance> getMap() throws Exception {
					return _map;
				}
			};
			_utils = new ServerSyncModelUtils(_repository);
			_syncServer = new SyncServer(_repository);
		}

		public void register(Client client) throws Exception {
			IClient c = _utils.createClient();
			c.coordinates().getIdentifier().setKey(client._id);
			IStatus s = _utils.createStatus();
			c.setStatus(s);
			log("Registered status: " + s);
			ArrayList<IInstance> updates = new ArrayList<IInstance>();
			updates.add(c);
			updates.add(s);
			_utils.getRuntime().updater().update(updates);
		}

		public void sync(Client client) throws Exception {
			SyncResult discovered = _syncServer.discover(client._id,
					client._tracker.pendingUpdates()._changes,
					SampleModelUtils.RUNTIME);
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

		// Resync
		resync(client1, server, i);
		// This caused an NPE during tests
		resync(client1, server, i);
		resync(client2, server, i);
		resync(client3, server, i);
		
		// Resync again to check no status from the first phase are used again
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
		client._utils.getRuntime().updater()
				.update(Collections.<IInstance> singleton(e1));
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
}
