package com.th3l4b.common.text;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class IndentedWriter extends FilterWriter {

	public static PrintWriter get(Writer writer) {
		return new PrintWriter(new IndentedWriter(writer), true);
	}

	public static PrintWriter get(Writer writer, String indent) {
		return new PrintWriter(new IndentedWriter(writer, indent), true);
	}

	public static final String DEFAULT_INDENTATION = "    ";

	protected boolean _indentationPending = true;
	protected char[] _indentation;

	public IndentedWriter(Writer out) {
		this(out, DEFAULT_INDENTATION);
	}

	public IndentedWriter(Writer out, String indentation) {
		super(out);
		_indentation = indentation.toCharArray();
		reset();
	}

	protected void reset() {
		_indentationPending = true;
	}

	protected boolean isNewLine(int c) {
		return (c == '\r') || (c == '\n');
	}

	@Override
	public void close() throws IOException {
		super.close();
		reset();
	}

	@Override
	public void flush() throws IOException {
		super.flush();
	}

	protected char[] handle(char[] input, int off, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = off; i < (off + len); i++) {
			char c = input[i];
			if (isNewLine(c)) {
				reset();
			} else {
				if (_indentationPending) {
					sb.append(_indentation);
					_indentationPending = false;
				}
			}
			sb.append(c);
		}
		return sb.toString().toCharArray();

	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		char[] r = handle(cbuf, off, len);
		super.write(r, 0, r.length);
	}

	@Override
	public void write(int c) throws IOException {
		if (isNewLine(c)) {
			super.write(c);
			reset();
		} else {
			// Not new line, check if indentation needed.
			if (_indentationPending) {
				for (char i : _indentation)
					super.write(i);
				_indentationPending = false;
			}
			super.write(c);
		}

	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		String r = new String(handle(str.toCharArray(), off, len));
		super.write(r, 0, r.length());
	}
}