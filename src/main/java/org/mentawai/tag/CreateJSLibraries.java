package org.mentawai.tag;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.mentawai.tag.html.ajax.listener.AjaxListener;
import org.mentawai.tag.html.dyntag.BaseListener;

public class CreateJSLibraries implements ServletContextListener {

  public void contextInitialized(ServletContextEvent e) {
	  
	  AjaxListener.createLibraries(true);
	  BaseListener.createLibraries(true);
  }

  public void contextDestroyed(ServletContextEvent e) {

  }
}