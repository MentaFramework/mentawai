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
 * A filter intercepts an action so it can modify its input and output, before and after the action is executed. 
 * Filters are very useful for validation, authentication, value objects, file upload, etc.
 * 
 * @author Sergio Oliveira
 */
public interface Filter {
	
	/**
	 * Executes the filter.
	 * 
	 * @param chain The InvocationChain for the action this filter is being applied to.
	 * @return The result of the filter or the action the filter is being applied to.
	 */
	public String filter(InvocationChain chain) throws Exception;
    
    /**
     * Gives a chance to the filter to deallocalte any resources before it is destroyed.
     * This is called when the web application is stopped, in other words,
     * this has nothing to do with garbage collection.
     */
    public void destroy();
	
}