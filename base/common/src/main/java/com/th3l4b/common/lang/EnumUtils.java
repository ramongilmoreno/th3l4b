package com.th3l4b.common.lang;

public class EnumUtils {
	public static <T extends Enum<T>> T failSafeParse(String value,
			Class<T> enumClass, T fallback) {
		try {
			return Enum.valueOf(enumClass, value);
		} catch (Exception e) {
			return fallback;
		}
	}

}
