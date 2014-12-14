package com.th3l4b.srm.codegen.template.runtime;

import java.io.File;
import java.util.Map;

import com.th3l4b.srm.model.base.IModel;

public interface ITemplateGenerator {
	void produce(IModel model, Map<String, String> properties, File outputDir)
			throws Exception;
}
