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

import java.util.Map;

/**
 * An action can implement this interface when it wants to define
 * the rules for input values conversion.
 *
 * The ConverterFilter will detect that an action is Convertable and will
 * try to convert its values. Another option would be to create your own
 * ConversionFilter.
 *
 * @author Sergio Oliveira
 */
public interface Convertable {
    
   /**
    * Prepare the converters that will be used to convert the values of the action input.
    * 
    * @param converters
    * @param innerAction
    */
    public void prepareConverters(Map<String, Converter> converters, String innerAction);
    
}