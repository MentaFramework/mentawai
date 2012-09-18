package org.mentawai.rule;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mentawai.core.Action;
import org.mentawai.util.MockAction;

public class DependentFieldValueRuleTest {

	DependentFieldValueRule rule;
	Action action;
	
	@Before
	public void setup() {
		rule = DependentFieldValueRule.getInstance("type", "x", "field2");
		action = new MockAction();
	}
	
	@Test
	public void testCheckDoesNotExist() {
		// True because field type does not exists
		Assert.assertTrue( rule.check("type", action) );
	}
	
	@Test
	public void testCheckNotPass() {
		action.getInput().setValue("type", "x");
		// False because value of field1 is equal x but field2 is not present
		Assert.assertFalse( rule.check("type", action) );
	}
	
	@Test
	public void testCheckPass() {
		action.getInput().setValue("type", "x");
		action.getInput().setValue("field2", "f2value");
		// True because value of field1 is equal x and field2 is present
		Assert.assertTrue( rule.check("type", action) );
	}
	
	@Test
	public void testCheckField1DoesNotExists() {
		action.getInput().setValue("type", "x");
		action.getInput().setValue("type", "y");
		// True because value of field type is not x
		Assert.assertTrue( rule.check("type", action) );
	}

}
