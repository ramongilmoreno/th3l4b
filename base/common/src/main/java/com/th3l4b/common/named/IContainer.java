package com.th3l4b.common.named;

public interface IContainer<N extends INamed> extends Iterable<N> {
	void add (N named) throws Exception;
	void remove (String name) throws Exception;
	boolean contains (String name) throws Exception;
}
