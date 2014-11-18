package com.th3l4b.srm.codegen.template.parser;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;

import com.th3l4b.srm.codegen.template.description.DefaultNamesEntry;
import com.th3l4b.srm.codegen.template.description.DefaultTemplate;
import com.th3l4b.srm.codegen.template.description.ITemplate;
import com.th3l4b.srm.codegen.template.description.TemplateUnit;
import com.th3l4b.srm.codegen.template.description.tree.DefaultIterationNode;
import com.th3l4b.srm.codegen.template.description.tree.DefaultSubstitutionNode;
import com.th3l4b.srm.codegen.template.description.tree.DefaultTextNode;
import com.th3l4b.srm.codegen.template.description.tree.ITemplateNode;
import com.th3l4b.srm.codegen.template.description.tree.IterationType;

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
				parseSpecial(filename, r.getFileNameRoot());
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
		parseSpecial(pbr, r.getContentRoot());

		// Return composed result
		return r;
	}

	public void parseSpecial(Reader reader, ITemplateNode node)
			throws Exception {
		PushbackReader pbr = new PushbackReader(reader, 1);
		Stack<ITemplateNode> stack = new Stack<ITemplateNode>();
		stack.push(node);
		parseText(pbr, stack);
	}

	private void parseText(PushbackReader reader, Stack<ITemplateNode> parents)
			throws Exception {
		int c = -1;
		StringBuilder sb = new StringBuilder();
		while ((c = reader.read()) != -1) {
			if (c == '<') {
				int c2 = reader.read();
				if (c2 == '%') {
					DefaultTextNode n = new DefaultTextNode(sb.toString());
					parents.peek().children().add(n);
					sb.setLength(0);
					parseEscape(reader, parents);
				} else {
					reader.unread(c2);
				}
			} else {
				sb.append((char) c);
			}
		}
		DefaultTextNode n = new DefaultTextNode(sb.toString());
		parents.peek().children().add(n);
	}

	private void parseEscape(PushbackReader reader, Stack<ITemplateNode> parents)
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
						parents.peek().children().add(r);
					} else if (text.startsWith("iterate")) {
						text = text.substring("iterate".length()).trim();
						String[] split = text.split(" ");
						if (split.length != 2) {
							throw new IllegalArgumentException(
									"Cannot parse iteration: " + text);
						}
						r = new DefaultIterationNode(Enum.valueOf(
								IterationType.class, split[0]), split[1]);
						parents.peek().children().add(r);
						parents.push(r);
						parseText(reader, parents);
					} else if (text.equals("end iterate")) {
						parents.pop();
						parseText(reader, parents);
					} else {
						throw new IllegalArgumentException(
								"Unknown input in scape: " + sb.toString());
					}
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
}
