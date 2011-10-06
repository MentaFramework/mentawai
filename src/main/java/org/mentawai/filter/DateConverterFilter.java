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
package org.mentawai.filter;

import java.util.Map;

import org.mentawai.converter.Converter;
import org.mentawai.converter.DateConverter;
import org.mentawai.core.Action;

public class DateConverterFilter extends ConversionFilter {
	
	private final Converter dateConverter;
	
	private final String[] fields;
	
	public DateConverterFilter(String ...fields) {
		super();
		this.fields = fields;
		this.dateConverter = new DateConverter();
	}
	
	public DateConverterFilter(int style, String ...fields) {
		super();
		this.fields = fields;
		this.dateConverter = new DateConverter(style);
	}
	
	public void prepareConverters(Map<String, Converter> converters, Action action, String innerAction) {
		
		for(int i=0;i<fields.length;i++) {
			
			converters.put(fields[i], dateConverter);
		}
	}
}