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
package org.mentawai.jruby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyObject;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.ext.posix.util.Platform;
import org.jruby.internal.runtime.methods.DynamicMethod;
import org.jruby.javasupport.JavaEmbedUtils;
import org.mentawai.core.ApplicationManager;

public class JRubyInterpreter implements Runnable {
	
	public static long RELOAD_INTERVAL = 5000; // 5s... 
	
	private static JRubyInterpreter instance = null;

    private Ruby runtime;
    private RubyRuntimeAdapter evaler;
    
    private Object rubyLoader = null;
    
    private final Thread thread;
    
    private volatile boolean running;
    
    private JRubyInterpreter() throws IOException {
    	
    	// set env variables...
    	
        boolean isWindows = Platform.IS_WINDOWS;
        
        String SEP = isWindows ? "\\" : "/";
    	
    	String jruby_home = System.getenv("JRUBY_HOME");
    	String jruby_lib = System.getenv("JRUBY_LIB");
    	String jruby_shell = System.getenv("JRUBY_SHELL");
    	String jruby_script = System.getenv("JRUBY_SCRIPT");
    	String jruby_opts = System.getenv("JRUBY_OPTS");
    	
    	if (jruby_home != null) System.setProperty("jruby.home", jruby_home);
    	
    	if (jruby_lib != null) {
    		System.setProperty("jruby.lib", jruby_lib);
    	} else if (jruby_home != null) {
			System.setProperty("jruby.lib", jruby_home + SEP + "lib");
    	}
    	
    	if (jruby_shell != null) {
    		System.setProperty("jruby.shell", jruby_shell);
    	} else {
    		if (isWindows) {
    			System.setProperty("jruby.shell", "cmd.exe");	
    		} else {
    			System.setProperty("jruby.shell", "/bin/sh");
    		}
    	}
    	
    	if (jruby_script != null) {
    		System.setProperty("jruby.script", jruby_script);
    	} else {
    		if (isWindows) {
    			System.setProperty("jruby.script", "jruby.bat");
    		} else {
    			System.setProperty("jruby.script", "jruby");
    		}
    	}
    	
    	String realPath = ApplicationManager.getRealPath();
        
        List<String> loadPaths = new ArrayList<String>();
	    loadPaths.add(".");
	    loadPaths.add(realPath + SEP + "WEB-INF" + SEP + "ruby");
	    
        runtime = JavaEmbedUtils.initialize( loadPaths );
        
        evaler = JavaEmbedUtils.newRuntimeAdapter();
        
    	// require rubygems...
    	
    	if (jruby_home != null && jruby_opts != null && jruby_opts.indexOf("-rubygems") != -1) {
    		
    		require("rubygems");
    	}

    	// UTF through -Ku
    	
    	if (jruby_home != null && jruby_opts != null && jruby_opts.indexOf("-K") != -1) {
    		
    		int start = jruby_opts.indexOf("-K");
    		
    		String s = jruby_opts.substring(start + 2);
    		
    		int end = s.indexOf(" ");
    		
    		if (end > 0) s = s.substring(0, end);
    		
    		eval("$KCODE = '" + s + "'");
    	}
        
    	loadFileFromClasspath("org/mentawai/jruby/loader.rb");
    	
    	loadFileFromClasspath("org/mentawai/jruby/action_manager.rb");
    	
    	loadFileFromClasspath("org/mentawai/jruby/utils.rb");
    	
    	loadFileFromClasspath("org/mentawai/jruby/ruby_action.rb");
    	
    	rubyLoader = eval("Mentawai::JRuby::Loader.new('" + realPath + "')");
    	
    	reloadFiles();
        	
    	thread = new Thread(this);
    	
    	running = true;
    	
    	thread.setDaemon(true);
    	
    	thread.start();
    }
    
    public void run() {
    	
    	while(running) {
    		
    		try {
    			
    			Thread.sleep(RELOAD_INTERVAL);
    			
    		} catch(InterruptedException e) {
    			
    			// will be called by close();
    			
    		}
    		
    		try {
    		
    			if (running) reloadFiles();
    			
    		} catch(Exception e) {
    			
    			e.printStackTrace();
    		}
    	}
    }
    
    public void close() {
    	
    	running = false;
    	
    	thread.interrupt();
    	
    	try {
    		
    		thread.join();
    		
    	} catch(Exception e) {
    		
    		e.printStackTrace();
    	}
    }
    
    /**
     * Return a list of all setters for this ruby object.
     * A Ruby setter has the form of name= (ends with =)
     * 
     * @param rubyObject the ruby object from where to get the setters
     * @return A list of setters (method names)
     */
    public List<String> getSetters(Object rubyObject) {
    	
    	RubyClass rubyClass = ((RubyObject) rubyObject).getMetaClass();
    	
    	Map<String, DynamicMethod> map = rubyClass.getMethods();
    	
    	List<String> setters = new ArrayList<String>(map.size());
    	
    	Iterator<String> iter = map.keySet().iterator();
    	
    	while(iter.hasNext()) {
    		
    		String m = iter.next();
    		
    		if (m.length() <= 1 || m.equals("==") || m.equals("===")) continue;
    		
    		if (m.endsWith("=")) setters.add(m);
    	}
    	
    	return setters;
    }
    
    private void reloadFiles() {
		call(rubyLoader, "reloadFiles");
    }
    
    public void require(String file) {
    	eval("require '" + file + "'");
    }
    
    public RubyObject eval(String code) {
    	
    	Object obj = evaler.eval( runtime, code );
    	
    	if (obj == null) return null;
    	
    	if (obj instanceof RubyObject) {
    		return (RubyObject) obj;
    	}
    	
    	// hopefully this should never happen because everything in Ruby is an object...
    	// but I may be missing something here...
    	
    	System.err.println("Object created by eval is not a RubyObject: " + obj.getClass());
    	
    	return null;
    }
    
    public String callAction(Object rubyObject, String methodName) {
    	Object obj = call(rubyObject, methodName);
        return obj != null ? obj.toString() : null;
    }
    
    // Call generic ruby methods...
    
    private static final Object[] EMPTY = new Object[0];
    
    private Object callMethodImpl(Object rubyObject, String methodName, Object[] params, Class<? extends Object> returnType) {
    	
    	String[] s = methodName.split("\\.");
    	
    	if (s.length == 1) {
    	
    		return JavaEmbedUtils.invokeMethod(runtime, rubyObject, methodName, params, returnType);
    		
    	} else {
    		
    		String m1 = s[0]; // first method...
    		String m2 = getMethods(s); // remaining methods...
    		
    		Object temp = JavaEmbedUtils.invokeMethod(runtime, rubyObject, m1, EMPTY, Object.class);
    		
    		return callMethodImpl(temp, m2, params, returnType); // recursive...
    	}
    }
    
    /**
     * Call a method on a Ruby object. (no arguments)
     * 
     * @param rubyObject The ruby object in the Java side
     * @param methodName The name of the method to call
     * @return Any object created and returned by the method
     */
    public Object call(Object rubyObject, String methodName) {
    	return callMethodImpl(rubyObject, methodName, EMPTY, Object.class);
    }
    
    /**
     * Call a method on a Ruby object. (no arguments but forcing the return type)
     * 
     * @param rubyObject The ruby object in the Java side
     * @param methodName The name of the method to call
     * @param returnType The return type for the return value of the method
     * @return Any object created and returned by the method
     */
    public Object call(Object rubyObject, String methodName, Class<? extends Object> returnType) {
    	return callMethodImpl(rubyObject, methodName, EMPTY, returnType);
    }
    
    /**
     * Call a method on a Ruby object passing some arguments to the ruby side.
     * 
     * @param rubyObject The ruby object in the Java side
     * @param methodName The name of the method to call
     * @param params The arguments to pass to the method
     * @return Any object created and returned by the method
     */
    public Object call(Object rubyObject, String methodName, Object ... params) {
    	return callMethodImpl(rubyObject, methodName, params, Object.class);
    }
    
    /**
     * Call a method on a Ruby object passing some arguments to the ruby side. (force the return type)
     * 
     * @param rubyObject The ruby object in the Java side
     * @param methodName The name of the method to call
     * @param returnType The return type for the return value of the method 
     * @param params The arguments to pass to the method
     * @return Any object created and returned by the method
     */
    public Object call(Object rubyObject, String methodName, Class<? extends Object> returnType, Object ... params) {
    	return callMethodImpl(rubyObject, methodName, params, returnType);
    }
    
    /**
     * Call a setter for this property. It just appends '=' to construct the method name to use for the setter.
     * 
     * Ex: set(rubyObject, "name", "hi") is equivalent to call(rubyObject, "name=", "hi")
     * 
     * @param rubyObject The ruby object in the Java side
     * @param propName The name of the property to set
     * @param value The value of the property
     * @return Any object created and returned by the method
     */
    public Object set(Object rubyObject, String propName, Object value) {
    	return call(rubyObject, propName + "=", value);
    }
    
    /**
     * Call Ruby's respond_to? to find out if the Ruby object responds to a method.
     * 
     * @param rubyObject The ruby object in the Java side
     * @param methodName The method name
     * @return true if the ruby object responds to that method
     */
    public boolean respondTo(Object rubyObject, String methodName) {
    	Object res = JavaEmbedUtils.invokeMethod(runtime, rubyObject, "respond_to?", new Object[] { methodName }, Object.class);
    	if (res instanceof Boolean) {
    		return (Boolean) res;
    	}
    	throw new RuntimeException("Problem calling respond_to?!");
    }
    
    public static boolean isInitialized() {
    	return instance != null;
    }
    
    public synchronized static JRubyInterpreter getInstance() {
    	if (instance == null) {
    		try {
    			instance = new JRubyInterpreter();
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
    	}
    	return instance;
    }
    
    public void loadFileFromClasspath(String filename) throws IOException {
    	
    	InputStream is = getResourceAsStream(filename);
    	
    	if (is == null) throw new IOException("Cannot find resource: " + filename);
    	
    	StringBuilder sb = new StringBuilder(1024);
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(is));
    	
    	String line;
    	
    	while((line = br.readLine()) != null) {
    		
    		sb.append(line).append('\n');
    	}
    	
    	eval(sb.toString());
    }
    
    private InputStream getResourceAsStream(String name) {
    	
        InputStream inStream = null;
        
        Class<? extends Object> c = getClass();
        
        ClassLoader classLoader = c.getClassLoader();
        
        if (classLoader != null) {
        	
          inStream = classLoader.getResourceAsStream(name);
          
        }
        
        if (inStream == null) {
        	
          inStream = ClassLoader.getSystemResourceAsStream(name);
          
        }
        
        return inStream;
       
   } 
    
    public Ruby getRuntime() {
    	return runtime;
    }
    
    void removeAction(long id) {
    	
    	StringBuilder sb = new StringBuilder(64);
    	
    	sb.append("Mentawai::JRuby::ActionManager.instance.del_action(").append(id).append(")");
    	
    	eval(sb.toString());
    	
    }
    
    Object createAction(long id, String ruby_klass_name) {
    	
    	StringBuilder sb = new StringBuilder(64);
    	
    	sb.append("Mentawai::JRuby::ActionManager.instance.add_action(").append(id);
    	sb.append(", '").append(ruby_klass_name).append("')");
    	
    	return eval(sb.toString());
    }
    
    Object getAction(long id) {
    	
		StringBuilder sb = new StringBuilder(64);
    	
    	sb.append("Mentawai::JRuby::ActionManager.instance.get_action(").append(id).append(")");
    	
    	return eval(sb.toString());
    	
    }
    
    private static String getMethods(String[] s) {
    	
    	StringBuilder sb = new StringBuilder(32);
    	
    	for(int i = 1; i < s.length; i++) {
    		
    		if (i > 1) sb.append('.');
    		
    		sb.append(s[i]);
    	}
    	
    	return sb.toString();
    	
    }    
}