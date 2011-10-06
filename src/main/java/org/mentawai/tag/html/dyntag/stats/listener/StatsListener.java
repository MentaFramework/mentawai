
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
package org.mentawai.tag.html.dyntag.stats.listener;

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

public class StatsListener extends BaseListener {

	public static List<String>        DIRS_FOLDER_NAME = new ArrayList<String>();
	public static ArrayList<FileTransfer>   LIST_PATH_FILES  = null;

	public void init() {

		String filesPackageDir = "/org/mentawai/tag/html/dyntag/stats/lib/";

		String fileDest = BaseTag.BASE_DIR+"stats/";

		DIRS_FOLDER_NAME = Arrays.asList(new String [] {fileDest});
		LIST_PATH_FILES = new ArrayList<FileTransfer>();
		
		if (!PREVENT_COPY) {

    		LIST_PATH_FILES.add(new FileTransfer("js","selectBox.js",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("js","jquery.js",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("js","jquery_treeview.js",filesPackageDir,fileDest));
    
    		LIST_PATH_FILES.add(new FileTransfer("css","jquery.css",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("css","screen.css",filesPackageDir,fileDest));
    
    		LIST_PATH_FILES.add(new FileTransfer("gif","bg.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","file.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","folder-closed.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","folder.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","minus.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","plus.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-black.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-black-line.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-default.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-default-line.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-famfamfam.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-famfamfam-line.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-gray.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-gray-line.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-red.gif",filesPackageDir,fileDest));
    		LIST_PATH_FILES.add(new FileTransfer("gif","treeview-red-line.gif",filesPackageDir,fileDest));
		
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
