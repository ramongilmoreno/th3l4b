package com.th3l4b.srm.json;

import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class JsonUtils {

	public static JsonNames NAMES = new JsonNames();

	public static IJsonModelRuntime runtime(IModelRuntime runtime)
			throws Exception {
		DefaultJsonModelRuntime jmr = new DefaultJsonModelRuntime(runtime);
		for (IEntityRuntime er : runtime) {
			DefaultJsonEntityRuntime jer = new DefaultJsonEntityRuntime(er);
			for (IFieldRuntime fr : er) {
				DefaultJsonFieldRuntime jfr = new DefaultJsonFieldRuntime(fr);
				jer.add(jfr);
			}
			jmr.add(jer);
		}

		return jmr;
	}
}
