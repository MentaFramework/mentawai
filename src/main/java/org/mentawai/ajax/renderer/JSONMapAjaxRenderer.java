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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONObject;

import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;
import org.mentawai.core.ConsequenceException;

public class JSONMapAjaxRenderer implements AjaxRenderer {

	//public static final String JSON_MAP_ATTR = "json_map";
	
	public JSONMapAjaxRenderer() {
		
	}
	
	public String encode(Object obj, Locale loc, boolean pretty) throws Exception {
		
		JSONObject json;
		
		if (obj instanceof Map) {
			
			//json = JSONObject.fromMap((Map) obj);
			
			json = JSONObject.fromObject(obj);
			
		} else if (obj instanceof JSONObject) {
			
			json = (JSONObject)obj;
			
		} else {
			
			throw new ConsequenceException("Value is not a Collection or JSONObject: " + obj);
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
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("success", true);
        
        Map<String, Object> data = new HashMap<String, Object>();
        
        data.put("key1", "data1");
        data.put("key2", "data2");
        data.put("key3", "data3");
        
        Map<String, Object> data2 = new HashMap<String, Object>();
        
        data2.put("pela1", "xxxx");
        data2.put("pela2", "yyyy");
        
        data.put("key4", data2);
        
        map.put("data", data);
        
        AjaxRenderer renderer = new JSONMapAjaxRenderer();
        
        System.out.println(renderer.encode(map, null, true));
    }
    
}
