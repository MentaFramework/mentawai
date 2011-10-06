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
package org.mentawai.ioc;


/**
 * Defines an IoC component.
 * A Mentawai IoC component returns instances of a given class to be injected in the action by the IoCFilter and InjectionFilter.
 *
 * @author Sergio Oliveira
 */
public interface Bean {
  
    /**
     * Returns an instance of the class this component represents.
     *
     * @return An instance of the class this component represents.
     * @throws InstantiationException if for any reason the instance cannot be created.
     */
    public Object getBean() throws InstantiationException;
    
    public Class<? extends Object> getType();
}
