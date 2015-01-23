package com.th3l4b.srm.base.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.th3l4b.common.text.ITextConstants;
import com.th3l4b.srm.model.base.DefaultModel;
import com.th3l4b.srm.model.base.IModel;
import com.th3l4b.srm.model.parser.generated.ModelLexer;
import com.th3l4b.srm.model.parser.generated.ModelParser;

/**
 * Utilities for model parsing
 */
public class Parser {
	
	public static IModel parse(File input) throws Exception {
		FileInputStream is = new FileInputStream(input);
		try {
			return parse(is);
		} finally {
			is.close();
		}
	}

	public static IModel parse(InputStream is)
			throws Exception {
		InputStreamReader isr = new InputStreamReader(is,
				ITextConstants.UTF_8);
		try {
			// http://www.theendian.com/blog/antlr-4-lexer-parser-and-listener-with-example-grammar/
			ANTLRInputStream ais = new ANTLRInputStream(is);
			ModelLexer lexer = new ModelLexer(ais);
			CommonTokenStream ts = new CommonTokenStream(lexer);
			ModelParser parser = new ModelParser(ts);

			DefaultModel model = new DefaultModel();
			parser.document(model);
			ModelTester.test(model);
			return model;

		} finally {
			isr.close();
		}
	}
}
