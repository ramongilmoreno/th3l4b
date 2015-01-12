package com.th3l4b.srm.model.runtime;

import java.util.Collection;

public interface IReverse  {
	void add (IReverseRelationship reverseRelationship) throws Exception;
	void remove (IReverseRelationship reverseRelationship) throws Exception;
	Collection<IReverseRelationship> get (String targetType) throws Exception;
}
