package com.th3l4b.srm.model.runtime.codegen;

import java.util.Map;

public class CodegenUtils implements ICodegenConstants {
	public static String pkg(Map<String, String> properties) {
		return properties.get(PACKAGE_NAME);
	}

	public static String pkgToDir(Map<String, String> properties) {
		return pkgToDir(pkg(properties));
	}

	public static String pkgToDir(String pkg) {
		return pkg.replaceAll("\\.", "/");
	}
}
