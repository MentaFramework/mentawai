package org.mentawai.cript;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MentaCriptTest {
	
	MentaCript mentaCript;
	
	@Before
	public void setUp() throws Exception {
		mentaCript = MentaCript.getCommonInstance();
	}

	@Test
	public void testCript() {
		
		Assert.assertEquals( "49kRw4I/LWs=", mentaCript.cript("") );
		Assert.assertEquals( "8PlvDGLiXak=", mentaCript.cript("1") );
		Assert.assertEquals( "8PlvDGLiXak=", mentaCript.cript(1) );
		
		Assert.assertEquals( "DtdvnYls4L4=", mentaCript.cript("123") );
		Assert.assertEquals( "DtdvnYls4L4=", mentaCript.cript(123) );
		
		Assert.assertEquals( "4E1vnZ0eKNc=", mentaCript.cript("Teste") );
		Assert.assertEquals( "hnIsr8YIMxiWrXVGQbaMNg==", mentaCript.cript("Robert Gil") );
	}

	@Test
	public void testDecript() throws DecriptException {
		Assert.assertEquals("1", mentaCript.decript("8PlvDGLiXak="));
		Assert.assertEquals("123", mentaCript.decript("DtdvnYls4L4="));
		Assert.assertEquals("Teste", mentaCript.decript("4E1vnZ0eKNc="));
		Assert.assertEquals("Robert Gil", mentaCript.decript("hnIsr8YIMxiWrXVGQbaMNg=="));
		Assert.assertEquals("", mentaCript.decript("") );
		Assert.assertNull(mentaCript.decript(null) );
	}

	@Test(expected=DecriptException.class)
	public void testDecriptException() throws DecriptException {
		// will throw exception
		mentaCript.decript("8PlvDGLiXak_=");
	}
	
	@Test(expected=DecriptException.class)
	public void testDecriptExceptionNumber() throws DecriptException {
		// will throw exception
		mentaCript.decript("1");
	}
	
	@Test(expected=DecriptException.class)
	public void testDecriptExceptionText() throws DecriptException {
		// will throw exception
		mentaCript.decript("error");
	}
	
	@Test
	public void testNull() throws DecriptException {
		String s = null;
		Integer i = null;
		Assert.assertNull( mentaCript.cript( s ) );
		Assert.assertNull( mentaCript.cript( i ) );
		Assert.assertNull( mentaCript.decript(s) );
	}
	

}
