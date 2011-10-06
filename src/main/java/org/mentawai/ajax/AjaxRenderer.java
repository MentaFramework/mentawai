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
package org.mentawai.ajax;

import java.util.Locale;


/**
 * This class will generate a document representation from a Java object.
 * 
 * The document representation is usually XML or JSON.
 * 
 * @author soliveira
 * @author Rubem Azenha
 */
public interface AjaxRenderer {
    
    /**
     * Content-type for text/html.
     */
    public static final String TEXT_HTML = "text/html";
    
    /**
     * Content-type for text/xml.
     */
    public static final String TEXT_XML = "text/xml";
    
    public static final String APP_JSON = "application/json";
    
    public static final String APP_JS = "application/javascript";
    
    public static final String APP_URLENCODED = "application/x-www-form-urlencoded";
    
	public String encode(Object object, Locale loc, boolean pretty) throws Exception;
    
   public String getContentType();
    
   public String getCharset(); 
    
}
 