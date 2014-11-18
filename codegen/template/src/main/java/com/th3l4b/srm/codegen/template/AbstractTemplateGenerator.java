package com.th3l4b.srm.codegen.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.th3l4b.common.text.ITextConstants;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IModel;

public abstract class AbstractTemplateGenerator {

	public static abstract class Model extends AbstractTemplateGenerator {

		protected abstract void file(IModel model, PrintWriter out)
				throws Exception;

		protected abstract void content(IModel model, PrintWriter out)
				throws Exception;

		@Override
		public void internalProduce(IModel model, File outputDir)
				throws Exception {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			file(model, pw);
			pw.flush();
			sw.flush();
			File target = new File(outputDir, sw.getBuffer().toString());
			FileOutputStream fos = new FileOutputStream(target);
			try {
				OutputStreamWriter osw = new OutputStreamWriter(fos,
						ITextConstants.UTF_8);
				try {
					PrintWriter out = new PrintWriter(osw);
					content(model, out);
					out.flush();
					if (out.checkError()) {
						throw new Exception("Could not write to file: "
								+ target.getAbsolutePath());
					}

				} finally {
					osw.close();
				}

			} finally {
				fos.close();
			}

		}
	}

	public static abstract class Entity extends AbstractTemplateGenerator {

		protected abstract void file(IEntity entity, IModel model,
				PrintWriter out) throws Exception;

		protected abstract void content(IEntity entity, IModel model,
				PrintWriter out) throws Exception;

		@Override
		public void internalProduce(IModel model, File outputDir)
				throws Exception {

			for (IEntity entity : model) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				file(entity, model, pw);
				pw.flush();
				sw.flush();
				File target = new File(outputDir, sw.getBuffer().toString());
				FileOutputStream fos = new FileOutputStream(target);
				try {
					OutputStreamWriter osw = new OutputStreamWriter(fos,
							ITextConstants.UTF_8);
					try {
						PrintWriter out = new PrintWriter(osw);
						content(entity, model, out);
						out.flush();
						if (out.checkError()) {
							throw new Exception("Could not write to file: "
									+ target.getAbsolutePath());
						}

					} finally {
						osw.close();
					}

				} finally {
					fos.close();
				}

			}
		}
	}

	public void produce(IModel model, File outputDir) throws Exception {
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				throw new Exception("Failed to create directory: "
						+ outputDir.getAbsolutePath());
			}
		}

		if (!outputDir.isDirectory()) {
			throw new IllegalArgumentException("Not a directory: "
					+ outputDir.getAbsolutePath());
		}

		internalProduce(model, outputDir);
	}

	public abstract void internalProduce(IModel model, File outputDir)
			throws Exception;

}
