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

import java.util.Locale;

/**
 * This is a simple interface to format an object from the action output.
 * 
 * Check one of its implementations that comes with Mentawai before implementing this class.
 *
 * @author Sergio Oliveira
 */
public interface Formatter {
    
    /**
     * Formats an output object from this action.
     *
     * @param value The value to format
     * @param loc The locale to use (if needed)
     * @return The value formatted to a String
     */
    public String format(Object value, Locale loc);
	
}
	
