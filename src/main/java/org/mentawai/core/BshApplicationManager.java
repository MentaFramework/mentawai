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

import java.io.IOException;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;

/**
 *  A ScriptApplicationManager that executes a BeanShell script file to configure itself.
 *
 * @author Sergio Oliveira
 */
public class BshApplicationManager extends ScriptApplicationManager {
    
    /** The name of the script file. */
    public static final String DEFAULT_SCRIPT_NAME = "ApplicationManager.bsh";
    
    /** The method name inside the script file to execute. */
    public static final String METHOD_NAME = "config(appMgr, application)";
    
    /**
     * Here we are just calling setScriptName(DEFAULT_SCRIPT_NAME) and runScript(application).
     *
     * @param application The application context for this web application.
     */
    public void init(Context application) {

        runScript(application);        
        
    }
    
    protected String getScriptName() {
        
        return DEFAULT_SCRIPT_NAME;
        
    }
    
    protected void prepareScript(Interpreter i) throws EvalError {
        
        i.set("SUCCESS", SUCCESS);
        i.set("ERROR", ERROR);
        i.set("LOGIN", LOGIN);
        i.set("ACCESSDENIED", ACCESSDENIED);

        i.set("ACTION", ACTION);
        i.set("REQUEST", REQUEST);

        i.set("INPUT", INPUT);
        i.set("OUTPUT", OUTPUT);
        i.set("SESSION", SESSION);
        i.set("APPLICATION", APPLICATION);
    
        i.set("EXCEPTION", EXCEPTION);
    
        i.set("REDIR", REDIR);
        
        i.eval("fwd(a) { return new Forward(a); }");
        
        i.eval("redir(a) { return new Redirect(a); }");

    }
    
    public void runScript(String scriptFile, Context application) {
        
        try {
            
            Interpreter i = new Interpreter();
            
            i.source(scriptFile);
            
            i.set("application", application);
            
            i.set("appMgr", this);
            
            prepareScript(i);
            
            i.eval(METHOD_NAME);
            
        } catch(IOException e) {
            
            e.printStackTrace();
            
            throw new DSLException("Error reading the bsh script " + scriptFile);
            
        } catch(TargetError e) {
            
            e.printStackTrace();
            
            throw new DSLException("Error executing the bsh script " + scriptFile + ": " + e.getTarget());
            
        } catch(EvalError e) {
            
            e.printStackTrace();
            
            throw new DSLException("Error validating the bsh script " + scriptFile);
            
        }
    }
}

		
		