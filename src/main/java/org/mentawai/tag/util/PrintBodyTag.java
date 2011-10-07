package org.mentawai.tag.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.mentaregex.Regex;

public abstract class PrintBodyTag extends PrintTag {
	
	private boolean replaceAll = false;
	
	public void setReplaceAll(boolean flag) {
		this.replaceAll = flag;
	}
	
	@Override
    public int doEndTag() throws JspException {
		
        String s = getStringToPrint();
        
        String body = getBody();
        
    	if (body != null) {
    		
    		if (s.contains("/")) {
    			
    			s = s.replaceAll("/", "#/");
    		}
    		
    		if (replaceAll) {
    		
    			s = Regex.sub(body, "s/#$#{out#}/" + s + "/g", '#');
    			
    		} else {
    			
    			s = Regex.sub(body, "s/#$#{out#}/" + s + "/", '#');
    		}
    	}        
        if (s != null) {
        	
        	if (noHTML) {
        	
        		s = s.replaceAll("\\<.*?\\>","");
        		
        	}
            
            if (maxToPrint > 0 && s.length() > maxToPrint) {
                
                s = s.substring(0, maxToPrint) + "...";
                
            }
            
            try {
                pageContext.getOut().print(s);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        return EVAL_PAGE;
    }	
	
}