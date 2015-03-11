package com.th3l4b.common.data.named;

public interface IContainer<N extends INamed> extends Iterable<N> {
	N get (String name) throws Exception;
	void add (N named) throws Exception;
	void remove (String name) throws Exception;
	boolean contains (String name) throws Exception;
	int size () throws Exception;
}
