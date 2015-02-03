package com.th3l4b.srm.codegen.template;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import com.th3l4b.common.text.ITextConstants;
import com.th3l4b.srm.codegen.template.description.DefaultNamesEntry;
import com.th3l4b.srm.codegen.template.description.DefaultTemplate;
import com.th3l4b.srm.codegen.template.description.ITemplate;
import com.th3l4b.srm.codegen.template.description.TemplateUnit;
import com.th3l4b.srm.codegen.template.description.nodes.DefaultCodeNode;
import com.th3l4b.srm.codegen.template.description.nodes.DefaultSubstitutionNode;
import com.th3l4b.srm.codegen.template.description.nodes.DefaultTextNode;
import com.th3l4b.srm.codegen.template.description.nodes.ITemplateNode;

public class TemplateParser {

	public static final int MAX_LINE = 10000;

	enum Status {
		AwaitingUnit, InUnit, AwaitingNames, InNames, AwaitingFilename, InFilename, AwaitingContent, InContent
	}

	private String readLine(Reader reader) throws Exception {
		StringBuilder sb = new StringBuilder();
		int c = -1;
		while ((c = reader.read()) != -1) {
			char read = (char) c;
			if ((read == '\n') || (read == '\r')) {
				return sb.toString();
			}
			sb.append(read);
			if (sb.length() >= MAX_LINE) {
				throw new Exception("Line too long: " + sb.toString());
			}
		}
		return null;
	}

	private void skipUntil(String line, String keyword, PushbackReader pbr)
			throws Exception {
		String key = "^[\\s]*" + keyword + "[\\s]*:[\\s]*";
		if (!line.matches(key + ".*")) {
			throw new Exception("Could not find keyword " + keyword
					+ ". Found instead: " + line);
		}
		line = line.replaceFirst(key, "") + "\n";
		pbr.unread(line.toCharArray());
	}

	private String readUntil(String keyword, PushbackReader pbr, String line)
			throws Exception {
		pbr.unread((line + "\n").toCharArray());

		StringBuilder sb = new StringBuilder();
		String l = null;
		while ((l = readLine(pbr)) != null) {
			String key = "^[\\s]*" + keyword + "[\\s]*:.*";
			if (l.matches(key)) {
				pbr.unread((l + "\n").toCharArray());
				return sb.toString().replaceAll("[\\s]+", " ").trim()
						.toString();
			}

			sb.append(l);
			sb.append('\n');
		}
		throw new IllegalStateException("Could not find keyword: " + keyword);
	}

	public ITemplate parse(String name, Reader reader) throws Exception {
		DefaultTemplate r = new DefaultTemplate();
		r.setTemplateName(name);

		Status status = Status.AwaitingUnit;

		PushbackReader pbr = new PushbackReader(reader, MAX_LINE);

		mainLoop: while (true) {
			String line = readLine(pbr);
			if (line == null) {
				throw new Exception("Unexpected end of file in status: "
						+ status.name());
			}

			if (line.matches("^[\\s]*#.*") || line.matches("[\\s]*")) {
				// Skip empty lines and comments
				continue;
			}

			switch (status) {
			case AwaitingContent:
				skipUntil(line, "content", pbr);
				status = Status.InContent;
				break mainLoop;
			case AwaitingFilename:
				skipUntil(line, "file", pbr);
				status = Status.InFilename;
				break;
			case AwaitingNames:
				skipUntil(line, "names", pbr);
				status = Status.InNames;
				break;
			case AwaitingUnit:
				skipUntil(line, "unit", pbr);
				status = Status.InUnit;
				break;
			case InContent: {
				throw new IllegalStateException();
			}
			case InFilename: {
				StringReader filename = new StringReader(readUntil("content",
						pbr, line));
				parseSpecial(filename, r.getFileName());
				status = Status.AwaitingContent;
				break;
			}
			case InNames: {
				String names = readUntil("file", pbr, line);
				String[] splits = names.split(",");
				for (String s : splits) {
					String[] parts = s.trim().split("[\\s]+");
					if (parts.length != 2) {
						throw new Exception("Cannot get names from input: "
								+ names);
					}
					String clazz = parts[0];
					String n = parts[1];
					r.getNames().add(new DefaultNamesEntry(clazz, n));
				}
				status = Status.AwaitingFilename;
				break;
			}
			case InUnit: {
				String unit = readUntil("names", pbr, line);
				unit = unit.trim();
				r.setTemplateUnit(Enum.valueOf(TemplateUnit.class, unit));
				status = Status.AwaitingNames;
				break;
			}

			default:
				throw new IllegalStateException(status.name());
			}
		}

		// Skip spaces
		int c = -1;
		while ((c = pbr.read()) != -1) {
			if (Character.isWhitespace((char) c)) {
				continue;
			} else {
				pbr.unread(c);
				break;
			}
		}
		parseSpecial(pbr, r.getContent());

		// Return composed result
		return r;
	}

	private void parseSpecial(Reader reader, List<ITemplateNode> nodes)
			throws Exception {
		PushbackReader pbr = new PushbackReader(reader, 1);
		parseText(pbr, nodes);
	}

	private void parseText(PushbackReader reader, List<ITemplateNode> nodes)
			throws Exception {
		int c = -1;
		StringBuilder sb = new StringBuilder();
		while ((c = reader.read()) != -1) {
			if (c == '<') {
				int c2 = reader.read();
				if (c2 == '%') {
					// Do not include unnecessary empty text nodes
					if (sb.length() != 0) {
						DefaultTextNode n = new DefaultTextNode(sb.toString());
						sb.setLength(0);
						nodes.add(n);
					}
					parseEscape(reader, nodes);
					continue;
				} else {
					reader.unread(c2);
				}
			}
			sb.append((char) c);
		}
		DefaultTextNode n = new DefaultTextNode(sb.toString());
		nodes.add(n);
	}

	private void parseEscape(PushbackReader reader, List<ITemplateNode> nodes)
			throws Exception {
		boolean isSubstitution = false;
		int c = -1;
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		while ((c = reader.read()) != -1) {
			if (first && (c == '=')) {
				isSubstitution = true;
				first = false;
				continue;
			}
			first = false;
			if (c == '%') {
				int c2 = reader.read();
				if (c2 == '>') {
					ITemplateNode r = null;
					String text = sb.toString();
					text = text.replaceAll("[\r\n\t ]+", " ").trim();
					if (isSubstitution) {
						r = new DefaultSubstitutionNode(text);
					} else {
						r = new DefaultCodeNode(text);
					}
					nodes.add(r);
					return;
				} else {
					reader.unread('%');
				}
			} else {
				sb.append((char) c);
			}
		}
		throw new IllegalStateException(
				"Unexpected end of file parsing <% ... %> contents");
	}

	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream(args[0]);
		try {
			InputStreamReader isr = new InputStreamReader(fis,
					ITextConstants.UTF_8);
			try {
				ITemplate template = new TemplateParser().parse("input", isr);
				PrintWriter out = new PrintWriter(System.out, true);
				DefaultTemplate.print(template, out);
				out.println();
			} finally {
				isr.close();
			}
		} finally {
			fis.close();
		}
	}
}
