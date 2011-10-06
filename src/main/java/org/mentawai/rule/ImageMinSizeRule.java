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

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.mentawai.core.Action;
import org.mentawai.core.Input;
import org.mentawai.util.ImageUtils;

public class ImageMinSizeRule implements Rule {

	private final static Map<String,ImageMinSizeRule> cache = new HashMap<String, ImageMinSizeRule>();
	
	private Map<String, String> tokens = new HashMap<String, String>();
	
	private final int width;
	private final int height;
	
	public ImageMinSizeRule(int w) {
		this(w, -1);
	}

	private ImageMinSizeRule(int w, int h) {
		this.width = w;
		this.height = h;
		tokens.put("width", String.valueOf(w));
		if (h > 0) tokens.put("height", String.valueOf(h));
	}

	public static ImageMinSizeRule getInstance(int w) {

		return getInstance(w, -1);
	}
	
	public static ImageMinSizeRule getInstance(int w, int h) {
		
		StringBuilder sb = new StringBuilder(16);
		
		sb.append(w).append('_').append(h);
		
		String pattern = sb.toString();

		ImageMinSizeRule fr = cache.get(pattern);

		if (fr != null) return fr;

		fr = new ImageMinSizeRule(w, h);

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
        	
        	byte[] data = item.get();
        	
        	if (item.getSize() <= 0 || data == null || data.length <= 0) {
        		
        		throw new org.mentawai.util.RuntimeException("Cannot find image file to validate: " + field);
        		
        	}
        	
        	Dimension d = ImageUtils.getSize(data);
        	
        	if (d.getWidth() < width || d.getHeight() < height) return false;
        	
        	return true;
        	
        } else {
        	
        	throw new org.mentawai.util.RuntimeException("Bad type for file upload: " + value.getClass());
        }
	}

	public Map<String, String> getTokens() {
		return tokens;
	}
}
