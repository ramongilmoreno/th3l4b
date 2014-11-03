package com.th3l4b.srm.codegen.template.execution;

import com.th3l4b.srm.codegen.template.description.tree.IIterationNode;
import com.th3l4b.srm.codegen.template.description.tree.ISubstitutionNode;
import com.th3l4b.srm.codegen.template.description.tree.ITemplateNode;
import com.th3l4b.srm.codegen.template.description.tree.ITextNode;
import com.th3l4b.srm.codegen.template.description.tree.IterationType;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IField;
import com.th3l4b.srm.model.base.IModel;

public class TemplateExecutioner {

	/**
	 * Writes in the {@link ITemplateExecutionContext#getOut()} the result of
	 * applying the context to the {@link ITemplateNode} hierarchy.
	 */
	public void execute(ITemplateNode root, ITemplateExecutionContext context)
			throws Exception {
		internalExecute(root, context);
	}

	private void internalExecute(ITemplateNode node,
			ITemplateExecutionContext context) throws Exception {
		if (node instanceof ITextNode) {
			ITextNode textNode = (ITextNode) node;
			context.getOut().write(textNode.getSubstitution());
			for (ITemplateNode n : node.children()) {
				internalExecute(n, context);
			}
		} else if (node instanceof IIterationNode) {
			IIterationNode iterationNode = (IIterationNode) node;
			switch (iterationNode.getIteration()) {
			case entity:
				IModel m = (IModel) context.getCurrent();
				for (IEntity e : m) {
					ChangeCurrent newContext = new ChangeCurrent(e, context);
					for (ITemplateNode n : node.children()) {
						internalExecute(n, newContext);
					}
				}
				break;
			case field: {
				IEntity e = (IEntity) context.getCurrent();
				for (IField f : e) {
					ChangeCurrent newContext = new ChangeCurrent(f, context);
					for (ITemplateNode n : node.children()) {
						internalExecute(n, newContext);
					}
				}
				break;
			}
			case fieldImmediate: {
				IEntity e = (IEntity) context.getCurrent();
				for (IField f : e) {
					if (f.isReference()) {
						continue;
					}
					ChangeCurrent newContext = new ChangeCurrent(f, context);
					for (ITemplateNode n : node.children()) {
						internalExecute(n, newContext);
					}
				}
				break;
			}
			case fieldReference: {
				IEntity e = (IEntity) context.getCurrent();
				for (IField f : e) {
					if (!f.isReference()) {
						continue;
					}
					ChangeCurrent newContext = new ChangeCurrent(f, context);
					for (ITemplateNode n : node.children()) {
						internalExecute(n, newContext);
					}
				}
				break;
			}
			default:
				throw new IllegalArgumentException(
						"Don't know how to handle an " + IterationType.class
								+ " as: " + iterationNode.getIteration());
			}
		} else if (node instanceof ISubstitutionNode) {
			String todo;
		} else {
			throw new IllegalArgumentException("Don't know how to handle an "
					+ ITemplateNode.class + " as: " + node);
		}

	}
}
