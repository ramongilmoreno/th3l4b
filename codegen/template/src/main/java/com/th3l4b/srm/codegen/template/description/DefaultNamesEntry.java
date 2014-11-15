package com.th3l4b.srm.codegen.template.description;

public class DefaultNamesEntry implements INamesEntry {

	private String _clazz;
	private String _name;

	public DefaultNamesEntry(String clazz, String name) throws Exception {
		setClazz(clazz);
		setName(name);
	}
	
	@Override
	public String getClazz() throws Exception {
		return _clazz;
	}

	@Override
	public void setClazz(String clazz) throws Exception {
		_clazz = clazz;
	}

	@Override
	public String getName() throws Exception {
		return _name;
	}

	@Override
	public void setName(String name) throws Exception {
		_name = name;
	}

}
