package com.th3l4b.srm.codegen.sample.junit;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.srm.codegen.sample.generated.entities.IEntity1;
import com.th3l4b.srm.codegen.sample.generated.inmemory.AbstractSampleInMemoryModelRuntime;
import com.th3l4b.srm.codegen.sample.generated.utils.SampleModelUtils;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class BasicModelTest {

	SampleModelUtils createModelUtils() throws Exception {
		final Map<IIdentifier, IInstance> data = new LinkedHashMap<IIdentifier, IInstance>();
		IModelRuntime r = new AbstractSampleInMemoryModelRuntime() {
			@Override
			protected Map<IIdentifier, IInstance> getMap() throws Exception {
				return data;
			}
		};
		SampleModelUtils utils = new SampleModelUtils(r);
		return utils;
	}

	@Test
	public void testCreateEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();
		IEntity1 e1 = utils.createEntity1();
		Assert.assertEquals(EntityStatus.ToMerge, e1.coordinates().getStatus());
	}

	@Test
	public void testSaveEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();
		IEntity1 e1 = utils.createEntity1();
		IIdentifier id = e1.coordinates().getIdentifier();
		IInstance found = utils.finder().find(id);
		Assert.assertNotNull("Could not find unknown object", found);
		Assert.assertEquals("Unknown object is not Unknown",
				EntityStatus.Unknown, found.coordinates().getStatus());
		utils.getModelRuntime().updater()
				.update(Collections.<IInstance> singleton(e1));
		found = utils.finder().find(id);
		Assert.assertNotNull("Not found just created object", found);
		Assert.assertEquals(
				"Found object must be in saved status right after creating it",
				EntityStatus.Saved, found.coordinates().getStatus());
	}

	@Test
	public void testSaveDeletedEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();
		testSaveDeletedEntity(utils);
	}

	private IIdentifier testSaveDeletedEntity(SampleModelUtils utils)
			throws Exception {
		IEntity1 e1 = utils.createEntity1();
		e1.coordinates().setStatus(EntityStatus.ToDelete);
		IIdentifier id = e1.coordinates().getIdentifier();
		IInstance found = utils.finder().find(id);
		Assert.assertNotNull("Could not find unknown object", found);
		Assert.assertEquals("Unknown object is not Unknown",
				EntityStatus.Unknown, found.coordinates().getStatus());
		utils.getModelRuntime().updater()
				.update(Collections.<IInstance> singleton(e1));
		found = utils.finder().find(id);
		Assert.assertNotNull("Not found just created object", found);
		Assert.assertEquals(
				"Found object must be in deleted status right after creating it",
				EntityStatus.Deleted, found.coordinates().getStatus());
		return id;
	}

	@Test
	public void testMergeDeletedEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();
		IIdentifier id = testSaveDeletedEntity(utils);

		// Modify a value. Status untouched.
		IEntity1 e = utils.createEntity1();
		e.coordinates().setIdentifier(id);
		String value = UUID.randomUUID().toString();
		e.setField11(value);
		utils.getModelRuntime().updater()
				.update(Collections.<IInstance> singleton(e));

		// Find result
		IEntity1 found = utils.finder().findEntity1(id.getKey());
		Assert.assertEquals("Modified field was not saved", value,
				found.getField11());
		Assert.assertEquals("Item was not left deleted", EntityStatus.Deleted,
				found.coordinates().getStatus());
	}

	@Test
	public void testSavePreviouslyDeletedEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();
		IIdentifier id = testSaveDeletedEntity(utils);

		// Modify a value. Status untouched.
		IEntity1 e = utils.createEntity1();
		e.coordinates().setIdentifier(id);
		e.coordinates().setStatus(EntityStatus.ToSave);
		String value = UUID.randomUUID().toString();
		e.setField11(value);
		utils.getModelRuntime().updater()
				.update(Collections.<IInstance> singleton(e));

		// Find result
		IEntity1 found = utils.finder().findEntity1(id.getKey());
		Assert.assertEquals("Modified field was not saved", value,
				found.getField11());
		Assert.assertEquals("Item was not restored as saved",
				EntityStatus.Saved, found.coordinates().getStatus());
	}
}
