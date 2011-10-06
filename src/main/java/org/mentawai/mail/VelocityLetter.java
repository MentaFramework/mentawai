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
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mentawai.core.ApplicationManager;

public class VelocityLetter implements Letter {
	
	public static String CHARSET = "UTF-8";
    
    private static final String DEF_DIR = "letters";
    private static final String SEP = File.separator;
    
    private static VelocityEngine ve = null;
    
    private String filename;
    private VelocityContext vc = new VelocityContext();
    private Map<Locale, Template> cacheBody = new HashMap<Locale, Template>();
    private Map<Locale, String> cacheSubject = new HashMap<Locale, String>();
    private String dir = DEF_DIR;
    
    public VelocityLetter(String filename) {
        if (ve == null) {
            ve = new VelocityEngine();
            Properties p = new Properties();
            p.setProperty("file.resource.loader.path", ApplicationManager.getRealPath());
            p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
            try {
            	ve.init(p);
            } catch(Exception e) {
            	throw new RuntimeException(e);
            }
        }
        this.filename = filename;
    }
    
    public VelocityLetter(String filename, String dir) throws Exception {
        this(filename);
        this.dir = dir;
    }

    public void setAttribute(String name, Object value) {
        vc.put(name, value);
    }
        
    public String getText(Locale loc) throws Exception {
        Template t = cacheBody.get(loc);
        if (t == null) {
            StringBuffer sb = new StringBuffer();
            
            //sb.append(ApplicationManager.getRealPath()).append(SEP).append(dir).append(SEP);
            sb.append(SEP).append(dir).append(SEP);
            
            if (loc != null) {
            
            	sb.append(loc).append(SEP);
            }
            
            sb.append(filename);
            
            t = ve.getTemplate(sb.toString(), CHARSET);
            
            cacheBody.put(loc, t);
        }
        
        StringWriter sw = new StringWriter();
        t.merge(vc, sw);
        return cutFirstLine(sw.toString());
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
       
       return s;
    }
    
    public String getSubject() throws Exception {
       
       return getSubject(null);
    }

}
