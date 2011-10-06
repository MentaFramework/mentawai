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
package org.mentawai.core;

/**
 * Give the actions a chance to configure itself inside the action class
 * instead of having to configure the ApplicationManager for every action.
 * 
 * OBS: Although this is pratical as you are doing everything in a single place (inside the action class)
 * this will tie configuration to code, pretty much like annotations do. It is up to you to decide
 * which approach (configuration inside the application manager or configuration inside action)
 * is better for your taste, religion and project.
 * 
 * @author Sergio Oliveira
 *
 */
public interface Configurable {
    
    public void configure(ApplicationManager appManager);
    
}