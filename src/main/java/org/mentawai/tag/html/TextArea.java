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
import javax.servlet.jsp.tagext.BodyContent;

/**
 * @author Sergio Oliveira
 */
public class TextArea extends HTMLTag {
    
	private String name;
	private String id = null;
	private String klass = null;
    private String style = null;
    private int cols = -1;
    private int rows = -1;
    private int maxlength = 0;
	
	public void setName(String name) { this.name = name; }
	public void setKlass(String klass) { this.klass = klass; }
    public void setStyle(String style) { this.style = style; }
	public void setId(String id) { this.id = id; }
    public void setCols(int cols) { this.cols = cols; }
    public void setRows(int rows) { this.rows = rows; }
    public void setMaxlength(int maxlength) { this.maxlength = maxlength; }
	
	protected StringBuffer buildTag() {
		StringBuffer sb = new StringBuffer(2000);
		sb.append("<textarea name=\"").append(name).append("\"");
		if (klass != null) sb.append(" class=\"").append(klass).append("\"");
        if (style != null) sb.append(" style=\"").append(style).append("\"");
		if (id != null) sb.append(" id=\"").append(id).append("\"");
        if (cols > 0) sb.append(" cols=\"").append(cols).append("\"");
        if (rows > 0) sb.append(" rows=\"").append(rows).append("\"");
        if (maxlength > 0) sb.append(" onKeyDown=\"this.value=this.value.substring(0,")
        	.append(maxlength).append(")\" onKeyUp=\"this.value=this.value.substring(0,")
        	.append(maxlength).append(")\"");
        
        sb.append(getExtraAttributes());
        
		return sb;
	}
    
    protected String formatText(String text) {
        return text;
    }
    
    public String getStringToPrint() throws JspException {

        StringBuffer sb = new StringBuffer(200);
		sb.append(buildTag().toString());		
        sb.append(">");
        
        Object value = findObject(name, false, true);
        if (value == null) {
            // check if there is a default text in the page...
            BodyContent bc = getBodyContent();
            if (bc != null) {
                String text = bc.getString();
                sb.append(text);
            }
        } else {
            sb.append(formatText(value.toString()));
        }
        sb.append("</textarea>");
		return sb.toString();
    }
    
}
            
