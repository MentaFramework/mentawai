package org.mentawai.rule;

import junit.framework.TestCase;

public class EmailRuleTest extends TestCase {

	public void testCheckString() {
		EmailRule v = EmailRule.getInstance();
		
		assertTrue( v.check("robertwgil@gmail.com") );
		assertTrue( v.check("robert-gil@gmail.com") );
		assertTrue( v.check("robert.gil@gmail.com") );
		assertTrue( v.check("robert-w-gil@gmail.com") );
		assertTrue( v.check("robert-w.gil@gmail.com") );
		assertTrue( v.check("robertwgil@g-mail.com") );
		assertTrue( v.check("robert.w.gil@gmail.com") );
		assertTrue( v.check("robert.w.gil@saint-gmail.com") );
		assertTrue( v.check("robert.w.gil@saint-gmail.com.br") );
		
		assertFalse( v.check("robertwgilgmail.com") );
		assertFalse( v.check("@gmail.com") );
		assertFalse( v.check("robertwgil@") );
		assertFalse( v.check("robertwgil@gmailcom") );
		assertFalse( v.check("robertwgil@gmail-com") );
		assertFalse( v.check("robertwgil@ gmail.com") );
		assertFalse( v.check(" robertwgil@gmail.com") );
		assertFalse( v.check("robertwgil@gmail.com ") );
		
	}

}
