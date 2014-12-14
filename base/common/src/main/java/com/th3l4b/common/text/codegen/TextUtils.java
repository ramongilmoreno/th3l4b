package com.th3l4b.common.text.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.Normalizer;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.th3l4b.common.text.IPrintable;
import com.th3l4b.common.text.ITextConstants;

public class TextUtils {

	public static boolean empty(String s) {
		return s == null || s.length() == 0;
	}

	public static Iterable<Integer> unicodeIterable(String input) {
		return unicodeIterable(new StringReader(input));
	}

	/**
	 * Facility to code for() loops of characters from iterator. You can only
	 * invoke {@link Iterable#iterator()} once or an
	 * {@link IllegalArgumentException} witll be thrown.
	 */
	public static Iterable<Integer> unicodeIterable(final Reader input) {
		return new Iterable<Integer>() {

			boolean _first = true;

			@Override
			public Iterator<Integer> iterator() {
				if (_first) {
					_first = false;
					return unicodeIterator(input);
				} else {
					throw new IllegalStateException(
							"Called twice for the iterator. Only once supported.");
				}
			}
		};
	}

	public static Iterator<Integer> unicodeIterator(String input) {
		return unicodeIterator(new StringReader(input));
	}

	/**
	 * Iterates an input reader by unicode characters.
	 */
	public static Iterator<Integer> unicodeIterator(final Reader input) {
		return new Iterator<Integer>() {

			// A -2 means that the input reader shall be inspected to find new
			// characters.
			// A -1 means that the EOF of the reader was found.
			int _current = -2;

			@Override
			public boolean hasNext() {
				if (_current == -1) {
					// Detect the -1 value as EOF
					return false;
				} else if (_current >= 0) {
					// Found a value for which next() has not been called yet.
					return true;
				} else {
					// Proceed
					try {
						int c = input.read();
						if (c != -1) {
							// Check surrogates
							char ch = (char) c;
							if (Character.isHighSurrogate(ch)) {
								char low = (char) input.read();
								if (!Character.isLowSurrogate(low)) {
									throw new IllegalArgumentException(
											"Found low surrogate without high. High: "
													+ ch + ", candidate low: "
													+ low);
								}
								if (!Character.isSurrogatePair(ch, low)) {
									throw new IllegalArgumentException(
											"Candidate surrogates were not surrogates: "
													+ ch + ", candidate low: "
													+ low);
								}
								c = Character.toCodePoint(ch, low);
							}
						}
						_current = c;
						return _current >= 0;

					} catch (IOException ioe) {
						throw new RuntimeException(ioe);
					}
				}
			}

			@Override
			public Integer next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				int r = _current;
				// Next character needs to be read from input.
				_current = -2;

				return new Integer(r);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	public static void write(int codePoint, Writer out) throws IOException {
		if (Character.isSupplementaryCodePoint(codePoint)) {
			out.write(Character.toChars(codePoint));
		} else {
			out.write(codePoint);
		}
	}

	public static void print(int codePoint, PrintWriter out) {
		if (Character.isSupplementaryCodePoint(codePoint)) {
			out.write(Character.toChars(codePoint));
		} else {
			out.write(codePoint);
		}
	}

	/**
	 * Get an ASCII representation of the input string.
	 */
	public static String ASCII(String input) {
		// http://stackoverflow.com/questions/3322152/java-getting-rid-of-accents-and-converting-them-to-regular-letters
		return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll(
				"[^\\p{ASCII}]", "");
	}

	/**
	 * Computes a C identifier by limiting the characters to letters and
	 * numbers. If resulting identifier starts with a number (invalid as per C
	 * specification), the XX prefix is added.
	 */
	public static String cIdentifier(String input) {
		String r = ASCII(input).replaceAll("[^A-Za-z0-9]", " ");
		r = CamelUtils.toCamelCase(r);
		if (r.matches("\\d.*")) {
			r = "XX" + r;
		}
		return r;
	}

	public static String escapeJavaString(String src) throws Exception {
		StringWriter out = new StringWriter(src.length());
		escapeJavaString(src, out);
		return out.getBuffer().toString();
	}

	public static void escapeJavaString(String src, Writer out)
			throws Exception {
		if (src == null) {
			src = "";
		}
		StringReader in = new StringReader(src);
		escapeJava(in, out);
	}

	public static void escapeJava(Reader in, Writer out) throws Exception {
		int c;
		while ((c = in.read()) != -1) {
			if (c < ' ' || c > 127 || c == '\\' || c == '\"') {
				out.write("\\u");
				String s = Integer.toHexString((int) c);
				for (int j = s.length(); j < 4; j++) {
					out.write('0');
				}
				out.write(s);
			} else {
				out.write(c);
			}
		}
	}

	/**
	 * Computes an String which is a limit unicode characters long as much from
	 * the original.
	 */
	public static String limit(String input, int limit) throws Exception {
		if (input == null) {
			return null;
		}

		int count = 0;
		StringWriter out = new StringWriter();
		for (Integer i : unicodeIterable(input)) {
			if (++count > limit) {
				break;
			}
			write(i.intValue(), out);
		}
		return out.toString();
	}

	public static void print(File f, IPrintable p) throws Exception {
		FileOutputStream fos = new FileOutputStream(f);
		try {
			OutputStreamWriter osw = new OutputStreamWriter(fos,
					ITextConstants.UTF_8);
			try {
				PrintWriter pw = new PrintWriter(osw);
				p.print(pw);
				pw.close();
				if (pw.checkError()) {
					throw new IOException("Could not produce file: "
							+ f.getAbsolutePath());
				}

			} finally {
				osw.close();
			}

		} finally {
			fos.close();
		}

	}
}
