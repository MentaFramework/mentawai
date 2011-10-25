/*
 * Mentawai Web Framework http://mentawai.lohis.com.br/
 * Copyright (C) 2005  Sergio Oliveira Jr. (sergio.oliveira.jr@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.mentawai.tag.i18n;

import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentaregex.Regex;
import org.mentawai.i18n.I18N;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.tag.Out;
import org.mentawai.tag.util.Context;
import org.mentawai.tag.util.PrintTag;

/**
 * @author Sergio Oliveira
 */
public class PrintI18N extends PrintTag {
	
	private static boolean DEBUG = LocaleManager.I18N_DEBUG;
	
	private String key;
	private String[] dynValues = null;
    private boolean noPrefix = false;
	
	public void setKey(String key) {
		this.key = key.trim();
	}
    
    public void setNoPrefix(boolean noPrefix) {
        this.noPrefix = noPrefix;
    }
    
    public void setDynValues(String dynValues) {
    	
    	this.dynValues = dynValues.split("\\s*,\\s*");
    }
    
    protected String getKey() throws JspException {
    	
    	if (key != null) {
    		
    		return key;
    	}
    	
    	String body = getBody();
    	
    	if (body != null) {
    		
    		return body;
    	}
    	
    	throw new JspException("i18n tag does not have key attribute and does not have body text!");
    	
    }
    
	public String getStringToPrint() throws JspException {
		
		String key = getKey();
		
		boolean hasKey = this.key != null;
		
		String body = getBody();

        I18N [] props = (I18N []) pageContext.getAttribute("_i18n");
        Locale loc = (Locale) pageContext.getAttribute("_locale");
        String prefix = (String) pageContext.getAttribute("_prefix");
        
		if (props == null || loc == null) {
			// lazy loading:
			
			UseI18N.loadI18N(pageContext, new String[0], null);
			
			props = (I18N []) pageContext.getAttribute("_i18n");
	        loc = (Locale) pageContext.getAttribute("_locale");
	        prefix = (String) pageContext.getAttribute("_prefix");
			
			// throw new JspException("i18n tag needs a useI18N tag in the same page!");
		}
        
        if (prefix != null && !noPrefix) {
            key = prefix + "." + key;
        }
		
        for(int i=props.length-1;i>=0;i--) {
        	
            if (props[i] == null) continue;
            
            if (props[i].hasKey(key)) {
            	
            	String v = props[i].get(key);
            	
            	String cp = req.getContextPath();
            	
            	if (cp.startsWith("/")) {
            		cp = "#" + cp;
            	}
            	
        		v = Regex.sub(v, "s/<mtw:contextPath *#/>/" + cp + "/g", '#');
        		
        		// NOW substitute dyn values...
        		
        		if (dynValues != null) {
        			
        			Tag parent = findAncestorWithClass(this, Context.class);
        			
        			int index = 0;
        			
        			for(String dynValue : dynValues) {
        			
        				index++;
        				
        				Object dynVal = Out.getValue(parent, dynValue, pageContext, true);
        				
        				String dynValString = dynVal != null ? dynVal.toString() : "NULL";
        				
        				v = Regex.sub(v, "s/#$" + index + "/" + dynValString + "/g", '#');
        			}
        			
        		}
            	
            	if (DEBUG) {
            		
            		return "{" + v + "}";
            		
            	} else {
            	
            		return v;
            		
            	}
            }
        }

        if (hasKey && body != null && DEBUG) {
        	
        	// return full description...
        	
        	StringBuilder sb = new StringBuilder(128);
        	
        	sb.append(key).append(" = ").append(body);
        	
        	return sb.toString();
        	
        } else {
        
        	StringBuilder sb = new StringBuilder(128);
	        sb.append('!');
	        sb.append(loc.toString());
	        sb.append('.');
	        sb.append(key);
	        sb.append('!');
	        
			return sb.toString();
		
        }
	}
}