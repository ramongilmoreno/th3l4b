package com.th3l4b.srm.sync.client;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.th3l4b.srm.json.Generator;
import com.th3l4b.srm.json.JsonUtils;
import com.th3l4b.srm.json.Parser;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;
import com.th3l4b.srm.model.runtime.RuntimeFilter;
import com.th3l4b.srm.sync.base.SyncUtils;
import com.th3l4b.srm.sync.client.generated.ClientSyncModelUtils;
import com.th3l4b.srm.sync.client.generated.entities.IUpdate;

/**
 * Facade to track changes on a tracked environment. The updates are logged into
 * a repository environment.
 */
public class ClientUpdateTracker {

	private IUpdater _originalTrackedUpdater;
	private IRuntime _tracked;
	private IRuntime _repository;
	private IRuntime _original;

	public ClientUpdateTracker(final IRuntime tracked, IRuntime repository)
			throws Exception {
		_original = tracked;
		_originalTrackedUpdater = tracked.updater();
		final IUpdater updater = new IUpdater() {
			@Override
			public Collection<IInstance> update(Collection<IInstance> entities)
					throws Exception {
				ClientUpdateTracker.this.track(entities);
				return _originalTrackedUpdater.update(entities);
			}
		};

		_tracked = new RuntimeFilter(tracked) {
			@Override
			public IUpdater updater() throws Exception {
				return updater;
			}
		};
		_repository = repository;
	}

	public IRuntime getOriginal() {
		return _original;
	}

	public IRuntime getTracked() {
		return _tracked;
	}

	public IRuntime getRepository() {
		return _repository;
	}

	protected void track(Collection<IInstance> entities) throws Exception {
		entities = SyncUtils.groupUpdates(entities, getTracked().model());
		StringWriter sw = new StringWriter();
		Generator generator = new Generator(JsonUtils.runtime(getTracked()
				.model()), sw);
		generator.write(entities);
		generator.close();

		ClientSyncModelUtils csmu = new ClientSyncModelUtils(getRepository());
		ArrayList<IInstance> u = new ArrayList<IInstance>();

		IUpdate update = csmu.createUpdate();
		update.setContents(sw.getBuffer().toString());
		u.add(update);
		csmu.update(u);
	}

	public class PendingUpdates {
		public Collection<IInstance> _changes = new ArrayList<IInstance>();
		public Collection<IUpdate> _updates = new ArrayList<IUpdate>();
	}

	/**
	 * Synces the given updates with those from the repository.
	 * 
	 * @return The updates tracked
	 */
	public Collection<IInstance> sync(Collection<IInstance> remote)
			throws Exception {
		// Null safe protection
		remote = remote == null ? Collections.<IInstance>emptyList() : remote;
		
		// Group initial updates
		remote = SyncUtils.groupUpdates(remote, getTracked().model());

		PendingUpdates pu = pendingUpdates();

		// Compute difference
		Collection<IInstance> missing = SyncUtils.missingUpdates(remote,
				pu._changes, getTracked().model());

		// Apply changes
		_originalTrackedUpdater.update(missing);

		// Delete all updates
		ArrayList<IInstance> discarded = new ArrayList<IInstance>();
		for (IUpdate u : pu._updates) {
			u.coordinates().setStatus(EntityStatus.ToDelete);
			discarded.add(u);
		}
		getRepository().updater().update(discarded);

		// Return local changes
		return pu._changes;
	}

	public PendingUpdates pendingUpdates() throws Exception {
		// Find all tracked changes
		ClientSyncModelUtils csmu = new ClientSyncModelUtils(getRepository());
		PendingUpdates pu = new PendingUpdates();
		for (IUpdate u : csmu.finder().allUpdate()) {
			pu._updates.add(u);
			String contents = u.getContents();
			if (contents != null) {
				StringReader sr = new StringReader(contents);
				Parser parser = new Parser(JsonUtils.runtime(getTracked()
						.model()), sr);
				for (IInstance i : parser.parse(false, true)._many) {
					pu._changes.add(i);
				}
			}
		}
		pu._changes = SyncUtils.groupUpdates(pu._changes, getTracked().model());
		return pu;
	}
}
