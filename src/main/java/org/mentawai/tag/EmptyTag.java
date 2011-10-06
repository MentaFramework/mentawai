package org.mentawai.tag;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.util.ConditionalTag;

public class EmptyTag extends ConditionalTag {
	
	private String value;
	
	public void setValue(String value) { this.value = value; }
	
	public boolean testCondition() throws JspException {
		
		Object obj = getValue(value);
		
		if (obj == null || obj.toString().trim().length() == 0) {
			
			return true;
		}
		
		return false;
	}
}