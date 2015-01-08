package com.th3l4b.srm.codegen.java.runtime;

import java.util.Iterator;

import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFieldRuntime;

@Deprecated
public abstract class AbstractIndexBasedRuntimeEntiy extends
		AbstractInstance {

	protected IEntityRuntime _runtime;
	protected String[] _values;
	protected boolean[] _isSet;

	public AbstractIndexBasedRuntimeEntiy(IEntityRuntime runtime) throws Exception {
		_runtime = runtime;
		Iterator<IFieldRuntime> i = runtime.iterator();
		int count = 0;
		while (i.hasNext()) {
			i.next();
			count++;
		}
		_values = new String[count];
		_isSet = new boolean[count];
	}

	protected abstract int indexOfField(String field) throws Exception;
}
