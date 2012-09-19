package org.mentawai.cript;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CriptMD5Test {
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCript() {
		
		Assert.assertEquals( "49kRw4I/LWs=", MentaCript.cript("") );
		Assert.assertEquals( "8PlvDGLiXak=", MentaCript.cript("1") );
		Assert.assertEquals( "8PlvDGLiXak=", MentaCript.cript(1) );
		
		Assert.assertEquals( "DtdvnYls4L4=", MentaCript.cript("123") );
		Assert.assertEquals( "DtdvnYls4L4=", MentaCript.cript(123) );
		
		Assert.assertEquals( "4E1vnZ0eKNc=", MentaCript.cript("Teste") );
		Assert.assertEquals( "hnIsr8YIMxiWrXVGQbaMNg==", MentaCript.cript("Robert Gil") );
	}

	@Test
	public void testDecript() throws DecriptException {
		Assert.assertEquals("1", MentaCript.decript("8PlvDGLiXak="));
		Assert.assertEquals("123", MentaCript.decript("DtdvnYls4L4="));
		Assert.assertEquals("Teste", MentaCript.decript("4E1vnZ0eKNc="));
		Assert.assertEquals("Robert Gil", MentaCript.decript("hnIsr8YIMxiWrXVGQbaMNg=="));
		Assert.assertEquals("", MentaCript.decript("") );
		Assert.assertNull(MentaCript.decript(null) );
	}

	@Test(expected=DecriptException.class)
	public void testDecriptException() throws DecriptException {
		// will throw exception
		MentaCript.decript("8PlvDGLiXak_=");
	}
	
	@Test(expected=DecriptException.class)
	public void testDecriptExceptionNumber() throws DecriptException {
		// will throw exception
		MentaCript.decript("1");
	}
	
	@Test(expected=DecriptException.class)
	public void testDecriptExceptionText() throws DecriptException {
		// will throw exception
		MentaCript.decript("error");
	}
	
	@Test
	public void testNull() throws DecriptException {
		String s = null;
		Integer i = null;
		Assert.assertNull( MentaCript.cript( s ) );
		Assert.assertNull( MentaCript.cript( i ) );
		Assert.assertNull( MentaCript.decript(s) );
	}
	

}
