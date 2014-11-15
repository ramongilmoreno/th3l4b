package com.th3l4b.srm.codegen.template.junit;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.junit.Test;

import com.th3l4b.common.text.ITextConstants;
import com.th3l4b.srm.codegen.template.description.DefaultTemplate;
import com.th3l4b.srm.codegen.template.description.ITemplate;
import com.th3l4b.srm.codegen.template.parser.TemplateParser;

public class TestTemplateParser {
	@Test
	public void test() throws Exception {
		InputStream is = getClass().getResourceAsStream("SampleTemplate.srmt");
		try {
			InputStreamReader isr = new InputStreamReader(is,
					ITextConstants.UTF_8);
			ITemplate template = new TemplateParser().parse(isr);
			PrintWriter out2 = new PrintWriter(System.out, true);
			DefaultTemplate.print(template, out2);
			out2.flush();
		} finally {
			if (is != null) {
				is.close();
			}
		}

	}
}
