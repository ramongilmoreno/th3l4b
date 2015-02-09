package com.th3l4b.srm.codegen.java.runtime;

import com.th3l4b.common.data.named.DefaultNamed;
import com.th3l4b.srm.model.runtime.IReverseRelationship;

public class DefaultReverseRelationship extends DefaultNamed implements IReverseRelationship {

	private String _sourceType;
	private String _field;
	private String _targetType;
	
	public DefaultReverseRelationship(String sourceType, String field,
			String targetType, String name) throws Exception {
		setSourceType(sourceType);
		setField(field);
		setTargetType(targetType);
		setName(name);
	} 

	@Override
	public String getSourceType() throws Exception {
		return _sourceType;
	}

	@Override
	public void setSourceType(String sourceType) throws Exception {
		_sourceType = sourceType;
	}

	@Override
	public String getField() throws Exception {
		return _field;
	}

	@Override
	public void setField(String field) throws Exception {
		_field = field;
	}

	@Override
	public String getTargetType() throws Exception {
		return _targetType;
	}

	@Override
	public void setTargetType(String targetType) throws Exception {
		_targetType = targetType;
	}
}
