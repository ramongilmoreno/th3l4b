package com.th3l4b.srm.model.runtime.implementation;

public abstract class AbstractIndexBasedRuntimeEntiy extends
		AbstractRuntimeEntity {

	protected String[] _values;
	protected boolean[] _isSet;

	public AbstractIndexBasedRuntimeEntiy() throws Exception {
		int l = fields().length;
		_values = new String[l];
		_isSet = new boolean[l];
	}

	protected abstract int indexOfField(String field) throws Exception;

	@Override
	public String get(String field) throws Exception {
		return _values[indexOfField(field)];
	}

	@Override
	public void set(String field, String value) throws Exception {
		_values[indexOfField(field)] = value;
		_isSet[indexOfField(field)] = true;
	}

	@Override
	public boolean isSet(String field) throws Exception {
		return _isSet[indexOfField(field)];
	}

	@Override
	public void unSet(String field) throws Exception {
		_isSet[indexOfField(field)] = false;
	}
}
