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

import com.thoughtworks.xstream.XStream;

/**
 * A AjaxRenderer that uses XStream.
 * 
 * Depending on the XStream driver, you may generate XML or JSON.
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class XStreamRenderer implements AjaxRenderer {

    private final XStream xstream;
    private final String contentType;

    public XStreamRenderer() {

        this(new XStream(), TEXT_XML);
    }
    
    public XStreamRenderer(XStream xstream) {
    	this(xstream, TEXT_XML);
    }
    
    public XStreamRenderer(String contentType) {
    	this(new XStream(), contentType);
    }

    public XStreamRenderer(XStream xstream, String contentType) {

        if (xstream == null) {
        	
            throw new IllegalArgumentException("The attribute \'xStream\' cannot be null!");
            
        }
        
        if (contentType == null) {
        	
            throw new IllegalArgumentException("The attribute \'contentType\' cannot be null!");
            
        }
        
        this.xstream = xstream;
        this.contentType = contentType;
    }

    public String encode(Object obj, Locale loc, boolean prettty) throws Exception {
    	
    	// not sure how to handle pretty x compact formats here...
    	
        return xstream.toXML(obj);
    }

    public String getContentType() {
        return contentType;
    }
    
    public String getCharset() {
       return AjaxConsequence.DEFAULT_CHARSET;
    }
}
