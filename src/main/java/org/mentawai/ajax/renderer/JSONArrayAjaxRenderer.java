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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import net.sf.json.JSONArray;

import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;

/**
 * Expect a java.util.Collection or a JSONArray in the output.
 * 
 * @author Davi Luan Carneiro
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class JSONArrayAjaxRenderer implements AjaxRenderer {

    //public static final String JSON_ARRAY_ATTR = "json_array";

    public JSONArrayAjaxRenderer() {
    	
    }

    public String encode(Object obj, Locale loc, boolean pretty) throws Exception {
    	
        JSONArray json;
        
        if (obj instanceof Collection) {
        	
            //json = new JSONArray((Collection) obj);
            
            json = JSONArray.fromObject(obj);
            
        } else if (obj instanceof JSONArray) {
        	
            json = (JSONArray) obj;
            
        } else {
        	
            throw new IllegalArgumentException("Value is not a Collection or JSONArray: " + obj);
        }
        
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
    
    public static void main(String[] args) throws Exception {
        
        Collection<String> coll = new ArrayList<String>(3);
        
        coll.add("Sergio");
        coll.add("Rubem");
        coll.add("Robert");
        
        AjaxRenderer renderer = new JSONArrayAjaxRenderer();
        
        System.out.println(renderer.encode(coll, null, true));
        
    }
}
