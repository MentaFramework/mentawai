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
package org.mentawai.ajax.renderer;

import java.util.Locale;

import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;
/**
 * 
 * <p>A AjaxRender that creates a XML structure from
 * a given String.</p>
 * 
 * <p>By default, it gets from the action's output a String identified by the key "value" and
 * generates a XML structure with a sigle tag named "value", with the body content as the value
 * from the output, but this behavior can be changed at the constructor.</p>
 *   
 * <p>
 * Example of a generated XML:
 * <br/>
 * &ltvalue&gtvalueFromOutput&lt/value&gt
 * </p>
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 *
 */
public class StringAjaxRenderer implements AjaxRenderer {
	
    //private static final String DEFAULT_VALUE_ATTR = "value";
    private static final String DEFAULT_TAG_NAME= "value";

	private String tagName;
	
	public StringAjaxRenderer() {
		this.tagName = DEFAULT_TAG_NAME;
	}
	
	public StringAjaxRenderer(String tagName) {
		this.tagName = tagName;
	}
	
	public String encode(Object obj, Locale loc, boolean pretty) throws Exception {
		
		if (!(obj instanceof String)) throw new IllegalArgumentException("Value is not a String: " + obj);
		
		String value = (String) obj;
		
		StringBuilder buffer = new StringBuilder();
		
		buffer.append('<').append(tagName).append('>').append(value)
		
		.append("</").append(tagName).append('>');
		
		return buffer.toString();
	}

    public String getContentType() {
        return TEXT_XML;
    }
    
    public String getCharset() {
       return AjaxConsequence.DEFAULT_CHARSET;
    }

}
