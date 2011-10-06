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
package org.mentawai.tag.html.dyntag.formSkin;

import java.util.ArrayList;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.html.dyntag.BaseConfig;
import org.mentawai.tag.html.dyntag.BaseTag;
import org.mentawai.tag.html.dyntag.FileTransfer;
/**
 *	@author Alex Fortuna
 *	@since 1.0
 */
public class FormSkinConfig extends BaseConfig {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<FileTransfer>   LIST_PATH_FILES  = null;
	private String skin = "greenSky";
	
	/**
	 * @return Returns the skin.
	 */
	public String getSkin() {
		return skin;
	}

	/**
	 * @param skin The skin to set.
	 */
	public void setSkin(String skin) {
		this.skin = skin;
	}

	/** Method to build tag */	
	protected StringBuffer buildTag() {
		LIST_PATH_FILES = new ArrayList<FileTransfer>();		
                
        /* greenSky */
        if(getSkin().trim().toUpperCase().equals("GREENSKY")){        
            LIST_PATH_FILES.add(new FileTransfer("css","formSkin.css",null,BaseTag.BASE_DIR+"formSkin/greenSky/"));
        }
        
        /* xClear */
        if(getSkin().trim().toUpperCase().equals("XCLEAR")){        
            LIST_PATH_FILES.add(new FileTransfer("css","formSkin.css",null,BaseTag.BASE_DIR+"formSkin/xClear/"));
        }
        
        /* xpBlue */
        if(getSkin().trim().toUpperCase().equals("XPBLUE")){        
            LIST_PATH_FILES.add(new FileTransfer("css","formSkin.css",null,BaseTag.BASE_DIR+"formSkin/xpBlue/"));
        }
        
        /* xpOliva */
        if(getSkin().trim().toUpperCase().equals("XPOLIVA")){        
            LIST_PATH_FILES.add(new FileTransfer("css","formSkin.css",null,BaseTag.BASE_DIR+"formSkin/xpOliva/"));
        }
        
        /* polo */
        if(getSkin().trim().toUpperCase().equals("POLO")){        
            LIST_PATH_FILES.add(new FileTransfer("css","formSkin.css",null,BaseTag.BASE_DIR+"formSkin/polo/"));
        }
        
        /* Verifica se o skin esta vazio. 
         * Caso esteja um valor padrao e setado.
         * */
        if(LIST_PATH_FILES.size() == 0){
        	LIST_PATH_FILES.add(new FileTransfer("css","formSkin.css",null,BaseTag.BASE_DIR+"formSkin/xClear/")); 	
        }
        
		StringBuffer results = new StringBuffer();
		//results.append(buldImportJsFile(FormSkinListener.LIST_PATH_FILES).toString());
		results.append(buldImportCssFile(LIST_PATH_FILES).toString());
		return results;  
	}
	
	public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}
}
