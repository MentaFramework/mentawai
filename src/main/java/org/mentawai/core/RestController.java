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

public class RestController extends Controller {

    private static final long serialVersionUID = 416731934749699926L;
    
    public static final String getRequestURI(HttpServletRequest req) {
        
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

        // cut the last '/'
        if (uri.endsWith("/") && uri.length() > 1) {

            uri = uri.substring(0, uri.length() - 1);
        }
        
        return uri;
    }
    
    @Override
    protected String getActionName(HttpServletRequest req) {
        
        String uri = getRequestURI(req);
        
        String[] s = uri.split("/");
        
        if (s.length > 0) {
        	return s[0];
        }
        
        return null;
        
    }

    @Override
    protected String getInnerActionName(HttpServletRequest req) {

        return null;
    }
}
