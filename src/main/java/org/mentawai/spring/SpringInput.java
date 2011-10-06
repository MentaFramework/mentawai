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
package org.mentawai.spring;

import org.mentawai.core.Input;
import org.mentawai.core.InputWrapper;
import org.springframework.beans.factory.BeanFactory;

/**
 * Input with built-in Spring support. It will look for the object in the Spring Bean Factory 
 * if it cannot find it in the action input.
 * 
 * @author Davi Luan Carneiro
 */
public class SpringInput extends InputWrapper {
    
    public static boolean DEBUG = false;

	private BeanFactory beanFactory = null;
	
	public SpringInput(Input input, BeanFactory beanFactory) {
		super(input);
		this.beanFactory = beanFactory;
	}

	public Object getValue(String name) {
        
		Object value = super.getValue(name);
        
        if (value != null) {
        	return value;
        }
        
        try {
        
            value = beanFactory.getBean(name);
        
            setValue(name, value);
            
        } catch(Exception e) {
            
            if (DEBUG) e.printStackTrace();
            
        }
        
		return value;
	}
}