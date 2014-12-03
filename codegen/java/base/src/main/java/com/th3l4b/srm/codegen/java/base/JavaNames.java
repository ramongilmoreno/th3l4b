package com.th3l4b.srm.codegen.java.base;

import com.th3l4b.common.data.named.INamed;
import com.th3l4b.srm.model.base.IModel;

public class JavaNames {

	public String name(INamed f) throws Exception {
		return f.getName();
	}
	
	public String pkg (IModel m) throws Exception {
		return "aa.bb.cc";
	}
}
