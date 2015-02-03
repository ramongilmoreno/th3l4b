package com.th3l4b.srm.codegen.sample.junit;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.srm.codegen.sample.generated.entities.IEntity1;
import com.th3l4b.srm.codegen.sample.generated.inmemory.AbstractSampleInMemoryModelRuntime;
import com.th3l4b.srm.codegen.sample.generated.utils.SampleModelUtils;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IReverse;

public class BasicModelTest {

	SampleModelUtils createModelUtils() throws Exception {
		final Map<IIdentifier, IInstance> data = new LinkedHashMap<IIdentifier, IInstance>();
		IModelRuntime r = new AbstractSampleInMemoryModelRuntime() {
			@Override
			protected Map<IIdentifier, IInstance> getMap() throws Exception {
				return data;
			}

			@Override
			protected IReverse createReverse() throws Exception {
				throw new UnsupportedOperationException("Not implemented yet");
			}
		};
		SampleModelUtils utils = new SampleModelUtils(r);
		return utils;
	}

	@Test
	public void testCreateEntity() throws Exception {
		SampleModelUtils utils = createModelUtils();
		IEntity1 e1 = utils.createEntity1();
		Assert.assertEquals(EntityStatus.ToModify, e1.coordinates().getStatus());
	}
}