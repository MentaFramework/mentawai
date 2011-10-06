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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class ServiceController extends Controller {

    private static final long serialVersionUID = 4167319994749699926L;
    
    protected String extension = null;
    
    private String ext = null;

    @Override
    public void init(ServletConfig conf) throws ServletException {

        super.init(conf);
        
        String extension = conf.getInitParameter("extension");
        
        if (extension != null) {
            
            this.extension = extension;
            
            this.ext = "." + extension;
        }
        
    }
    
    protected final String getRequestURI(HttpServletRequest req) {
        
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
        
        // remove extension if present...
        
        if (extension != null && uri.endsWith(ext)) {
            
            uri = uri.substring(0, uri.length() - ext.length());
        }
        
        return uri;
    }
    
    @Override
    protected String getActionName(HttpServletRequest req) {
        
        String uri = getRequestURI(req);
        
        String[] s = uri.split("/");
        
        if (s.length > 0) {
            
            String res = s[0];
            
            if (res.length() > 1 && res.charAt(0) >= 'a' && res.charAt(0) <= 'z') {
                
                // capitalize first letter...
                
                res = res.substring(0, 1).toUpperCase() + res.substring(1);
            }
            
            return res;
        }
        
        return null;
        
    }

    @Override
    protected String getInnerActionName(HttpServletRequest req) {

        String uri = getRequestURI(req);
        
        String[] s = uri.split("/");
        
        if (s.length > 0) {
            
            return s[s.length - 1];
        }
        
        return null;
    }
}
