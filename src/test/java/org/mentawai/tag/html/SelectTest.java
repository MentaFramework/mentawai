package org.mentawai.tag.html;


import junit.framework.Assert;

import org.junit.Test;

public class SelectTest {

	@Test
	public void testProcessCriptValue() {
		
		Select s = new Select();
		s.setCript(true);
		
		StringBuilder html = new StringBuilder();
		html.append("<select name=\"groupId\">");
		html.append("<option value=\"1\">Convidado</option>");
		html.append("<option value=\"2\">Administrador</option>");
		html.append("<option value=\"3\" selected=\"selected\">Mestre</option>");
		html.append("</select>");
		
		String result = "<select name=\"mtw_cript_groupId\"><option value=\"8PlvDGLiXak=\">Convidado</option><option value=\"eT2wmS3QZos=\">Administrador</option><option value=\"W0UHWnNyixo=\" selected=\"selected\">Mestre</option></select>";
		
		Assert.assertEquals(result, s.processCriptValue(html.toString()));
		
	}

}
