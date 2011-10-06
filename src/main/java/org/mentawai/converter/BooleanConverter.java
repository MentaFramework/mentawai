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


/**
 * @author Sergio Oliveira
 */
public class BooleanConverter extends BasicConverter {
	
	private final boolean notPresentIsFalse;
	
	public BooleanConverter() {
		
		this(false);
	}
	
	public BooleanConverter(boolean notPresentIsFalse) {
		
		this.notPresentIsFalse = notPresentIsFalse;
	}
	
	@Override
	protected boolean allowNull() {
		
		return true;
	}
	
	public Object convert(Object value) throws ConversionException {
		
		if (value == null && notPresentIsFalse) {
			
			return Boolean.FALSE;
		}
		
		if (value == null) {
			
			throw new ConversionException("Cannot convert null value!");
		}
		
        String s = value.toString();
        
        if (s.equalsIgnoreCase("true") || s.equals("on")) return Boolean.TRUE;
        
        if (s.equalsIgnoreCase("false")) return Boolean.FALSE;
        
        int x = -1;
        
        try {
            x = Integer.parseInt(s);
            
            if (x == 0) return Boolean.FALSE;
            else if (x == 1) return Boolean.TRUE;
            
        } catch(Exception e) {
            throw new ConversionException("Cannot convert " + s + " to a boolean!");
        }
        
        throw new ConversionException("Cannot convert the number " + x + " to a boolean!");
    }
}
	
