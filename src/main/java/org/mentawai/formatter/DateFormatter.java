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
package org.mentawai.formatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter implements Formatter {
	
	private int style = -1;
	
	private SimpleDateFormat sdf = null;
	
	public DateFormatter(int style) {
		
		this.style = style;
	}
	
	public DateFormatter(String pattern) {
		
		this.sdf = new SimpleDateFormat(pattern);
	}
	
	public String format(Object value, Locale loc) {
		
		if(value == null) return null;
		
		if (!(value instanceof Date)) return value.toString();
		
		Date d = (Date) value;
		
		if (sdf != null) {
			
			return sdf.format(d);
			
		} else if (style != -1) {
			
			DateFormat df = DateFormat.getDateInstance(style, loc);
			
			return df.format(d);
			
		} else {
			
			throw new IllegalStateException("Should never be here!");
		}
	}
	
	
	
}