package com.th3l4b.srm.codegen.template.description.nodes;

public class DefaultCodeNode implements ICodeNode {

	private String _code;

	public DefaultCodeNode(String code) throws Exception {
		setCode(code);
	}

	@Override
	public String getCode() throws Exception {
		return _code;
	}

	@Override
	public void setCode(String code) throws Exception {
		_code = code;
	}

}
