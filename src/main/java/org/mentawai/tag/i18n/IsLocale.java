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

import org.mentawai.i18n.LocaleManager;
import org.mentawai.tag.util.ConditionalTag;

/**
 * @author Sergio Oliveira
 */
public class IsLocale extends ConditionalTag {
    
    private String value;
    
    public void setValue(String value) {
        
        this.value = value;
        
    }
    
    public boolean testCondition() throws JspException {
    	
    	String[] s = value.split("\\s*,\\s*");
    	
    	for(int i=0;i<s.length;i++) {
    		
    		LocaleManager.decideLocale(req, res); // force the locale to be chosen from param or cookie...
    	
    		Locale loc = LocaleManager.getLocale(req, false);
        
    		String locale = loc.toString();
        
    		if (locale.startsWith(s[i])) return true;
    	}
        
        return false;
        
    }
}