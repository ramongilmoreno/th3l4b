package com.th3l4b.common.data;

public class NullSafe {

	public static boolean equals(Object a, Object b) {
		if (a == b) {
			return true;
		} else if ((a == null) || (b == null)) {
			return false;
		} else {
			return a.equals(b) && b.equals(a);
		}
	}

	public static int hashCode(Object o) {
		if (o == null) {
			return 0;
		} else {
			return o.hashCode();
		}
	}
	
	public static String toString (Object o) {
		if (o == null) {
			return "" + null;
		} else {
			return o.toString();
		}
	}
}
