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
package org.mentawai.ajax.renderer;

import java.util.Locale;

import net.sf.json.JSONObject;

import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;

/**
 * A Ajax Render that gets a Java Object and transforms it into a JSON Object.
 * The render expect an object in the actions output, identified by the key
 * "object" by default, but you can change it.
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class JSONObjectAjaxRenderer implements AjaxRenderer {

    //private static final String JSONOBJECT_KEY = "object";

    public JSONObjectAjaxRenderer() {
    	
    }

    public String encode(Object obj, Locale loc, boolean pretty) throws Exception {

    	JSONObject json = JSONObject.fromObject(obj);
    	
    	if (pretty) {
    		
    		return json.toString(3);
    		
    	} else {
    		
    		return json.toString();
    	}
    	
    	
    }

    public String getContentType() {
        return APP_JSON;
    }
    
    public String getCharset() {
       return AjaxConsequence.DEFAULT_CHARSET;
    }
    
    public static class User {
        
        private String username;
        private int age;
        
        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
        
        public String getUsername() { return username; }
        
        public int getAge() { return age; }
    }
    
    public static void main(String[] args) throws Exception {
        
        User u = new User("sergio", 34);
        
        AjaxRenderer renderer = new JSONObjectAjaxRenderer();
        
        System.out.println(renderer.encode(u, null, true));
    }

}
