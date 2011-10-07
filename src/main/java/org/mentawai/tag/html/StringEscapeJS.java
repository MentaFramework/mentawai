package org.mentawai.tag.html;

import javax.servlet.jsp.JspException;

import org.mentaregex.Regex;
import org.mentawai.tag.util.PrintTag;

public class StringEscapeJS extends PrintTag {
	
	private static final String REGEX = "s/\\n/\\\\\n/g";
	
	public String getStringToPrint() throws JspException {
		
		String body = getBody();
		
		return Regex.sub(body, REGEX);
	}
	
	public static void main(String[] args) {
		
		String s = "aaa\nbbb\nccc\n";
		
		System.out.println(s);
		
		System.out.println(Regex.sub(s, REGEX));
	}
}