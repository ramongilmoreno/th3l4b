package com.th3l4b.srm.codegen.java.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import com.th3l4b.srm.base.parser.Parser;
import com.th3l4b.srm.codegen.java.generated.All;
import com.th3l4b.srm.model.base.IModel;
import com.th3l4b.srm.model.runtime.codegen.CodegenUtils;

public class JavaTask extends Task {

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
			LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
			properties.put(CodegenUtils.PACKAGE_NAME, getPackageName());

			All all = new All();
			for (FileSet fs : _input) {
				DirectoryScanner ds = fs.getDirectoryScanner(getProject());
				for (String fn : ds.getIncludedFiles()) {
					File f = new File(ds.getBasedir(), fn);
					l("Processing model file: " + f.getAbsolutePath());
					IModel model = Parser.parse(f);
					all.produce(model, properties, getTodir());
				}
			}
			l("Finished");
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
