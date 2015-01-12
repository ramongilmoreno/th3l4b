package com.th3l4b.common.data;

public class DefaultPair<A, B> implements IPair<A, B> {

	protected A _a;
	protected B _b;

	public DefaultPair() {
	}

	public DefaultPair(A a, B b) {
		_a = a;
		_b = b;
	}

	public A getA() {
		return _a;
	}

	public void setA(A a) {
		_a = a;
	}

	public B getB() {
		return _b;
	}

	public void setB(B b) {
		_b = b;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IPair<?, ?>) {
			IPair<?, ?> p = (IPair<?, ?>) obj;
			Object obja = p.getA();
			Object a = getA();
			if (!NullSafe.equals(a, obja)) {
				return false;
			}
			Object objb = p.getB();
			Object b = getB();
			if (!NullSafe.equals(b, objb)) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return NullSafe.hashCode(getA()) ^ NullSafe.hashCode(getB());
	}

	@Override
	public String toString() {
		return "[" + getA() + ", " + getB() + "]";
	}

}