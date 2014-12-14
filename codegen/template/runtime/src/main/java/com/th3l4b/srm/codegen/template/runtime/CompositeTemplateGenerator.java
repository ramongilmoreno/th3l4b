package com.th3l4b.srm.codegen.template.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.th3l4b.srm.model.base.IModel;

public class CompositeTemplateGenerator implements ITemplateGenerator {

	private List<ITemplateGenerator> _generators = new ArrayList<ITemplateGenerator>();

	public void add(ITemplateGenerator generator) throws Exception {
		_generators.add(generator);
	}

	@Override
	public void produce(IModel model, Map<String, String> properties,
			File outputDir) throws Exception {
		for (ITemplateGenerator g : _generators) {
			g.produce(model, properties, outputDir);
		}
	}
}
