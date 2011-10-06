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
package org.mentawai.message;

import java.util.Locale;

import org.mentawai.i18n.I18N;
import org.mentawai.i18n.I18NMap;

/**
 * @author Sergio Oliveira
 */
public class ClasspathMessageContext implements MessageContext {
	
	private final String resource;
	
	public ClasspathMessageContext(String resource) {
		this.resource = resource;
	}
	
    public String getMessage(String id, Locale loc) {
    	
        I18N i18n = I18NMap.getI18NFromClasspath(resource + "_" + loc.toString());

        I18N i18nLang = I18NMap.getI18NFromClasspath(resource + "_" + loc.getLanguage());
        
        if (i18n != null && i18n.hasKey(id)) {
            
            return i18n.get(id);
            
        }
        
        if (i18nLang != null && i18nLang.hasKey(id)) {
            
            return i18nLang.get(id);
        }
        
        return id;
    }
    
    public String toString() {
        return resource;
    }
}

    
    