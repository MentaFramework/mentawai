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
package org.mentawai.velocity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.mentawai.util.CharResponseWrapper;

public class VelocityServletFilter implements Filter {
    
    private static VelocityEngine ve = null;
    
    private FilterConfig filterConfig;
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        PrintWriter writer = res.getWriter();        
        CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse) res);
        chain.doFilter(req, wrapper);
        
        StringReader reader = new StringReader(wrapper.toString());
        Context ctx = new JSPContext((HttpServletRequest) req, filterConfig.getServletContext());
        try {
            ve.evaluate(ctx, writer, "VelocityServletFilter", reader);
        } catch(Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String resourceLoaderPath = filterConfig.getInitParameter("resourceLoaderPath");
        
        if (ve == null) {
            try {
                
                ve = new VelocityEngine();
                
                if (resourceLoaderPath != null) {
                
                    ClassLoader classLoader =  Thread.currentThread().getContextClassLoader();                
                    URL url = classLoader.getResource(resourceLoaderPath);
                    File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
        
                    
                    ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
                    ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, file.getAbsolutePath());
                    ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");
                    ve.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
                
                }

                ve.init();
            } catch(Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    
    public void destroy() { }

}



