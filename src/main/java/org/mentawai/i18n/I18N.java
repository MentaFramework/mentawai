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
package org.mentawai.i18n;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Sergio Oliveira
 */
public class I18N {
	
	public static String CHARSET = "UTF-8";

    private Properties props = null;

    public I18N(Properties props) {
        this.props = props;
    }
	
	public String get(int key) {
		return get(String.valueOf(key));
	}
    
    public boolean hasKey(String key) {
        String text = props.getProperty(key);
        return text != null;
    }

    public String get(String key) {
        String text = props.getProperty(key);
        if (text == null) return key;
        return text;
    }
	
	public Iterator<String> keys() {
		Enumeration en = props.propertyNames();
		HashSet<String> set = new HashSet<String>();
		while(en.hasMoreElements()) {
			String key = (String) en.nextElement();
			set.add(key);
		}
		return set.iterator();
	}
}