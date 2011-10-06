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
package org.mentawai.tag.html.dyntag;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class BaseListener implements ServletContextListener {
	
	protected static boolean PREVENT_COPY = true;
	
	public abstract void init();
	/**
	 *	Handles finding all CSS, JavaScript, and image resources from this
	 *	taglib's JAR file and copies them to a directory in the web app's
	 *	root directory where the tab components can link to them.
	 */
	public abstract void contextInitialized(ServletContextEvent evt);
	
	public abstract void contextDestroyed(final ServletContextEvent evt);
	
	protected void createComponentDir(final ServletContext servletContext,List<FileTransfer> listPathFiles) {
		try {
			   for(int i=0; i< listPathFiles.size(); i++){
				   File dir = null;
				   FileTransfer fileTransfer = listPathFiles.get(i);
				   String dirPath = servletContext.getRealPath((String)fileTransfer.getFileDestination());	 				
				   boolean exists = (dir = new File(dirPath)).exists();
				   if(!exists)
				   dir.mkdirs();
			   }			
			   
		} catch (Exception e) {
            
            e.printStackTrace();
		}
	}
	
	public static void createLibraries(boolean b) {
		PREVENT_COPY = !b;
	}
	
	protected void writeFile(final URL fromURL,
			final String toPath,
			final ServletContext servletContext) {
		InputStream in = null;
		OutputStream out = null;
		try {
         
         if (fromURL == null || toPath == null) return;
         
			in = new BufferedInputStream(fromURL.openStream());
			out = new BufferedOutputStream(new FileOutputStream(toPath));
			int len;
			byte [] buffer = new byte [4096];
			while ((len = in.read(buffer,0,buffer.length)) != -1) {
				out.write(buffer,0,len);
			}
			out.flush();
		} catch (Exception e) {
			
            e.printStackTrace();
            
		} finally {
			try { in.close(); out.close(); } catch (Exception e) { }
		}
	}
	
	
	
		
}