package com.th3l4b.srm.codegen.sample.junit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;
import com.th3l4b.srm.sample.base.generated.ISampleFinder;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.entities.IEntity1;
import com.th3l4b.srm.sample.base.generated.entities.IEntity2;
import com.th3l4b.srm.sample.base.generated.inmemory.AbstractSampleInMemoryRuntime;
import com.th3l4b.srm.sync.base.SyncUtils;
import com.th3l4b.srm.sync.base.UpdateTracker;
import com.th3l4b.srm.sync.base.generated.inmemory.AbstractSyncInMemoryRuntime;

public class SampleSyncTest {

	protected SampleModelUtils createModelUtils() throws Exception {
		SampleModelUtils utils = new SampleModelUtils(createSampleRuntime());
		return utils;
	}

	private IRuntime createSampleRuntime() {
		final Map<IIdentifier, IInstance> data = new LinkedHashMap<IIdentifier, IInstance>();
		IRuntime r = new AbstractSampleInMemoryRuntime() {
			@Override
			protected Map<IIdentifier, IInstance> getMap() throws Exception {
				return data;
			}
		};
		return r;
	}

	@Test
	public void testMerge() throws Exception {
		SampleModelUtils smu = createModelUtils();

		IEntity1 e1 = smu.createEntity1();
		String v1 = "Value 1";
		e1.setField11(v1);
		ICoordinates coordinates = e1.coordinates();
		coordinates.setStatus(EntityStatus.ToDelete);
		IIdentifier id = coordinates.getIdentifier();

		IEntity1 e2 = smu.createEntity1();
		ICoordinates coordinates2 = e2.coordinates();
		coordinates2.setIdentifier(id);
		coordinates2.setStatus(EntityStatus.ToMerge);
		String v2 = "Value 2";
		e2.setField12(v2);

		// Group modifications in a single instance
		ArrayList<IInstance> col = new ArrayList<IInstance>();
		col.add(e1);
		col.add(e2);
		Collection<IInstance> grouped = SyncUtils.groupUpdates(col,
				SampleModelUtils.RUNTIME);
		Assert.assertEquals("Updates were not merged", 1, grouped.size());

		// Check update was correct
		smu.getRuntime().updater().update(grouped);
		IEntity1 found = smu.finder().findEntity1(id);
		Assert.assertEquals(v1, found.getField11());
		Assert.assertEquals(v2, found.getField12());
		Assert.assertEquals(EntityStatus.Deleted, found.coordinates()
				.getStatus());
	}

	@Test
	public void testMissingUpdates() throws Exception {
		SampleModelUtils smu = createModelUtils();

		IEntity1 e1 = smu.createEntity1();
		String v1 = "Value 1";
		e1.setField11(v1);
		e1.setField12("Overwritten value");
		ICoordinates coordinates = e1.coordinates();
		coordinates.setStatus(EntityStatus.ToDelete);
		IIdentifier id = coordinates.getIdentifier();

		IEntity1 e2 = smu.createEntity1();
		ICoordinates coordinates2 = e2.coordinates();
		coordinates2.setIdentifier(id);
		coordinates2.setStatus(EntityStatus.ToMerge);
		String v2 = "Value 2";
		e2.setField12(v2);

		// Apply second step
		List<IInstance> step2 = Collections.<IInstance> singletonList(e2);
		IUpdater updater = smu.getRuntime().updater();
		updater.update(step2);

		// Compute delta of first step
		List<IInstance> step1 = Collections.<IInstance> singletonList(e1);
		Collection<IInstance> missing = SyncUtils.missingUpdates(step1, step2,
				SampleModelUtils.RUNTIME);

		Assert.assertNotEquals(0, missing.size());
		updater.update(missing);
		IEntity1 found = smu.finder().findEntity1(id);
		Assert.assertEquals(v1, found.getField11());
		Assert.assertEquals(v2, found.getField12());
		Assert.assertEquals(EntityStatus.Deleted, found.coordinates()
				.getStatus());
	}

	@Test
	public void testMissingUpdates2() throws Exception {
		SampleModelUtils smu = createModelUtils();

		IEntity1 e1 = smu.createEntity1();
		e1.setField12("Overwritten value");
		ICoordinates coordinates = e1.coordinates();
		coordinates.setStatus(EntityStatus.ToMerge);
		IIdentifier id = coordinates.getIdentifier();

		IEntity1 e2 = smu.createEntity1();
		ICoordinates coordinates2 = e2.coordinates();
		coordinates2.setIdentifier(id);
		coordinates2.setStatus(EntityStatus.ToDelete);
		String v2 = "Value 2";
		e2.setField12(v2);

		// Apply second step
		List<IInstance> step2 = Collections.<IInstance> singletonList(e2);
		IUpdater updater = smu.getRuntime().updater();
		updater.update(step2);

		// Compute delta of first step
		List<IInstance> step1 = Collections.<IInstance> singletonList(e1);
		Collection<IInstance> missing = SyncUtils.missingUpdates(step1, step2,
				SampleModelUtils.RUNTIME);

		// Check this time updates are empty (everything was updated by the
		// second step updates, no need of any update from the step 1).
		Assert.assertEquals(0, missing.size());
		IEntity1 found = smu.finder().findEntity1(id);
		Assert.assertEquals(v2, found.getField12());
		Assert.assertEquals(EntityStatus.Deleted, found.coordinates()
				.getStatus());
	}

	class TrackedEnvironment {
		IRuntime _tracked;
		IRuntime _repository;
		UpdateTracker _updateTracker;
		SampleModelUtils _sampleModelUtils;

		public TrackedEnvironment() throws Exception {
			_tracked = createSampleRuntime();
			_repository = new AbstractSyncInMemoryRuntime() {
				Map<IIdentifier, IInstance> _map = new HashMap<IIdentifier, IInstance>();

				@Override
				protected Map<IIdentifier, IInstance> getMap() throws Exception {
					return _map;
				}
			};
			_updateTracker = new UpdateTracker(_tracked, _repository);
			_sampleModelUtils = new SampleModelUtils(
					_updateTracker.getTracked());
		}
	}

	@Test
	public void testSync() throws Exception {
		TrackedEnvironment te1 = new TrackedEnvironment();
		TrackedEnvironment te2 = new TrackedEnvironment();

		String v1 = "Hello";
		IEntity1 e1 = te1._sampleModelUtils.createEntity1();
		e1.setField11(v1);
		te1._sampleModelUtils.getRuntime().updater()
				.update(Collections.<IInstance> singletonList(e1));
		UpdateTracker.PendingUpdates pu1 = te1._updateTracker.pendingUpdates();
		Assert.assertEquals(1, pu1._changes.size());

		String v2 = "Bye";
		IEntity1 e2 = te2._sampleModelUtils.createEntity1();
		e2.setField11(v2);
		te2._sampleModelUtils.getRuntime().updater()
				.update(Collections.<IInstance> singletonList(e2));

		String v3 = "Other";
		IEntity2 e3 = te1._sampleModelUtils.createEntity2();
		e3.setField21(v3);
		te2._sampleModelUtils.getRuntime().updater()
				.update(Collections.<IInstance> singletonList(e3));

		// Sync first environment with the changes from the second
		UpdateTracker.PendingUpdates pu2 = te2._updateTracker.pendingUpdates();
		Collection<IInstance> mu = SyncUtils.missingUpdates(pu2._changes,
				pu1._changes, SampleModelUtils.RUNTIME);
		Assert.assertEquals(2, mu.size());
		te1._sampleModelUtils.getRuntime().updater().update(mu);

		// Check updates
		ISampleFinder finder = te1._sampleModelUtils.finder();
		IEntity1 fe1 = finder.findEntity1(e1.coordinates().getIdentifier());
		Assert.assertEquals(EntityStatus.Saved, fe1.coordinates().getStatus());
		Assert.assertEquals(v1, fe1.getField11());
		IEntity1 fe2 = finder.findEntity1(e2.coordinates().getIdentifier());
		Assert.assertEquals(EntityStatus.Saved, fe2.coordinates().getStatus());
		Assert.assertEquals(v2, fe2.getField11());
	}
}
