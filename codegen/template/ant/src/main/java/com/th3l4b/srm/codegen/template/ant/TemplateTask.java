package com.th3l4b.srm.codegen.template.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import com.th3l4b.common.text.IPrintable;
import com.th3l4b.common.text.ITextConstants;
import com.th3l4b.common.text.codegen.TextUtils;
import com.th3l4b.srm.codegen.template.CompositeTemplateGeneratorGenerator;
import com.th3l4b.srm.codegen.template.TemplateGeneratorGenerator;
import com.th3l4b.srm.codegen.template.TemplateParser;
import com.th3l4b.srm.codegen.template.description.ITemplate;
import com.th3l4b.srm.model.runtime.codegen.CodegenUtils;

public class TemplateTask extends Task {

	List<FileSet> _input = new ArrayList<FileSet>();
	File _todir;
	String _packageName;

	public void l(String msg) {
		log(msg, Project.MSG_ERR);
	}

	/**
	 * @see http://www.developer.com/lang/article.php/
	 *      10924_3636196_2/More-on-Custom-Ant-Tasks.htm
	 */
	public void addFileset(FileSet fileSet) {
		_input.add(fileSet);
	}

	public void addConfiguredFileset(FileSet fileSet) {
		_input.add(fileSet);
	}

	public FileSet createFileset() {
		return new FileSet();
	}

	public File getTodir() {
		return _todir;
	}

	public void setTodir(File todir) {
		_todir = todir;
	}

	public String getPackageName() {
		return _packageName;
	}

	public void setPackageName(String packageName) {
		_packageName = packageName;
	}

	@Override
	public void execute() throws BuildException {
		try {
			// Ensure target directory exists
			final String pkg = getPackageName();
			File td = new File(getTodir(), CodegenUtils.pkgToDir(pkg));
			if (!td.exists()) {
				if (!td.mkdirs()) {
					throw new IllegalStateException(
							"Could not create target directory: "
									+ td.getAbsolutePath());
				}
			}

			final ArrayList<ITemplate> all = new ArrayList<ITemplate>();

			for (FileSet fs : _input) {
				DirectoryScanner ds = fs.getDirectoryScanner(getProject());
				for (String fn : ds.getIncludedFiles()) {
					File f = new File(ds.getBasedir(), fn);
					l("Processing template file: " + f.getAbsolutePath());
					String name = f.getName().replaceAll("\\.srmt$", "");

					// Parse template
					ITemplate template = null;
					FileInputStream fis = new FileInputStream(f);
					try {
						InputStreamReader isr = new InputStreamReader(fis,
								ITextConstants.UTF_8);
						try {
							template = new TemplateParser().parse(name, isr);
						} finally {
							isr.close();
						}

					} finally {
						if (fis != null) {
							fis.close();
						}
					}
					all.add(template);

					// Generate template class
					final TemplateGeneratorGenerator tgg = new TemplateGeneratorGenerator();
					final ITemplate ftemplate = template;
					File t = new File(td, name + ".java");
					l("Producing file: " + t.getAbsolutePath());
					TextUtils.print(t, new IPrintable() {
						@Override
						public void print(PrintWriter out) throws Exception {
							tgg.content(ftemplate, pkg, out);

						}
					});

				}
				// Generate all templates class
				File f = new File(td,
						CompositeTemplateGeneratorGenerator.CLASS_NAME
								+ ".java");
				l("Producing all templates file: " + f.getAbsolutePath());
				TextUtils.print(f, new IPrintable() {
					@Override
					public void print(PrintWriter out) throws Exception {
						new CompositeTemplateGeneratorGenerator().content(all,
								pkg, out);
					}
				});
			}
			l("Finished");
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
