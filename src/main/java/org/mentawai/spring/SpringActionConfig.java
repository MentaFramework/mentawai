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

import org.mentawai.core.Action;
import org.mentawai.core.ActionConfig;
import org.mentawai.core.Controller;
import org.mentawai.core.PojoAction;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * ActionConfig for Spring support. The static method setBeanFactory specify the BeanFactory that
 * will be used by getAction method. By default, find a WebApplicationContext configured in web.xml
 * 
 * @author Davi Luan Carneiro
 */
public class SpringActionConfig extends ActionConfig {

	private static BeanFactory beanFactory = null;
	private String beanName = null;
	
	static {
		try {
			beanFactory = WebApplicationContextUtils.getWebApplicationContext(Controller.getApplication());
		} catch (Exception e) {}
	}
	
	public SpringActionConfig(String beanName) {
		super(Object.class);
		this.beanName = beanName;
	}
	
	public SpringActionConfig(String name, String beanName) {
		super(name, Object.class);
		this.beanName = beanName;
	}
	
	public SpringActionConfig(String name, String beanName, String innerAction) {
		super(name, Object.class, innerAction);
		this.beanName = beanName;
	}
		
	public static void setBeanFactory(BeanFactory beanFactory) {
		SpringActionConfig.beanFactory = beanFactory;
	}

	public Action getAction() {
		
		Object instance = beanFactory.getBean(beanName);
		
	     if ( instance instanceof Action ) {
	    	 
	       return (Action) instance;
	       
	     } else {
	    	 
	       return new PojoAction( instance );
	       
	     }
	}
    
	@Override
    public Class<? extends Object> getActionClass() {
        return beanFactory.getType(beanName);
    }
}
