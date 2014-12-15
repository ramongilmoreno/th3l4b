package com.th3l4b.srm.model.parser.junit;

import java.io.InputStream;
import java.io.PrintWriter;

import org.junit.Test;

import com.th3l4b.srm.base.parser.Parser;
import com.th3l4b.srm.model.base.IModel;
import com.th3l4b.srm.model.utils.PrintModel;

public class TestParser {
	@Test
	public void test() throws Exception {
		InputStream is = getClass().getResourceAsStream(
				getClass().getSimpleName() + ".srm");
		try {
			IModel model = Parser.parse(is);
			PrintWriter out = new PrintWriter(System.out, true);
			PrintModel.print(model, out);
			out.flush();
		} finally {
			is.close();
		}
	}
}
