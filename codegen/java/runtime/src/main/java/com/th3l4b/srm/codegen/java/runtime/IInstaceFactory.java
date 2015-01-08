package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.srm.model.runtime.IInstance;

public interface IInstaceFactory {
	IInstance create () throws Exception;
}
