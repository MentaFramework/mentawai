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
package org.mentawai.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.mentaregex.Regex;
import org.mentawai.core.ApplicationManager;
import org.mentawai.util.IOUtils;

public class TextLetter implements Letter {
	
	public static String CHARSET = "UTF-8";
    
    private static final String DEF_DIR = "letters";
    private static final String SEP = File.separator;
    
    private String filename;
    private Map<String, String> props = new HashMap<String, String>();
    
    private Map<Locale, String> cacheBody = new HashMap<Locale, String>();
    private Map<Locale, String> cacheSubject = new HashMap<Locale, String>();
    
    private String dir = DEF_DIR;
    
    public TextLetter(String filename) {
        this.filename = filename;
    }
    
    public TextLetter(String filename, String dir) {
        this(filename);
        this.dir = dir;
    }

    public void setAttribute(String name, Object value) {
        props.put(name, value.toString());
    }
        
    public String getText(Locale loc) throws Exception {
    	
        String t = cacheBody.get(loc);
        
        if (t == null) {
        	
            StringBuffer sb = new StringBuffer();
            
            sb.append(ApplicationManager.getRealPath()).append(SEP).append(dir).append(SEP);
            
            if (loc != null) {
            
            	sb.append(loc).append(SEP);
            }
            
            sb.append(filename);
            
            t = IOUtils.readFile(sb.toString(), CHARSET);
            
            cacheBody.put(loc, t);
        }
        
        String res = merge(t);

        return cutFirstLine(res);
    }
    
    private String merge(String text) {
    	
    	String result = new String(text);
    	
    	for(String key : props.keySet()) {
    		
    		String value = props.get(key);
    		
    		result = Regex.sub(result, "s/#$" + key + "/" + Regex.escapeSlash(value, "#") +"/g", '#');
    	}
    	
    	return result;
    }
    
    private String cutFirstLine(String s) {
       
       int index = s.indexOf('\n') + 1;
       
       if (index < s.length()) {
          
          return s.substring(index, s.length());
          
       } else if (index == s.length()) {
          
          return " ";
          
       } else {
          
          return s;
       }
    }
    
    public String getText() throws Exception {
       return getText(null);
     }
    
    public String getSubject(Locale loc) throws Exception {
       
       String s = cacheSubject.get(loc);
       
       if (s == null) {
          
          StringBuffer sb = new StringBuffer();
          
          sb.append(ApplicationManager.getRealPath()).append(SEP).append(dir).append(SEP);
          
          if (loc != null) {
          
        	  sb.append(loc).append(SEP);
        	  
          }
          
          sb.append(filename);
          
          BufferedReader br = null;
          
          try {
          
        	  br = new BufferedReader(new InputStreamReader(new FileInputStream(sb.toString()), CHARSET));
          
        	  s = br.readLine();
        	  
          } finally {
        	  
        	  if (br != null) try { br.close(); } catch(Exception e) { }
          }
          
          cacheSubject.put(loc, s);
       }
       
       return merge(s);
    }
    
    public String getSubject() throws Exception {
       
       return getSubject(null);
    }

}
