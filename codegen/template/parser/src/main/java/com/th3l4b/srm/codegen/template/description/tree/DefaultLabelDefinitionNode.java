package com.th3l4b.srm.codegen.template.description.tree;

/**
 * @deprecated
 */
public class DefaultLabelDefinitionNode extends DefaultTemplateNode implements ILabelDefinitionNode {

	String _label;
	String _contents;
	
	public DefaultLabelDefinitionNode(String label, String contents) throws Exception {
		setLabel(label);
		setContents(contents);
	}
	
	@Override
	public void setLabel(String label) throws Exception {
		_label = label;
	}

	@Override
	public String getLabel() throws Exception {
		return _label;
	}
	
	@Override
	public String getContents() throws Exception {
		return _contents;
	}
	
	@Override
	public void setContents(String contents) throws Exception {
		_contents = contents;
	}

}
