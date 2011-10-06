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
package org.mentawai.util;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class HttpUtils {

	private static final String ENC = "UTF-8";
	
	public static void disableCache(HttpServletResponse res) {
		
		res.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		res.setHeader("Pragma", "no-cache"); // HTTP 1.0
		res.setDateHeader("Expires", 0); // prevents caching at the proxy server
		
	}
	
    public static String encodeParam(String param) {
        
        try {
            
            return URLEncoder.encode(param, ENC);
            
        } catch(Throwable e) {
            
            throw new RuntimeException(e);
        }
    }
	
    public static String convertToQueryString(Map<String, Object> params) {
        
        return convertToQueryString(params, (String[]) null);
    }
    
    public static String convertToQueryString(Map<String, Object> params, String ... before) {
        
        StringBuilder sb = new StringBuilder(1024);
        
        if (before != null) {
            
            for(String s: before) {
            
                sb.append(s);
            }
        }
        
        for(String key: params.keySet()) {
            
            Object value = params.get(key);
            
            if (value instanceof List<?>) {
                
               List<?> list = (List<?>) value;
               
               for(Object o: list) {
                   
                   sb.append(key).append('=');
                   
                   sb.append(encodeParam(o.toString()));
                   
                   sb.append('&');
               }
               
            } else {
                
                sb.append(key).append('=');
                
                sb.append(encodeParam(value.toString()));
                
                sb.append('&');
            }
        }
        
        // remove last '&'
        
        if (sb.length() > 0) {
            
            char last = sb.charAt(sb.length() - 1);
            
            if (last == '&') {
                
                return sb.substring(0, sb.length() - 1);
            }
            
        }
        
        return sb.toString();
        
    }
}
