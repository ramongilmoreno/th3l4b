package com.th3l4b.srm.codegen.template.description.tree;

public class DefaultTextNode extends TemplateNodeUtils implements ITextNode {

	private String _text;
	
	public DefaultTextNode (String text) throws Exception {
		setText(text);
	}

	@Override
	public String getText() throws Exception {
		return _text;
	}

	@Override
	public void setText(String text) throws Exception {
		_text = text;
	}

}
