package org.mentawai.tag.html;


import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class SelectTest {

	@Test
	public void testProcessCriptValue() {
		
		HttpSession httpSession = Mockito.mock(HttpSession.class);
		Mockito.when(httpSession.getId()).thenReturn("SESSIONID");
		
		Select s = new Select(httpSession);
		s.setCript(true);
		
		StringBuilder html = new StringBuilder();
		html.append("<select name=\"groupId\">");
		html.append("<option value=\"1\">Convidado</option>");
		html.append("<option value=\"2\">Administrador</option>");
		html.append("<option value=\"3\" selected=\"selected\">Mestre</option>");
		html.append("</select>");
		
		String result = "<select name=\"mtw_cript_groupId\"><option value=\"p+xrnYMDSfw=\">Convidado</option><option value=\"G7j6lE3JpTY=\">Administrador</option><option value=\"AWwkKSH5qNU=\" selected=\"selected\">Mestre</option></select>";
		
		Assert.assertEquals(result, s.processCriptValue(html.toString()));
		
	}

}
