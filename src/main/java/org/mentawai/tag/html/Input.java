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

import javax.servlet.jsp.JspException;

import org.mentaregex.Regex;

/**
 * @author Sergio Oliveira
 */
public class Input extends HTMLTag {
    
	private String name;
	private String id = null;
	private String klass = null;
    private String style = null;
    private int size = -1;
    private int maxlength = -1;
    private String type = "text"; // default
    private String value = null;
    private Boolean loseValue = null;
	
	public void setName(String name) { this.name = name; }
	public void setSize(int size) { this.size = size; }
	public void setKlass(String klass) { this.klass = klass; }
    public void setStyle(String style) { this.style = style; }
	public void setId(String id) { this.id = id; }
	public void setMaxlength(int maxlength) { this.maxlength = maxlength; }
    public void setType(String type) { this.type = type; }
    public void setValue(String value) { this.value = value; }
    public void setLoseValue(boolean value) { this.loseValue = value; }
    
	protected StringBuffer buildTag() {
		
		Object value = null;
		
		if (type.equalsIgnoreCase("checkbox") || type.equalsIgnoreCase("radio")) {
			
			value = findObject(name, true, true); // try boolean as well...
			
		} else {
			
			value = findObject(name, false, true); // no need to try boolean...
		}
		
		StringBuffer sb = new StringBuffer(200);
		sb.append("<input type=\"").append(type).append("\" name=\"").append(name).append("\"");
        if (size > 0) sb.append(" size=\"").append(size).append("\"");
        if (maxlength > 0) sb.append(" maxlength=\"").append(maxlength).append("\"");
		if (klass != null) sb.append(" class=\"").append(klass).append("\"");
        if (style != null) sb.append(" style=\"").append(style).append("\"");
		if (id != null) sb.append(" id=\"").append(id).append("\"");
        
        if (type.equalsIgnoreCase("text") 
        		|| type.equalsIgnoreCase("password") 
        		|| type.equalsIgnoreCase("hidden")
        		|| type.equalsIgnoreCase("email")
        		|| type.equalsIgnoreCase("number")
        		|| type.equalsIgnoreCase("url")) {
        	
        	if (loseValue == null) {
        		
        		// default values...
        		
        		if (type.equalsIgnoreCase("password")) {
        			
        			loseValue = true; // better for security
        			
        		} else {
        			
        			loseValue = false;
        		}
        	}
        	
        	if (!loseValue) {
        	
                if (value != null) {
                	
                	String x = Regex.sub(value.toString(), "s/\\\"/\\&quot;/g");
                	
                	sb.append(" value=\"").append(x).append("\"");
                	
                } else if (this.value != null) {
                	
                	String x = Regex.sub(this.value.toString(), "s/\\\"/\\&quot;/g");
                	
                	sb.append(" value=\"").append(x).append("\"");
                }
            
        	}
            
        } else if (type.equalsIgnoreCase("checkbox") || type.equalsIgnoreCase("radio")) {
        	
        	Object obj = null;
        	
            if (this.value != null) {
            	
            	obj = findObject(this.value, false, true);
            	
                sb.append(" value=\"");
            	
            	if (obj != null) {
            		
            		String x = Regex.sub(obj.toString(), "s/\\\"/\\&quot;/g");
            		
            		sb.append(x);
            		
            	} else {
            		
            		String x = Regex.sub(this.value.toString(), "s/\\\"/\\&quot;/g");
            		
            		sb.append(x);
            		
            	}
                sb.append("\"");
            }
            
            if(type.equalsIgnoreCase("checkbox")) {
            	
            	 String[] values = findValues(name, false, true);
            	 
            	 if (contains(values, this.value)) {
                     sb.append(" checked=\"true\"");
                 }
            	
            } else {
            	
            	if (obj != null && value != null && obj.toString().equals(value.toString())) {
                	
                	sb.append(" checked=\"true\"");
                	
                } else if (this.value != null && value != null && this.value.toString().equals(value.toString())) {

                	sb.append(" checked=\"true\"");
                	
                } else if (value instanceof Boolean) {
                	
                	Boolean b = (Boolean) value;
                	
                	if (b) sb.append(" checked=\"true\"");
                }
            	
            }
            
            
            
        } else {
        	
            throw new IllegalArgumentException("Input tag with bad type: " + type);
        }
        
        String extra = getExtraAttributes();
        
        sb.append(extra);
        
		return sb;
	}
    
    public String getStringToPrint() throws JspException {

    	StringBuilder sb = new StringBuilder(200);
		sb.append(buildTag().toString());		
        sb.append(" />");
		
		return processCriptValue(sb.toString());
    }

}
            
