package com.th3l4b.srm.codegen.template.runtime;

public class CodegenUtils {
	public static String packageToDir (String pkg) {
		return pkg.replaceAll("\\.", "/");
	}
}
