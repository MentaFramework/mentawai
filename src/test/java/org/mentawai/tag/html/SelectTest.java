package org.mentawai.tag.html;

import org.junit.Test;

public class SelectTest {

	@Test
	public void testProcessCriptValue() {
		
		Select s = new Select();
		
		StringBuilder html = new StringBuilder();
		html.append("<select name=\"groupId\">");
		html.append("<option value=\"1\">Convidado</option>");
		html.append("<option value=\"2\">Administrador</option>");
		html.append("<option value=\"3\" selected=\"selected\">Mestre</option>");
		html.append("</select>");
		
		System.out.println(	s.processCriptValue(html.toString()) );
		
	}

}
