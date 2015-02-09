package com.th3l4b.srm.model.runtime;

import com.th3l4b.common.data.named.IContainer;

public interface IReverse  {
	void add (IReverseRelationship reverseRelationship) throws Exception;
	void remove (IReverseRelationship reverseRelationship) throws Exception;
	IContainer<IReverseRelationship> get (String targetType) throws Exception;
}
