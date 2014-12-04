package com.th3l4b.common.text.codegen;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Simple PrintWriter for strings.
 */
public class StringPrintWriter extends PrintWriter {

	public StringPrintWriter() {
		this(new StringWriter());
	}

	public StringPrintWriter(int size) {
		this(new StringWriter(size));
	}

	public StringPrintWriter(StringWriter out) {
		super(out);
	}

	public StringBuffer getBuffer() {
		return getStringWriter().getBuffer();
	}

	public StringWriter getStringWriter() {
		return (StringWriter) out;
	}

	@Override
	public String toString() {
		return getBuffer().toString();
	}

	@Override
	public void close() {
		// Keep buffer in out attribute after closing (super.close() sets this
		// to null).
		StringWriter writer = getStringWriter();
		super.close();
		out = writer;
	}

}
