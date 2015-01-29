package com.th3l4b.srm.codegen.sample.junit;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.srm.codegen.sample.generated.entities.IEntity1;
import com.th3l4b.srm.codegen.sample.generated.utils.SampleModelUtils;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class BasicModelTest {

	SampleModelUtils createModelUtils() throws Exception {
		IModelRuntime r = null;
		SampleModelUtils utils = new SampleModelUtils(r);
		utils = null;
		return utils;
	}

	@Test
	public void testCreateEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();
		if (utils == null) {
			return;
		}
		IEntity1 e1 = utils.createEntity1();
		Assert.assertEquals(EntityStatus.ToModify, e1.coordinates().getStatus());
	}
}
