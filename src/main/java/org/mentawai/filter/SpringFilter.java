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
package org.mentawai.filter;

import org.mentawai.core.Controller;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;
import org.mentawai.spring.SpringInput;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Filter to substitute an regular action input for a InputSpring. Can be used as a global filter.
 * By default, find a WebApplicationContext configured in web.xml
 * 
 * @author Davi Luan Carneiro
 */
public class SpringFilter implements Filter {

	private BeanFactory beanFactory;
	
	public SpringFilter() {
		this.beanFactory = WebApplicationContextUtils.getWebApplicationContext(Controller.getApplication());
	}
	
	public SpringFilter(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	public String filter(InvocationChain chain) throws Exception {
		SpringInput spring = new SpringInput(chain.getAction().getInput(), beanFactory);
		chain.getAction().setInput(spring);
		return chain.invoke();
	}

	public void destroy() {}
}
