package org.mentawai.tag;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.util.ConditionalTag;

public class ExistsTag extends ConditionalTag {
	
	private String value;
	
	public void setValue(String value) { this.value = value; }
	
	public boolean testCondition() throws JspException {
		
		if (value.startsWith("param.")) {
		
			String[] s = value.split("\\.");
			
			if (s.length == 2) {
				
				Object obj = null;
				
				String key = s[1];
				
				if (action != null) {
					
					obj = action.getInput().getValue(key);
					
				} else {
					
					obj = req.getParameter(key);
				}
				
				if (obj == null || obj.toString().trim().length() == 0) {
					
					return false;
				}
				
				return true;
			}
			
			return false;
			
		} else {
		
			Object obj = getValue(value);
		
			return obj != null;
		}
	}
}