package com.th3l4b.srm.codegen.template.description.tree;

public class DefaultSubstitutionNode extends DefaultTemplateNode implements
		ISubstitutionNode {

	private String _substitution;

	public DefaultSubstitutionNode(String substitution) throws Exception {
		setSubstitution(substitution);
	}

	@Override
	public String getSubstitution() throws Exception {
		return _substitution;
	}

	@Override
	public void setSubstitution(String s) throws Exception {
		_substitution = s;
	}

}
