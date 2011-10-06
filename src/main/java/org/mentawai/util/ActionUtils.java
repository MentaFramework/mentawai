package org.mentawai.util;

import java.util.Map;

import org.mentawai.core.ActionConfig;
import org.mentawai.core.ApplicationManager;

public class ActionUtils {
	
    public static String getUrlFrom(ActionConfig ac) {
  	   
  	   StringBuilder sb = new StringBuilder(64);
  	   
  	   sb.append("/").append(ac.getName());
  	   
  	   if (ac.getInnerAction() != null) {
  		   
  		   sb.append(".").append(ac.getInnerAction());
  	   }
  	   
  	   if (ApplicationManager.EXTENSION != null) {
  		   
  		   sb.append(".").append(ApplicationManager.EXTENSION);
  	   }
  	   
  	   return sb.toString();
     }
    
    public static String getUrlWithContextFrom(ActionConfig ac) {
    	
    	StringBuilder sb = new StringBuilder(128);
    	
    	String context = ApplicationManager.CONTEXT_PATH;
    	
    	if (context == null) throw new IllegalStateException("Context path is not defined! Use ServletContextGrabber as a listener inside your project's web.xml!");
    	
    	sb.append(context).append(getUrlFrom(ac));
    	
    	return sb.toString();
    }
    
    public static String call(String host, ActionConfig ac, Map<String, Object> params) throws Throwable {
    	
    	return call(host, ApplicationManager.PORT, ac, params);
    }

    public static String call(String host, ActionConfig ac) throws Throwable {
    	
    	return call(host, ac, null);
    }

    
    public static String call(int port, ActionConfig ac, Map<String, Object> params) throws Throwable {
    	
    	return call("127.0.0.1", port, ac, params);
    }
    
    public static String call(int port, ActionConfig ac) throws Throwable {
    	
    	return call(port, ac, null);
    }
    
    public static String call(ActionConfig ac, Map<String, Object> params) throws Throwable {
    	
    	return call(ApplicationManager.PORT, ac, params);
    }
    
    public static String call(ActionConfig ac) throws Throwable {
    	
    	return call(ac, null);
    }
    
    public static String call(String host, int port, ActionConfig ac, Map<String, Object> params) throws Throwable {
    	
    	StringBuilder sb = new StringBuilder(256);
    	
    	sb.append("http://").append(host);
    	
    	if (port != 80) sb.append(":").append(port);
    	
    	String url = getUrlWithContextFrom(ac);
    	
    	sb.append(url);
    	
    	return IOUtils.readURL(sb.toString(), params);
    }
    
    public static String call(String host, int port, ActionConfig ac) throws Throwable {
    	
    	return call(host, port, ac, null);
    }
	
}