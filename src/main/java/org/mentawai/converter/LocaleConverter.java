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

import java.util.Locale;

import org.mentawai.core.Action;
import org.mentawai.core.Input;

/**
 * A converter that needs to know the user locale to perform the conversion.
 * You should override this class to implement your own converters.
 *
 * @author Sergio Oliveira
 */
public abstract class LocaleConverter implements Converter {
	
    /**
     * Convert an object to another one using the use locale.
     *
     * @param value The object to convert.
     * @param loc The user locale to use in the convertion.
     * @return The new object converted.
     * @throws ConversionException if the conversion fail for any reason.
     */
	public abstract Object convert(Object value, Locale loc) throws ConversionException;
	
	/**
	 * Override this method if you want to convert null (not-present) values.
	 * 
	 * Default is FALSE.
	 * 
	 * @return If this converter should try to convert NULL values.
	 * @since 1.11
	 */
	protected boolean allowNull() {
		
		return false;
	}
    
    public Object convert(String field, Action action) throws ConversionException {
        Input input = action.getInput();
        Object value = input.getValue(field);
        if (value == null && !allowNull()) throw new ConversionException("Cannot convert null!");
        Locale loc = action.getLocale();
        return convert(input.getValue(field), loc);
    }    
	
}
	
