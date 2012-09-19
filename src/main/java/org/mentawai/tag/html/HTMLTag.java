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
package org.mentawai.tag.html;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.core.BaseAction;
import org.mentawai.core.Input;
import org.mentawai.core.Output;
import org.mentawai.cript.MentaCript;
import org.mentawai.tag.Out;
import org.mentawai.tag.util.Context;
import org.mentawai.tag.util.PrintTag;

/**
 * @author Sergio Oliveira
 */
public abstract class HTMLTag extends PrintTag {
    
    private static final String SEPARATOR = "#";
    
    private String extra;
    
    private String separator = SEPARATOR;
    
    protected boolean cript = false;
    
    public void setCript(boolean cript) {this.cript = cript; }
    public void setExtra(String extra) {this.extra = extra; }
    public void setSeparator(String separator) { this.separator = separator; }
    
    /**
     * 
     * @param html tag that its values will be cripted
     * @return html with values cripteds
     */
    protected String processCriptValue(String s) {

		if(cript) {
			
			MentaCript mc = MentaCript.getInstance( session );
			
			int indexOf = 0;
			while ((indexOf = s.indexOf("value=\"", indexOf)) > 0) {
				indexOf += 7;
				String value = s.substring( indexOf , s.indexOf("\"", indexOf));
				String cripted = mc.cript(value);
				s = s.replace("value=\"" + value, "value=\"" + cripted);
			}
			return processCriptedName(s);
		}
		
		return s;
	}
    
    /**
     * 
     * @param html tag that its name will be changed to cript pattern
     * @return html with name cripted
     */
    protected String processCriptedName(String v) {
    	
    	int indexOfName = v.indexOf("name=\"") + 6;
		String name = v.substring( indexOfName , v.indexOf("\"", indexOfName));
		
		StringBuilder fieldName = new StringBuilder();
		fieldName.append("name=\"");
		fieldName.append(MentaCript.PREFIX_CRIPT_TAG);
		fieldName.append(name);
		
		return v.replace("name=\"" + name, fieldName.toString());
		
    }

    protected String getExtraAttributes() {
        
        if (extra == null) return "";
        
        StringBuffer sb = new StringBuffer(512);
        
        String[] s = extra.split("\\" + separator);
        
        for(int i=0;i<s.length;i++) {
            
            String[] ss = s[i].split("=");
            
            if (ss.length != 2) continue;
            
            sb.append(" ").append(ss[0].trim()).append("=\"").append(ss[1].trim()).append('"');
            
        }
        
        return sb.toString();
    }
    
    protected Object findObject(String name) {
    	
    	return findObject(name, false, false);
    	
    }
    
    protected Object findObject(String name, boolean tryBoolean, boolean inputFirst) {
        
        Object value = null;
        
        if (inputFirst) {
        	
        	if (action != null && BaseAction.isPost(action) && action.getInput().has(name)) {
        		
        		return action.getInput().getValue(name);
        	}
        }
        
        Tag parent = findAncestorWithClass(this, Context.class);
        
        if (parent != null) {
        
	        try {
	            
	            value = Out.getValue(parent, name, pageContext, tryBoolean);
	            
	            if (value != null && !value.equals("")) return value;
	            
	        } catch(JspException e) {
	            
	            //e.printStackTrace();
	            
	        }
        
        }
        
        if (action == null) return null;
        
		Output output = action.getOutput();
		Input input = action.getInput();
		
		value = output.getValue(name);
		
		if (value == null)  // Find using expression like 'bean.id'
			value = Out.getValue(name, pageContext, tryBoolean);
		
		if (value == null)  // Try input
			value = input.getValue(name);	
		
		if (value == null) // Try session
			value = session.getAttribute(name);
			
		
		
        return value;
    }    
    
    protected String findValue(String name) {
        Object value = findObject(name);
        if (value == null) return null;
        return value.toString();
    }
    
    protected String findValue(String name, boolean tryBoolean, boolean inputFirst) {
        Object value = findObject(name, tryBoolean, inputFirst);
        if (value == null) return null;
        return value.toString();
    }
    
    protected boolean contains(int [] values, int x) {
        if (values == null) return false;
		for(int i=0;i<values.length;i++) {
			if (values[i] == x) return true;
		}
        return false;
    }
    
    protected boolean contains(String [] values, String x) {
        if (values == null) return false;
		for(int i=0;i<values.length;i++) {
			if (values[i] != null && values[i].equals(x)) return true;
		}
        return false;
    }	
    
    protected String[] findValues(String name) {
    	
    	return findValues(name, false, false);
    }
    
	protected String[] findValues(String name, boolean tryBoolean, boolean inputFirst) {
		
        Object value = findObject(name, tryBoolean, inputFirst);
        
		if (value == null) return null;
		
		if (value instanceof int[]) {
			
			int[] x = (int[]) value;
			
			String[] s = new String[x.length];
			
			for(int i=0;i<x.length;i++) {
				
				s[i] = String.valueOf(x[i]);
			}
			
			return s;
		}
		
		if (value instanceof String[]) {
			
			return (String[]) value;
		}
		
		if (value instanceof Integer) {
			String[] x = new String[1];
			x[0] = String.valueOf(((Integer) value).intValue());
			return x;
		}

		// treat any other number such as Float...
		if (value instanceof Number) {
			String[] x = new String[1];
			x[0] = value.toString();
			return x;
		}
		
		if (value instanceof String) {
			String[] x = new String[1];
			x[0] = (String) value;
			return x;
		}
		
		if(value instanceof Enum){
			String[] x = new String[1];
			x[0] = value.toString();
			return x;
		}
      
      if (value instanceof Collection) {
         
         Collection c = (Collection) value;
         
         String[] x = new String[c.size()];
         
         int index = 0;
         
         Iterator iter = c.iterator();
         
         while(iter.hasNext()) {
            
            x[index++] = iter.next().toString();
            
         }
         
         return x;
      }
      
      if( value instanceof Boolean) {
    	  return new String[] { String.valueOf(value) };
      }
		
		return null;
	}
    
}

