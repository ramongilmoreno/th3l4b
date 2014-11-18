package com.th3l4b.srm.codegen.template.description.tree;

import java.util.Collection;

public interface ITemplateNode {
	Collection<ITemplateNode> children () throws Exception;
}
