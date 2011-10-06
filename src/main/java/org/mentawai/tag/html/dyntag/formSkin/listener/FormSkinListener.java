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
package org.mentawai.tag.html.dyntag.formSkin.listener;

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
/**
 *	@author Alex Fortuna
 *	@since 1.0
 */
public class FormSkinListener extends BaseListener {

	public static List<String>        DIRS_FOLDER_NAME = new ArrayList<String>();
	public static ArrayList<FileTransfer>   LIST_PATH_FILES  = null;
	
	// static final Strings to reduce String concatenations and more String creations.
	static final String GREENSKY_SKIN = BaseTag.BASE_DIR + "formSkin/greenSky/";
	static final String XCLEAR_SKIN = BaseTag.BASE_DIR + "formSkin/xClear/";
	static final String XPBLUE_SKIN = BaseTag.BASE_DIR + "formSkin/xpBlue/";
	static final String XPOLIVA_SKIN = BaseTag.BASE_DIR + "formSkin/xpOliva/";
	static final String POLO_SKIN = BaseTag.BASE_DIR + "formSkin/polo/";
	static final String GIF = "gif";
	static final String CSS = "css";
	static final String MTW_BUTTON = "mtwButton.gif";
	static final String MTW_FORM_BUTTON_CLOSE = "mtwFormButtonClose.gif";
	static final String MTW_FORM_SUP_CENTER = "mtwFormSupCenter.gif";
	static final String MTW_FORM_SUP_LEFT = "mtwFormSupLeft.gif";
	static final String MTW_FORM_SUP_RIGHT = "mtwFormSupRight.gif";
	static final String MTW_LEGEND = "mtwLegend.gif";
	static final String FORM_SKIN = "formSkin.css";
	
	static final String LIB_PATH_GREENSKY_SKIN = "/org/mentawai/tag/html/dyntag/formSkin/lib/greenSky/";
	static final String LIB_PATH_XCLEAR_SKIN = "/org/mentawai/tag/html/dyntag/formSkin/lib/xClear/";
	static final String LIB_PATH_XPBLUE_SKIN = "/org/mentawai/tag/html/dyntag/formSkin/lib/xpBlue/";
	static final String LIB_PATH_XPOLIVA_SKIN = "/org/mentawai/tag/html/dyntag/formSkin/lib/xpOliva/";
	static final String LIB_PATH_POLO_SKIN = "/org/mentawai/tag/html/dyntag/formSkin/lib/polo/";
	
	public void init() {
		
		DIRS_FOLDER_NAME = Arrays.asList(new String [] {BaseTag.BASE_DIR+"formSkin/"});
		LIST_PATH_FILES = new ArrayList<FileTransfer>();	
		
		if (!PREVENT_COPY) {
		
    		/* Copy files of skin greenSky */
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_BUTTON, LIB_PATH_GREENSKY_SKIN, GREENSKY_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_BUTTON_CLOSE, LIB_PATH_GREENSKY_SKIN, GREENSKY_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_CENTER, LIB_PATH_GREENSKY_SKIN, GREENSKY_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_LEFT, LIB_PATH_GREENSKY_SKIN, GREENSKY_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_RIGHT, LIB_PATH_GREENSKY_SKIN, GREENSKY_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(CSS, FORM_SKIN, LIB_PATH_GREENSKY_SKIN, GREENSKY_SKIN));
    		
    		/* Copy files of skin xClear */
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_BUTTON, LIB_PATH_XCLEAR_SKIN, XCLEAR_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_BUTTON_CLOSE, LIB_PATH_XCLEAR_SKIN, XCLEAR_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_CENTER, LIB_PATH_XCLEAR_SKIN, XCLEAR_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_LEFT, LIB_PATH_XCLEAR_SKIN, XCLEAR_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_RIGHT, LIB_PATH_XCLEAR_SKIN, XCLEAR_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(CSS, FORM_SKIN, LIB_PATH_XCLEAR_SKIN, XCLEAR_SKIN));
    		
    		/* Copy files of skin xpBlue */
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_BUTTON, LIB_PATH_XPBLUE_SKIN, XPBLUE_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_BUTTON_CLOSE, LIB_PATH_XPBLUE_SKIN, XPBLUE_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_CENTER, LIB_PATH_XPBLUE_SKIN, XPBLUE_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_LEFT, LIB_PATH_XPBLUE_SKIN, XPBLUE_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_RIGHT, LIB_PATH_XPBLUE_SKIN, XPBLUE_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_LEGEND, LIB_PATH_XPBLUE_SKIN, XPBLUE_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(CSS, FORM_SKIN, LIB_PATH_XPBLUE_SKIN, XPBLUE_SKIN));
    		
    		/* Copy files of skin xpOliva */
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_BUTTON, LIB_PATH_XPOLIVA_SKIN, XPOLIVA_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_BUTTON_CLOSE, LIB_PATH_XPOLIVA_SKIN, XPOLIVA_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_CENTER, LIB_PATH_XPOLIVA_SKIN, XPOLIVA_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_LEFT, LIB_PATH_XPOLIVA_SKIN, XPOLIVA_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_RIGHT, LIB_PATH_XPOLIVA_SKIN, XPOLIVA_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_LEGEND, LIB_PATH_XPOLIVA_SKIN, XPOLIVA_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(CSS, FORM_SKIN, LIB_PATH_XPOLIVA_SKIN, XPOLIVA_SKIN));
    		
    		/* Copy files of skin xpOliva */
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_BUTTON, LIB_PATH_POLO_SKIN, POLO_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_BUTTON_CLOSE, LIB_PATH_POLO_SKIN, POLO_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_CENTER, LIB_PATH_POLO_SKIN, POLO_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_LEFT, LIB_PATH_POLO_SKIN, POLO_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_FORM_SUP_RIGHT, LIB_PATH_POLO_SKIN, POLO_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(GIF, MTW_LEGEND, LIB_PATH_POLO_SKIN, POLO_SKIN));
    		LIST_PATH_FILES.add(new FileTransfer(CSS, FORM_SKIN, LIB_PATH_POLO_SKIN, POLO_SKIN));
		
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
