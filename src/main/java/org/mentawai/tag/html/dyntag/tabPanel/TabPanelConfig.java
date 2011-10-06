/*
 * Created on 06/04/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mentawai.tag.html.dyntag.tabPanel;

import java.util.ArrayList;

import javax.servlet.jsp.JspException;

import org.mentawai.core.Controller;
import org.mentawai.tag.html.dyntag.BaseConfig;
import org.mentawai.tag.html.dyntag.BaseTag;
import org.mentawai.tag.html.dyntag.FileTransfer;
import org.mentawai.tag.html.dyntag.tabPanel.listener.TabPanelListener;
/**
 * @author Alex Fortuna
 */
public class TabPanelConfig extends BaseConfig {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<FileTransfer>   LIST_PATH_FILES  = null;
	private String skin = "winXp";
	
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
                
        /* winXp */
        if(getSkin().trim().toUpperCase().equals("WINXP")){        
            LIST_PATH_FILES.add(new FileTransfer("css","tab.css",null,BaseTag.BASE_DIR+"tabPanel/winXp/"));
        }
        /* win2k */
        if(getSkin().trim().toUpperCase().equals("WIN2K")){        
            LIST_PATH_FILES.add(new FileTransfer("css","tab.css",null,BaseTag.BASE_DIR+"tabPanel/win2k/"));
        }
        /* webFx */
        if(getSkin().trim().toUpperCase().equals("WEBFX")){        
            LIST_PATH_FILES.add(new FileTransfer("css","tab.css",null,BaseTag.BASE_DIR+"tabPanel/webFx/"));
        }
        
        /* mac */
        if(getSkin().trim().toUpperCase().equals("MAC")){        
            LIST_PATH_FILES.add(new FileTransfer("css","tab.css",null,BaseTag.BASE_DIR+"tabPanel/mac/"));
        }
        
        /* winXpRed */
        if(getSkin().trim().toUpperCase().equals("WINXPRED")){        
            LIST_PATH_FILES.add(new FileTransfer("css","tab.css",null,BaseTag.BASE_DIR+"tabPanel/winXpRed/"));
        }
        
        /* Verifica se o skin esta vazio. 
         * Caso esteja um valor padrao e setado.
         * */
        if(LIST_PATH_FILES.size() == 0){
        	LIST_PATH_FILES.add(new FileTransfer("css","tab.css",null,BaseTag.BASE_DIR+"tabPanel/winXp/")); 	
        }
        
		StringBuffer results = new StringBuffer();
		
		if (TabPanelListener.LIST_PATH_FILES == null) {
			
			TabPanelListener listener = new TabPanelListener();
			
			listener.contextInitialized(Controller.getApplication());
			
		}
		
		results.append(buldImportJsFile(TabPanelListener.LIST_PATH_FILES).toString());
		results.append(buldImportCssFile(LIST_PATH_FILES).toString());
		
		return results;  
	}
	
	public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}
}
