
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
package org.mentawai.tag.html.dyntag.selectBox.listener;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.mentawai.tag.html.dyntag.BaseListener;
import org.mentawai.tag.html.dyntag.BaseTag;
import org.mentawai.tag.html.dyntag.FileTransfer;

public class SelectBoxListener extends BaseListener {

	public static List<String>        DIRS_FOLDER_NAME = new ArrayList<String>();
	public static ArrayList<FileTransfer>   LIST_PATH_FILES  = null;
	
	public void init() {
				
		DIRS_FOLDER_NAME = Arrays.asList(new String [] {BaseTag.BASE_DIR+"selectBox/"});
		LIST_PATH_FILES = new ArrayList<FileTransfer>();	
		
		if (!PREVENT_COPY) {
		
			LIST_PATH_FILES.add(new FileTransfer("js","selectBox.js","/org/mentawai/tag/html/dyntag/selectBox/lib/",BaseTag.BASE_DIR+"selectBox/"));
		}
	}
	
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


	public void contextDestroyed(ServletContextEvent evt) {
		// TODO Auto-generated method stub

	}

}
