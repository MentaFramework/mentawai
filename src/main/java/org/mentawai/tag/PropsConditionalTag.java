package org.mentawai.tag;

import javax.servlet.jsp.JspException;

import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Props;
import org.mentawai.tag.util.ConditionalTag;

public class PropsConditionalTag extends ConditionalTag {
	
	private static Props props = null;
	private String test;
	private String value;
	
	private static Props getProps() {
		if (props == null) {
			if (ApplicationManager.getInstance() == null) {
				return null;
			} else {
				props = ApplicationManager.getInstance().getProps();
			}
		}
		return props;
	}
	
	public void setTest(String s) {
		this.test = s;
	}
	
	public void setValue(String v) {
		this.value = v;
	}
	
	@Override
    public boolean testCondition() throws JspException {
		Props props = getProps();
		if (props == null) {
			return false;
		}
		
		String theValue = props.getString(test);
		
		if (theValue == null) return false;

		return theValue.equals(value);
    }
}