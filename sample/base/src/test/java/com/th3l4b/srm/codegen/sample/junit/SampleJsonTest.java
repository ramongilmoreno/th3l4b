package com.th3l4b.srm.codegen.sample.junit;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import com.th3l4b.srm.json.IJsonConstants;
import com.th3l4b.srm.json.IJsonModelRuntime;
import com.th3l4b.srm.json.JsonUtils;
import com.th3l4b.srm.json.Parser;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.entities.IJSONTest;

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

}
