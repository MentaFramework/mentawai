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

import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;

public class JsonRenderer implements AjaxRenderer {

	public JsonRenderer() {
		
	}
	
	public String encode(Object obj, Locale loc, boolean pretty) throws Exception {
		
		JSONObject json = null;
		
		JSONArray jsonArray = null;
		
		if (obj instanceof JSONObject) {
			
			json = (JSONObject) obj; // ready to go...
			
		} else if (obj instanceof List) {
			
			// convert to JSON array by default...
			
			jsonArray = JSONArray.fromObject(obj);
			
		} else {
			
			json = JSONObject.fromObject(obj); // convert anything to JSON !!! OBS: Be careful with Hibernate lazy loading...
		}
		
		if (pretty) {
			
			if (jsonArray != null) {
		
				return jsonArray.toString(3);
				
			} else {
				
				return json.toString(3);
			}
			
		} else {
			
			if (jsonArray != null) {
			
				return jsonArray.toString();
				
			} else {
				
				return json.toString();
			}
		}
	}

    public String getContentType() {
        return APP_JSON;
    }
    
    public String getCharset() {
       return AjaxConsequence.DEFAULT_CHARSET;
    }
    
//    private static class Obj1 {
//    	
//    	private int id;
//    	private String username;
//    	private Obj2 obj2;
//    	
//		public int getId() {
//        	return id;
//        }
//		public void setId(int id) {
//        	this.id = id;
//        }
//		public String getUsername() {
//        	return username;
//        }
//		public void setUsername(String username) {
//        	this.username = username;
//        }
//		public Obj2 getObj2() {
//        	return obj2;
//        }
//		public void setObj2(Obj2 obj2) {
//        	this.obj2 = obj2;
//        }
//    }
//    
//    private static class Obj2 {
//    	
//    	private int age;
//    	private String name;
//		public int getAge() {
//        	return age;
//        }
//		public void setAge(int age) {
//        	this.age = age;
//        }
//		public String getName() {
//        	return name;
//        }
//		public void setName(String name) {
//        	this.name = name;
//        }
//    }
//    
//    public static void main(String[] args) throws Exception {
//        
//        Map<String, Object> map = new HashMap<String, Object>();
//        
//        map.put("success", true);
//        
//        Map<String, Object> data = new HashMap<String, Object>();
//        
//        data.put("key1", "data1");
//        data.put("key2", "data2");
//        data.put("key3", "data3");
//        
//        Map<String, Object> data2 = new HashMap<String, Object>();
//        
//        data2.put("pela1", "xxxx");
//        data2.put("pela2", "yyyy");
//        
//        data.put("key4", data2);
//        
//        map.put("data", data);
//        
//        AjaxRenderer renderer = new JSONMapAjaxRenderer();
//        
//        System.out.println(renderer.encode(map, null, true));
//        
//        Obj2 o2 = new Obj2();
//        o2.age = 35;
//        o2.name = "Sergio";
//        
//        Obj1 o1 = new Obj1();
//        o1.id = 1;
//        o1.username = "saoj";
//        o1.obj2 = o2;
//        
//        System.out.println("======>\n" + JSONObject.fromObject(o1));
//    }
    
}
