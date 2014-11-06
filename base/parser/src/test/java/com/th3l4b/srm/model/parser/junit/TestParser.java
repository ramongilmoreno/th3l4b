package com.th3l4b.srm.model.parser.junit;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import com.th3l4b.common.text.ITextConstants;
import com.th3l4b.srm.model.base.DefaultModel;
import com.th3l4b.srm.model.parser.generated.ModelLexer;
import com.th3l4b.srm.model.parser.generated.ModelParser;
import com.th3l4b.srm.model.utils.PrintModel;

public class TestParser {
	@Test
	public void test() throws Exception {
		InputStream is = getClass().getResourceAsStream(
				getClass().getSimpleName() + ".srm");
		try {
			InputStreamReader isr = new InputStreamReader(is, ITextConstants.UTF_8);
			try {
				StringWriter sw = new StringWriter();
				int r = -1;
				while ((r = isr.read()) != -1) {
					sw.write(r);
				}
				
				// http://www.theendian.com/blog/antlr-4-lexer-parser-and-listener-with-example-grammar/
				ANTLRInputStream ais = new ANTLRInputStream(sw.getBuffer().toString());
				ModelLexer lexer = new ModelLexer(ais);
				CommonTokenStream ts = new CommonTokenStream(lexer);
				ModelParser parser = new ModelParser(ts);
				
				DefaultModel model = new DefaultModel();
				parser.document(model);
				
				PrintWriter out = new PrintWriter(System.out, true);
				PrintModel.print(model, out);
				out.flush();
			} finally {
				isr.close();
			}
		} finally {
			is.close();
		}
	}
}
