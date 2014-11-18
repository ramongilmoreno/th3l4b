package com.th3l4b.common.data;

public class NullSafe {
	public static boolean equals(Object a, Object b) throws Exception {
		if (a == b) {
			return true;
		} else if ((a == null) || (b == null)) {
			return false;
		} else {
			return a.equals(b) && b.equals(a);
		}
	}
}
