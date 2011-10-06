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
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.mentawai.list.ListData;
import org.mentawai.list.ListItem;
import org.mentawai.list.ListManager;
import org.mentawai.tag.Out;

/**
 * @author Sergio Oliveira
 */
public class ListRadio extends HTMLTag {
	
	private String name;
	private String listname;
	private boolean useBR = false;
	private String klass = null;
    private String style = null;
	private String id = null;
    private int spacing = 1;
    private String defValue = null;
	
	public void setName(String name) { this.name = name; }
	public void setList(String listname) { this.listname = listname; }
	public void setUseBR(boolean useBR) { this.useBR = useBR; }
	public void setKlass(String klass) { this.klass = klass; }
    public void setStyle(String style) { this.style = style; }
	public void setId(String id) { this.id = id; }
	public void setDefValue(String defValue) { this.defValue = defValue; }
    
    public void setSpacing(int spacing) { 
        if (spacing < 0) throw new IllegalArgumentException("Spacing must be greater than zero: " + spacing);
        this.spacing = spacing;
    }
    
    public String getStringToPrint() throws JspException {
        
        String value = findValue(name, false, true);
        
        ListData list = null;
        
        Object obj = Out.getValue(listname, pageContext, false);
        
        if (obj != null && obj instanceof ListData) {
            
            list = (ListData) obj;
            
        } else if (obj != null && obj instanceof Map) {
        	
        	list = ListManager.convert(listname, (Map) obj);
        	
        } else if (obj != null && obj instanceof Collection) {
        	
        	list = ListManager.convert(listname, (Collection) obj);
            
        } else {
        
            list = ListManager.getList(listname);
            
        }
        
        StringBuffer sb = new StringBuffer(list.size() * 50);
        Iterator<ListItem> iter = list.getValues(loc).iterator();
    
        while(iter.hasNext()) {
            ListItem item = iter.next();
            String id = item.getKey();
            String n = item.getValue();
        
            sb.append("<input type=\"radio\" name=\"").append(name).append("\"");
            if (this.id != null) {
                sb.append(" id=\"").append(this.id).append("\"");
            }
            if (this.klass != null) {
                sb.append(" class=\"").append(this.klass).append("\"");
            }
            if (this.style != null) {
                sb.append(" style=\"").append(this.style).append("\"");
            }
            sb.append(" value=\"").append(id).append("\"");
            
            String extra = getExtraAttributes();
            
            sb.append(extra);
            
            if (value == null && defValue != null && defValue.equals(id)) {
            	
            	sb.append(" checked=\"true\" />");
            	
            } else if (value != null && value.equals(id)) {
                //sb.append(" CHECKED /> ");
                sb.append(" checked=\"true\" />");
                
                // Verifico se o valor eh 0=false ou 1=true
            } else if (value != null && (value.equals("true") || value.equals("false"))){
            	
            	if(value.equals("true") && id.equals("1")) {
            		sb.append(" checked=\"checked\" />");
            		
            	}
            	else if(value.equals("false") && id.equals("0")){
            		sb.append(" checked=\"checked\" />");
            		
            	} else {
                    sb.append("/>");
            	}
            	
            } else {
                sb.append("/>");
            }
            sb.append(n);
            if (useBR) {
                sb.append("<br/>\n");
            } else {
                for(int i=0;i<spacing;i++) {
                    sb.append("&nbsp;");
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
	
}
	
