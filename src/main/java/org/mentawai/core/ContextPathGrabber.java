package org.mentawai.core;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextPathGrabber implements ServletContextListener {

  public void contextInitialized(ServletContextEvent e) {
	  
	  ServletContext sc = e.getServletContext();
	  
	  String s = sc.getContextPath();
	  
	  ApplicationManager.CONTEXT_PATH = s;
  }

  public void contextDestroyed(ServletContextEvent e) {

  }
}