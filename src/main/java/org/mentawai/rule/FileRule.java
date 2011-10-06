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
package org.mentawai.rule;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.mentawai.core.Action;
import org.mentawai.core.Input;

/**
 * A rule to validate a content type of file to upload, this
 * validation uses regular expression pattern.
 *
 * @author Leiber Wallace
 * @author Diego Andrade
 */
public class FileRule implements Rule {

	private final static Map<String,FileRule> cache = new HashMap<String, FileRule>();
	private String pattern;

	public FileRule(String pattern) {
		this.pattern = pattern;
	}

	public static FileRule getInstance(String pattern) {

		FileRule fr = cache.get(pattern);

		if (fr != null) return fr;

		fr = new FileRule(pattern);

		cache.put(pattern, fr);

		return fr;


	}

    public boolean check(String field, Action action) {
    	
        Input input = action.getInput();
        
        Object value = input.getValue(field);
        
        if (value == null || value.toString().trim().equals("")) {
        	
        	// if we got to this point, it means that there is no RequiredRule
        	// in front of this rule. Therefore this field is probably an OPTIONAL
        	// field, so if it is NULL or EMPTY we don't want to do any
        	// futher validation and return true to allow it.
        	
        	return true; // may be optional
        }
        
        if (value instanceof FileItem) {
        	
        	FileItem item = (FileItem) value;
        	
        	if (item.getContentType().matches(pattern)) return true;
        	
        	return false;
        	
        } else {
        	
        	throw new org.mentawai.util.RuntimeException("Bad type for file upload: " + value.getClass());
        }
	}

	public Map<String, String> getTokens() {
		return null;
	}
}
