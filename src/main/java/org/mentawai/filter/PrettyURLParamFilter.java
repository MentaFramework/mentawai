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

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.PrettyURLController;

/**
 * A Filter for injecting in the action input the parameters supplied by the
 * PrettyURLController in separated attributes.
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * 
 * @see PrettyURLController
 */
public class PrettyURLParamFilter implements Filter {
	
	public static final String CHECK_PARAM = "isPrettyURL";

	private String[] paramsOrder;

	public PrettyURLParamFilter(String ... paramOrder) {
		this.paramsOrder = paramOrder;
	}

	public void destroy() {

	}

	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		Input input = action.getInput();
		
		boolean isPrettyURL = false;

		for (int i = 0; i < paramsOrder.length; i++) {
			
			String key = String.valueOf(i);
			
			if (!input.hasValue(key)) break;
			
			input.setValue(paramsOrder[i], input.getValue(key));
			
			input.removeValue(key);
			
			isPrettyURL = true;
		}
		
		if (isPrettyURL) {
			input.setValue(CHECK_PARAM, true); // let the action now we have received a pretty url
		}
		
		return chain.invoke();
	}

}
