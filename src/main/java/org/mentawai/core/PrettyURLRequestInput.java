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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrettyURLRequestInput extends RequestInput {
	
	public PrettyURLRequestInput(HttpServletRequest req, HttpServletResponse res) {
		
		// super will process the parameters as usual...
		
		super(req, res);
		
		// get the parameters from the request...
		
		String context = req.getContextPath();

		String uri = req.getRequestURI().toString();

		// remove the context from the uri, if present

		if (context.length() > 0 && uri.indexOf(context) == 0) {

			uri = uri.substring(context.length());

		}

		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {

			uri = uri.substring(1);

		}
		
		// cut last '/'
		if (uri.endsWith("/") && uri.length() > 1) {
			
			uri = uri.substring(0, uri.length() - 1);
		}
		
		String[] s = uri.split("/");
		
		if (s.length > 2) {
			
			int total = s.length - 2;
			
			map.put("prettyUrlParamsCount", total);
			
			for(int i=0;i<total;i++) {
				map.put(String.valueOf(i), s[i + 2]);
				keys.add(String.valueOf(i));
			}
		}
	}
}