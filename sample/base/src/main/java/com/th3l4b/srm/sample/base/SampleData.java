package com.th3l4b.srm.sample.base;

import java.util.ArrayList;

import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.entities.IEntity1;
import com.th3l4b.srm.sample.base.generated.entities.IEntity2;

public class SampleData {
	public void fill(IModelRuntime runtime) throws Exception {
		SampleModelUtils sample = new SampleModelUtils(runtime);
		ArrayList<IInstance> updates = new ArrayList<IInstance>();
		IEntity2 forReverse = sample.createEntity2();
		updates.add(forReverse);
		for (int i = 0; i < 10; i++) {
			{
				IEntity1 e = sample.createEntity1();
				e.setField11("Sample field 1 of entity 1 #" + i);
				e.setField12("Sample field 2 of entity 1 #" + i);
				e.setReference(forReverse);
				updates.add(e);
			}
			{
				IEntity2 e = sample.createEntity2();
				updates.add(e);
			}
		}
		runtime.updater().update(updates);
	}
}
