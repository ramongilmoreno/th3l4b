package com.th3l4b.srm.codegen.template.description.tree;

/**
 * @deprecated
 */
public class DefaultLabelNode extends DefaultTemplateNode implements ILabelNode {

	String _label;
	
	public DefaultLabelNode(String label) throws Exception {
		setLabel(label);

	}
	
	@Override
	public void setLabel(String label) throws Exception {
		_label = label;
	}

	@Override
	public String getLabel() throws Exception {
		return _label;
	}
	
}
