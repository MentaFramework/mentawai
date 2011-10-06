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

import java.util.Map;

import org.mentawai.core.Action;

/**
 * An interface that describes a validation rule.
 *
 * @author Sergio Oliveira
 */
public interface Rule {
    
    /**
     * Returns a map with tokens that can be used in the error messages.
     * The tokens in the error messages will be replaced by the token values in this map.
     * 
     * @return A map with the token values.
     */
	public Map<String, String> getTokens();
    
    /**
     * Check and validate the given field from the give action.
     *
     * @param field The field to validate.
     * @param action The action from where to get the field.
     * @return true if the validation succeeds.
     */
    public boolean check(String field, Action action);
    
}
