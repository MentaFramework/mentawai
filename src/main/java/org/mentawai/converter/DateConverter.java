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
package org.mentawai.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.mentawai.i18n.LocaleManager;

/**
 * Converts a <i>java.lang.String</i> to a <i>java.util.Date</i> using the user locale.
 *
 * @author Sergio Oliveira
 */
public class DateConverter extends LocaleConverter {
	
	private static final int STYLE = DateFormat.SHORT;
	
	private int style = STYLE;
	
	private boolean useStyle = false;
	
	private SimpleDateFormat sdf = null;
	
    /**
     * Creates an converter using the default DateFormat style (SHORT).
     */
	public DateConverter() { }
	
    /**
     * Creates an converter using the given DateFormat style.
     *
     * @param style The DateFormat style. (Ex: DateFormat.SHORT, DateFormat.FULL, etc.)
     */
	public DateConverter(int style) {
		this.style = style;
		useStyle = true;
	}
	
	public DateConverter(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	
	public DateConverter(String pattern) {
		this(new SimpleDateFormat(pattern));
	}
	
	public Object convert(Object value, Locale loc) throws ConversionException {
		
	  if (value instanceof String) {
		  
		  String s = ((String) value).trim();
		  
		  if (s.equals("")) return null;
		  
		  if (this.sdf != null) {
			  
			  try {
				  
				  return this.sdf.parse(s);
				  
			  } catch(ParseException e) {
				  
				  throw new ConversionException(e);
			  }
			  
			  
		  } else if (!useStyle && LocaleManager.getDateMask(loc) != null) {
			  
			  SimpleDateFormat sdf = new SimpleDateFormat(LocaleManager.getDateMask(loc));
			  
			  try {
				  
				  return sdf.parse(s);
				  
			  } catch(ParseException e) {
				  
				  return new ConversionException(e);
			  }
			  
		  } else {
			
			DateFormat df = DateFormat.getDateInstance(style, loc);
			df.setLenient(false);
			try {
				return df.parse(s);
			} catch(ParseException e) {
				throw new ConversionException(e);
			}
			
		  }
		  
		} else {
			throw new ConversionException("DateConverter can only parse strings: " + value.toString());
		}
	}
}
	
