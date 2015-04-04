package com.th3l4b.srm.codegen.sample.junit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.th3l4b.srm.sync.base.SyncUtils;

public class SampleSyncTest {

	protected SampleModelUtils createModelUtils() throws Exception {
		final Map<IIdentifier, IInstance> data = new LinkedHashMap<IIdentifier, IInstance>();
		IRuntime r = new AbstractSampleInMemoryRuntime() {
			@Override
			protected Map<IIdentifier, IInstance> getMap() throws Exception {
				return data;
			}
		};
		SampleModelUtils utils = new SampleModelUtils(r);
		return utils;
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
}
