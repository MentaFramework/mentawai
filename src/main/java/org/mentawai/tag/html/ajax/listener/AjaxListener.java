package org.mentawai.tag.html.ajax.listener;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.mentawai.tag.html.dyntag.BaseListener;
import org.mentawai.tag.html.dyntag.FileTransfer;

/**
 * 
 * @author Robert Willian Gil
 *
 */
public class AjaxListener extends BaseListener {

	public static List<FileTransfer>   LIST_PATH_FILES  = null;
	
	private static boolean PREVENT_COPY = true;
	
	@Override
	public void init() {
		LIST_PATH_FILES = new LinkedList<FileTransfer>();
		
		if (!PREVENT_COPY) {
		
			LIST_PATH_FILES.add(new FileTransfer("css","auto_complete.css","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			LIST_PATH_FILES.add(new FileTransfer("js","prototype.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			LIST_PATH_FILES.add(new FileTransfer("js","mentaAjax.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			LIST_PATH_FILES.add(new FileTransfer("js","util.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			LIST_PATH_FILES.add(new FileTransfer("js","effects.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			LIST_PATH_FILES.add(new FileTransfer("js","ac.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			LIST_PATH_FILES.add(new FileTransfer("js","mentaInPlace.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
		}
		
	}
	
	public static void createLibraries(boolean b) {
		PREVENT_COPY = !b;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent evt) {
		contextInitialized(evt.getServletContext());
	}
	
	public void contextInitialized(ServletContext servletContext) {
		this.init();
		createComponentDir(servletContext,LIST_PATH_FILES);
		
		for (int j=0;j< LIST_PATH_FILES.size();j++) {
			FileTransfer fileTransfer = LIST_PATH_FILES.get(j);
			URL fileOrigin         = getClass().getResource(fileTransfer.getFileOrigin()+fileTransfer.getFileName());
			String fileDestination = servletContext.getRealPath(fileTransfer.getFileDestination()+fileTransfer.getFileName()); 
			boolean exists = (new File(fileDestination)).exists();
			if(!exists){
				writeFile(fileOrigin,fileDestination,servletContext);	
			}			 
		}
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

}
