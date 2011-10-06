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

import java.util.Date;
/**
 * A converter for converting a java.util.Date to a java.sql.Date.
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 *
 */
public class UtilToSQLDateConverter extends BasicConverter {
    
    /**
     * Converts a java.util.Date to a java.sql.Date.
     * @throws ConversionException id the param value is not a java.util.Date instance.
     * @param value the value to be converted
     * @return the java.sql.Date converted
     */
	public Object convert(Object value) throws ConversionException {
        
		if (!(value instanceof Date)) {
			throw new ConversionException("The given value is not a java.util.Date object!");
		}
        
		Date date = (Date) value;

		java.sql.Date newDate = new java.sql.Date(date.getTime());

		return newDate;
	}
}