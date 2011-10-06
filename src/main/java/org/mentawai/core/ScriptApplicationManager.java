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
package org.mentawai.core;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An ApplicationManager that runs a script in any language to configure itself.
 *
 * This class is abstract and should be extended to manage script execution from Java.
 *
 * See BshApplicationManager for an example.
 *
 * @author Sergio Oliveira
 */
public abstract class ScriptApplicationManager extends ApplicationManager {
    
    private static final char SEP = File.separatorChar;
    
    protected String filename = null;
    
    protected File file = null;
    
    protected boolean reload = true;
    
    private long ts = 0;
    
    /**
     * Set reload mode to false if you don't want this class to check
     * if the script was modified and force a reload at runtime.
     * 
     * @param reload a flag to indicate if reload mode is on or off
     */
    public void setReloadMode(boolean reload) {
        
        this.reload = reload;
        
    }
    
    void service(Context appContext, HttpServletRequest req, HttpServletResponse res) {
        
        if (!reload) return;
        
        initScriptFile();
        
        if (ts == 0) {
            
            ts = file.lastModified();
            
            return;
            
        }
        
        synchronized(this) {
            
            if (file.lastModified() != ts) {
            
                ts = file.lastModified();
                
                runScript(appContext);
                
            }
        }
    }
    
    /**
     * The default implementation of this method assumes the script will
     * be in the /APP/WEB-INF/ directory. You may override this if you want
     * to load the script file from somewhere else.
     * 
     * @param script The name of the script to be loaded
     * @return The complete path to the script file
     */
    protected String getFilename(String script) {

        StringBuffer sb = new StringBuffer(getRealPath());
        sb.append(SEP).append("WEB-INF").append(SEP).append(script);
        
        return sb.toString();
    }
    
    private void initScriptFile() {

        if (file == null) {
            
            filename = getFilename(getScriptName());
            
            file = new File(filename);
        }        
    }
    
    /**
     * Call this method to execute the configuration script.
     *
     * @param application The application context for this web application
     */
    public void runScript(Context application) {
        
        initScriptFile();
        
        reset();
        
        runScript(filename, application);
    }
    
    /**
     * Subclasses should override this method to implement the details of how to 
     * execute the script from Java. The actual script Java interpreter is called
     * here to load and run the script file.
     * 
     * @param scriptFile The absolute path to the script file.
     * @param application The application context for this web application.
     */
    public abstract void runScript(String scriptFile, Context application);
    
    /**
     * Returns the file name of the Script that implements the 
     * ApplicationManager.
     * @return the script file name.
     */
    protected abstract String getScriptName();
}

		
		