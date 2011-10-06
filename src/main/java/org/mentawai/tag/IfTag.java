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
package org.mentawai.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.tag.util.ConditionalTag;
import org.mentawai.tag.util.Context;
import org.mentawai.util.Regex;

/**
 * @author Sergio Oliveira
 */
public class IfTag extends ConditionalTag {
    
    private String test = null;
    private String value = null;
    private String dynValue = null;
    
    public void setTest(String test) {
        this.test = test;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public void setDynValue(String dynValue) {
    	this.dynValue = dynValue;
    }
    
    public boolean testCondition() throws JspException {
    	
    	if (dynValue != null && value != null) {
    		
    		throw new JspException("Invalid IfTag: cannot have value and dynValue at the same time!");
    	}
    	
        Tag parent = findAncestorWithClass(this, Context.class);
        Object obj = Out.getValue(parent, test, pageContext, true);
        
        Object dynObj = null;
        
        if (dynValue != null) {
        	
        	dynObj = Out.getValue(parent, dynValue, pageContext, true);
        }
        
        if (obj == null) {
        	
        	if (value != null && value.equals("null")) return true;
        	
        	if (dynValue != null && dynObj == null) return true;
        	
        	// try request parameters if param.something...
        	
        	if (test.startsWith("param.")) {
        		
        		String s = Regex.sub(test, "s/param\\.//");
        		
        		if (s.trim().length() == 0) return false;
        		
        		obj = req.getParameter(s);
        		
        		if (obj == null || obj.toString().trim().length() == 0) return false;
        		
        	} else {
        	
        		// throw new JspException("NullPointerException on IfTag: test expression " + test + " evaluated to null!");
        	
        		return false; // no need to throw nasty exception here...
        	}
        	
        }
        
        if (obj instanceof Boolean && dynValue == null && value == null) {
        	
        	Boolean b = (Boolean) obj;
        	
        	return b.booleanValue();
        }
        
        if (dynValue != null) {
        	
        	if (dynObj == null) return false;
        	
        	return obj.equals(dynObj);
        	
        } else {
        
	        if (obj instanceof Boolean) {
	            Boolean b = (Boolean) obj;
	            if (value != null) {
	                if (!value.equalsIgnoreCase("false") && !value.equalsIgnoreCase("true")) {
	                    throw new JspException("Invalid IfTag: value must be a boolean: " + test + " / " + value);
	                }
	                boolean flag = value.equalsIgnoreCase("true");
	                return b.booleanValue() == flag;
	            }
	            return b.booleanValue();
	        } else if (obj instanceof Integer) {
	            Integer i = (Integer) obj;
	            if (value == null) {
	                throw new JspException("Invalid IfTag: value must be present for integer: " + test);
	            }
	            
	            if (value.indexOf(",") > 0) {
	            	
	            	// maybe a list of integers ???
	            	
	            	String[] s = value.split("\\s*,\\s*");
	            	
	            	int[] array = new int[s.length];
	            	
	            	try {
	            		
	            		for(int x=0;x<array.length;x++) {
	            			
	            			array[x] = Integer.parseInt(s[x]);
	            		}
	            		
	            		for(int x=0;x<array.length;x++) {
	            			
	            			if (array[x] == i.intValue()) return true;
	            		}
	            		
	            		return false;
	            		
	            	} catch(Exception e) { }
	            }
	            
	            try {
	                return i.intValue() == Integer.parseInt(value);
	            } catch(NumberFormatException e) {
	                throw new JspException("Invalid IfTag: value must be an integer: " + test + " / " + value);
	            }
	        } else if (obj instanceof Character) {
	        	
	        	Character c = (Character) obj;
	        	
	        	if (value == null) {
	                throw new JspException("Invalid IfTag: value must be present for character: " + test);
	            } else if (value.length() != 1) {
	            	throw new JspException("Invalid IfTag: value is not a char: " + value);
	            }
	            	
                return c.charValue() == value.charAt(0);
                
	        } else if (obj instanceof Enum) {
	        	
	        	String s = obj.toString();
	        	
	        	return s.equals(value);
	        	
	        } else if (obj instanceof String) {
	        	
	        	 String objString = (String) obj;
	        	 
		            if (value == null) {
		                throw new JspException("Invalid IfTag: value must be present for String: " + test);
		            }
		            
		            if (value.indexOf(",") > 0) {
		            	
		            	// maybe a list of String ???
		            	
		            	String[] s = value.split("\\s*,\\s*");
		            	
		            	for(String x : s) if (objString.equals(x)) return true;
		            	
		            	return false;
		            }
		            
		            return objString.equals(value);
	                
	        } else {
	            if (value == null) {
	                throw new JspException("Invalid IfTag: value must be present: " + test);
	            }
	            return value.equals(obj);
	        }
        
        }
    }
}