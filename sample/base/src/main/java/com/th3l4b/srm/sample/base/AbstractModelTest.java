package com.th3l4b.srm.sample.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.common.data.NullSafe;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IUpdater;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.entities.IEntity1;
import com.th3l4b.srm.sample.base.generated.entities.IEntity2;

public abstract class AbstractModelTest {

	protected abstract SampleModelUtils createModelUtils() throws Exception;

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
		utils.update(Collections.<IInstance> singleton(e1));
		found = utils.finder().find(id);
		Assert.assertNotNull("Not found just created object", found);
		Assert.assertEquals(
				"Found object must be in saved status right after creating it",
				EntityStatus.Saved, found.coordinates().getStatus());
	}

	@Test
	public void testUpdateEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();

		// Persist entity with one field set
		IEntity1 e1 = utils.createEntity1();
		IIdentifier id = e1.coordinates().getIdentifier();
		String v1 = "hello";
		e1.setField11(v1);
		IUpdater updater = utils.updater();
		updater.update(Collections.<IInstance> singleton(e1));

		// Persist entity setting the second field
		e1 = utils.createEntity1();
		e1.coordinates().setIdentifier(id);
		String v2 = "bye";
		e1.setField12(v2);
		updater.update(Collections.<IInstance> singleton(e1));

		// Check result keeps both values
		e1 = utils.finder().findEntity1(id);
		Assert.assertEquals(v1, e1.getField11());
		Assert.assertEquals(v2, e1.getField12());
	}

	@Test
	public void testSaveDeletedEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();
		testSaveDeletedEntity(utils);
	}

	private IIdentifier testSaveDeletedEntity(SampleModelUtils utils)
			throws Exception {
		IEntity1 e1 = utils.createEntity1();
		ICoordinates coordinates = e1.coordinates();
		coordinates.setStatus(EntityStatus.ToDelete);
		IIdentifier id = coordinates.getIdentifier();
		IInstance found = utils.finder().find(id);
		Assert.assertNotNull("Could not find unknown object", found);
		Assert.assertEquals("Unknown object is not Unknown",
				EntityStatus.Unknown, found.coordinates().getStatus());
		utils.update(Collections.<IInstance> singleton(e1));
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
		utils.update(Collections.<IInstance> singleton(e));

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
		utils.update(Collections.<IInstance> singleton(e));

		// Find result
		IEntity1 found = utils.finder().findEntity1(id.getKey());
		Assert.assertEquals("Modified field was not saved", value,
				found.getField11());
		Assert.assertEquals("Item was not restored as saved",
				EntityStatus.Saved, found.coordinates().getStatus());
	}

	@Test
	public void testReverse() throws Exception {
		SampleModelUtils utils = createModelUtils();
		IEntity1 e1a = utils.createEntity1();
		IEntity1 e1b = utils.createEntity1();
		IEntity1 e1c = utils.createEntity1();
		IEntity2 e2 = utils.createEntity2();
		e1a.setReference(e2);
		e1b.setReference(e2);
		e1c.setReference(e2);
		e1c.coordinates().setStatus(EntityStatus.ToDelete);
		ArrayList<IInstance> updates = new ArrayList<IInstance>();
		updates.add(e1a);
		updates.add(e1b);
		updates.add(e1c);
		updates.add(e2);
		utils.update(updates);
		Collection<IEntity1> refs = utils.finder().referencesEntity2_Reverse(
				e2.coordinates().getIdentifier().getKey());
		boolean e1aFound = false;
		boolean e1bFound = false;
		for (IInstance ref : refs) {
			IIdentifier id = ref.coordinates().getIdentifier();
			if (NullSafe.equals(id, e1a.coordinates().getIdentifier())) {
				if (e1aFound) {
					Assert.fail("Instance e1a found twice");
				}
				e1aFound = true;
			}
			if (NullSafe.equals(id, e1b.coordinates().getIdentifier())) {
				if (e1bFound) {
					Assert.fail("Instance e1b found twice");
				}
				e1bFound = true;
			}
			if (NullSafe.equals(id, e1c.coordinates().getIdentifier())) {
				Assert.fail("Instance e1c was saved deleted. Should not be returned by the reverse relationship");
			}
		}
		Assert.assertTrue("Only one of the entities was not found", e1aFound
				&& e1bFound);

		// Assert deleted item was inserted properly and returned as deleted
		IEntity1 e1cDeleted = utils.finder().findEntity1(
				e1c.coordinates().getIdentifier());
		Assert.assertEquals(
				"Item inserted as deleted in the reverse relationship was not correctly saved as deleted",
				EntityStatus.Deleted, e1cDeleted.coordinates().getStatus());
		Assert.assertEquals(
				"Item inserted as deleted in the reverse relationship does not point to the source object",
				e2.coordinates().getIdentifier().getKey(),
				e1cDeleted.getReference());
	}

	@Test
	public void testToString() throws Exception {
		SampleModelUtils utils = createModelUtils();
		new SampleData().fill(utils.runtime());
		for (IEntity1 e : utils.finder().allEntity1()) {
			System.out.println(e);
		}
		for (IEntity2 e2 : utils.finder().allEntity2()) {
			System.out.println(e2);
		}
	}

	@Test
	public void testDeletedReferences() throws Exception {

		// Check that everything is correctly setup
		SampleModelUtils utils = createModelUtils();
		IEntity1 e1 = utils.createEntity1();
		IEntity2 e2a = utils.createEntity2();
		e2a.setEntity1(e1);
		IEntity2 e2b = utils.createEntity2();
		e2b.setEntity1(e1);
		utils.update(Arrays.asList(new IInstance[] { e1, e2a, e2b }));
		Collection<IEntity2> found = utils.finder()
				.referencesEntity1_Sample(e1);
		Assert.assertEquals(2, found.size());

		// Now, delete one of the references
		e2b.coordinates().setStatus(EntityStatus.ToDelete);
		utils.update(Collections.<IInstance> singleton(e2b));

		// Check that everything is correctly setup
		found = utils.finder().referencesEntity1_Sample(e1);
		Assert.assertEquals(1, found.size());
	}

	/**
	 * Do not mark this as {@link Test} or an infinite loop will happen. This
	 * utility method is intented to execute all tests without requiring
	 * updating the list of tests.
	 */
	public void testAll() throws Exception {
		for (Method m : getClass().getMethods()) {
			if (m.isAnnotationPresent(Test.class)) {
				System.out.println("===== About to run test: " + m);
				m.invoke(this);
				System.out.println("=============== OK test: " + m);
			}
		}
	}
}
