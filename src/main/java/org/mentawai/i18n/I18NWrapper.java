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
package org.mentawai.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

class I18NWrapper {
  
    public static long TIME = LocaleManager.I18N_RELOAD_TIME;
    
    private long ts = 0L;  
    private long lm = 0;
    private I18N i18n = null;
    private File file = null;
    private String resource = null;
  
    public I18NWrapper(File file) {
        this.file = file;
    }
    
    public I18NWrapper(String resource) {
    	this.resource = resource;
    }
    
    private void reloadFromFile() {
    	
    	FileInputStream fis = null;
    	InputStreamReader isr = null;
    	
        try {
            if (!file.exists()) {
                this.i18n = null;
                return;
            }
            
            fis = new FileInputStream(file);
            
            isr = new InputStreamReader(fis, I18N.CHARSET);
            
            Properties prop = new Properties();
            prop.load(isr);
            fis.close();
            this.lm = file.lastModified();
            this.i18n = new I18N(prop);
            
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
        	
        	if (isr != null) try { isr.close(); } catch(Exception e) { }
        	if (fis != null) try { fis.close(); } catch(Exception e) { }
        }
    }   
    
    private void loadFromClasspath() {
    	
    	InputStream is = null;
    	
    	InputStreamReader isr = null;
    	
    	try {
    		
    		is = getClass().getResourceAsStream(resource);
    		
    		isr = new InputStreamReader(is, I18N.CHARSET);
    		
        	Properties prop = new Properties();
        	prop.load(isr);

        	this.i18n = new I18N(prop);
    		
    	} catch(IOException e) {
    		
    		e.printStackTrace();
    		
    	} finally {
    		
    		if (isr != null) try { isr.close(); } catch(Exception e) { }
    		if (is != null) try { is.close(); } catch(Exception e) { }
    		
    	}
    	
    }
  
    private boolean needsUpdate(boolean check) {
    	
    	if (resource != null) {
    		
    		return i18n == null;
    		
    	}
    	
        long ctm = System.currentTimeMillis();
        if (check) {
            if ((ctm - ts) > TIME) {
                ts = ctm;
            } else {
                return false;
            }
        }
        return i18n == null || file.lastModified() != lm;
    }
  
    public I18N getI18N() {
    	
        if (needsUpdate(true)) {
        	
            synchronized(file) {
                if (needsUpdate(false)) {
                	
                	if (resource != null) {
                		
                		loadFromClasspath();
                		
                	} else {
                	
                		reloadFromFile();
                		
                	}
                }
            }
        }
        return i18n;
    }
}
