package com.th3l4b.srm.codegen.sample.junit;

import java.util.LinkedHashMap;
import java.util.Map;

import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.sample.base.AbstractModelTest;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.inmemory.AbstractSampleInMemoryRuntime;

public class SampleModelTest extends AbstractModelTest {

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
}
