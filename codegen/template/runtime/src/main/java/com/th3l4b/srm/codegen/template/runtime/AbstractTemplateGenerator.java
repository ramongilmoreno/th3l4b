package com.th3l4b.srm.codegen.template.runtime;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.th3l4b.common.text.IPrintable;
import com.th3l4b.common.text.codegen.TextUtils;
import com.th3l4b.srm.model.base.IEntity;
import com.th3l4b.srm.model.base.IModel;

public abstract class AbstractTemplateGenerator implements ITemplateGenerator {

	public static abstract class Model extends AbstractTemplateGenerator {

		protected abstract void file(IModel model,
				Map<String, String> properties, PrintWriter out)
				throws Exception;

		protected abstract void content(IModel model,
				Map<String, String> properties, PrintWriter out)
				throws Exception;

		@Override
		public void internalProduce(final IModel model,
				final Map<String, String> properties, File outputDir)
				throws Exception {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			file(model, properties, pw);
			pw.flush();
			sw.flush();
			File target = new File(outputDir, sw.getBuffer().toString());
			TextUtils.print(target, new IPrintable() {
				@Override
				public void print(PrintWriter out) throws Exception {
					content(model, properties, out);
				}

			});
		}
	}

	public static abstract class Entity extends AbstractTemplateGenerator {

		protected abstract void file(IEntity entity, IModel model,
				Map<String, String> properties, PrintWriter out)
				throws Exception;

		protected abstract void content(IEntity entity, IModel model,
				Map<String, String> properties, PrintWriter out)
				throws Exception;

		@Override
		public void internalProduce(final IModel model,
				final Map<String, String> properties, File outputDir)
				throws Exception {

			for (IEntity entity : model) {
				final IEntity fentity = entity;
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				file(entity, model, properties, pw);
				pw.flush();
				sw.flush();
				File target = new File(outputDir, sw.getBuffer().toString());
				TextUtils.print(target, new IPrintable() {
					@Override
					public void print(PrintWriter out) throws Exception {
						content(fentity, model, properties, out);
					}
				});
			}
		}
	}

	public void produce(IModel model, Map<String, String> properties,
			File outputDir) throws Exception {
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

		internalProduce(model, properties, outputDir);
	}

	public abstract void internalProduce(final IModel model,
			final Map<String, String> properties, File outputDir)
			throws Exception;
}
