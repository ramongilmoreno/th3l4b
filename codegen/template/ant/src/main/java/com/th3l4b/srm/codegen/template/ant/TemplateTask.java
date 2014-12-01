package com.th3l4b.srm.codegen.template.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

public class TemplateTask extends Task {

	List<FileSet> _input = new ArrayList<FileSet>();
	File _todir;
	String _packageName;

	/**
	 * @see http://www.developer.com/lang/article.php/
	 *      10924_3636196_2/More-on-Custom-Ant-Tasks.htm
	 */
	public void addFileSet(FileSet fileSet) {
		log("addFileSet: " + fileSet.toString(), Project.MSG_ERR);
		_input.add(fileSet);
	}

	public File getTodir() {
		return _todir;
	}

	public void setTodir(File todir) {
		log("setToDir: " + todir.getAbsolutePath(), Project.MSG_ERR);
		_todir = todir;
	}

	public String getPackageName() {
		return _packageName;
	}

	public void setPackageName(String packageName) {
		log("setPackageName: " + packageName, Project.MSG_ERR);
		_packageName = packageName;
	}

	@Override
	public void execute() throws BuildException {
		log("START", Project.MSG_ERR);
		for (FileSet fs : _input) {
			log("FILESET: " + fs);
			DirectoryScanner ds = fs.getDirectoryScanner(getProject());
			for (String fn : ds.getIncludedFiles()) {
				log("FILENAME: " + fn);
			}
		}
		log("END", Project.MSG_ERR);
	}
}
