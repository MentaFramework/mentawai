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
package org.mentawai.tag.html;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.util.ConditionalTag;

/**
 * @author Sergio Oliveira
 */
public class IsBrowser extends ConditionalTag {
    
    private String value;
    
    public void setValue(String value) {
        
        this.value = value;
        
    }
    
    public boolean testCondition() throws JspException {
       
       String userAgent = req.getHeader("user-agent");
       
       if (userAgent == null || userAgent.equals("")) return false;
       
       if (value.equalsIgnoreCase("Firefox")) {
          
          if (userAgent.indexOf("Firefox") > 0) return true;
          
       } else if (value.equalsIgnoreCase("IE6")) {
          
          if (userAgent.indexOf("MSIE 6") > 0) return true;
          
       } else if (value.equalsIgnoreCase("IE7")) {
          
          if (userAgent.indexOf("MSIE 7") > 0) return true;
          
       } else if (value.equalsIgnoreCase("Safari")) {
          
          if (userAgent.indexOf("Safari") > 0) return true;
          
       } else if (value.equalsIgnoreCase("Opera")) {
          
          if (userAgent.indexOf("Opera") > 0) return true;
          
       } else {
          
          throw new JspException("Invalid argument for isBrowser tag!");
          
       }
       
       return false;
    }
}