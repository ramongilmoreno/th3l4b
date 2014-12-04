package com.th3l4b.common.text.codegen;

public class CamelUtils {

	public static String toCamelCase(String src) {
		if (src == null) {
			return null;
		}
		StringPrintWriter sw = new StringPrintWriter();

		boolean uppercasePending = true;
		for (Integer i : TextUtils.unicodeIterable(src)) {
			int c = i.intValue();
			if (uppercasePending) {
				// Skip leading spaces
				if (Character.isWhitespace(c)) {
					continue;
				}
				uppercasePending = false;
				TextUtils.print(Character.toUpperCase(c), sw);
			} else {
				if (Character.isWhitespace(c)) {
					uppercasePending = true;
					// Skip this character
				} else {
					TextUtils.print(c, sw);
				}
			}
		}

		return sw.getBuffer().toString();

	}
}
