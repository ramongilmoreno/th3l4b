package com.th3l4b.srm.codegen.sample.junit;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.srm.json.Generator;
import com.th3l4b.srm.json.IJsonConstants;
import com.th3l4b.srm.json.IJsonModelRuntime;
import com.th3l4b.srm.json.JsonUtils;
import com.th3l4b.srm.json.Parser;
import com.th3l4b.srm.json.Parser.Result;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.entities.IJSONTest;
import com.th3l4b.srm.sample.base.generated.entities.impl.DefaultJSONTest;

public class SampleJsonTest implements IJsonConstants {

	@Test
	public void simpliestTest() throws Exception {
		IJsonModelRuntime jmr = JsonUtils.runtime(SampleModelUtils.RUNTIME);
		String s = "{ \"" + FIELD_TYPE + "\": \""
				+ SampleModelUtils.ENTITY_JSONTest + "\", \"" + FIELD_ID
				+ "\": \"id\", \"" + FIELD_STATUS + "\": \""
				+ EntityStatus.ToMerge.name() + "\", \"" + FIELD_FIELDS
				+ "\": { \"" + SampleModelUtils.FIELD_JSONTest_Field1
				+ "\": \"a\", \"" + SampleModelUtils.FIELD_JSONTest_Field2
				+ "\": \"b\" } }";
		StringReader sr = new StringReader(s);
		Parser parser = new Parser(jmr, sr);
		Parser.Result result = parser.parse(true, false);
		IInstance r = result._one;
		Assert.assertNotNull(r);

		IJSONTest jt = (IJSONTest) r;
		Assert.assertEquals("id", jt.coordinates().getIdentifier().getKey());
		Assert.assertEquals(EntityStatus.ToMerge, jt.coordinates().getStatus());
		Assert.assertEquals("a", jt.getField1());
		Assert.assertEquals("b", jt.getField2());
	}

	@Test
	public void arrayTest() throws Exception {
		ArrayList<IInstance> r = new ArrayList<IInstance>();
		String f1 = "Some value of field 1";
		String f2 = "Some value of field 2";
		{
			DefaultJSONTest jt = new DefaultJSONTest();
			jt.coordinates().setStatus(EntityStatus.ToMerge);
			jt.setField1(f1);
			r.add(jt);
		}
		{
			DefaultJSONTest jt = new DefaultJSONTest();
			jt.coordinates().setStatus(EntityStatus.ToDelete);
			jt.setField2(f2);
			r.add(jt);
		}
		{
			DefaultJSONTest jt = new DefaultJSONTest();
			jt.coordinates().setStatus(EntityStatus.ToSave);
			jt.setField1(f1);
			jt.setField2(f2);
			r.add(jt);
		}
		{
			DefaultJSONTest jt = new DefaultJSONTest();
			jt.coordinates().setStatus(EntityStatus.Unknown);
			r.add(jt);
		}
		IJsonModelRuntime jmr = JsonUtils.runtime(SampleModelUtils.RUNTIME);
		StringWriter out = new StringWriter();
		Generator generator = new Generator(jmr, out);
		generator.write(r);
		generator.close();

		StringReader input = new StringReader(out.getBuffer().toString());
		Parser parser = new Parser(jmr, input);
		Result result = parser.parse(false, true);
		Collection<IInstance> many = result._many;
		Assert.assertNotNull(many);
		Assert.assertEquals(r.size(), many.size());

		int i = 0;
		for (IInstance j : many) {
			IJSONTest a = (IJSONTest) j;
			IJSONTest b = (IJSONTest) r.get(i++);
			Assert.assertTrue(SampleModelUtils.compareJSONTest(a, b));
		}
	}

}
