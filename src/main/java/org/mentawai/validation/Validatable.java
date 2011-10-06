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
package org.mentawai.validation;

/**
 * An action should implement this interface if it wants to define its rules for validation.
 * 
 * Another approach would be to have a ValidationFilter separated from the Action to do this.
 *
 * Some people would rather decouple the validation setup from the action. Other people would rather
 * put everything inside the same class. Mentawai offers the two approaches to let you decide for your own
 * which is the best for your need.
 *
 * @author Sergio Oliveira
 */
public interface Validatable {
    
	/**
	 * Prepares the validator, adding rules that should be applied to fields.
	 * 
	 * @param validator
	 * @param innerAction
	 */
    public void prepareValidator(Validator validator, String innerAction);
    
}

