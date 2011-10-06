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

import java.util.List;

public abstract class BaseConfig extends Base {	
    
	private static final long serialVersionUID = 1L;
    
	public StringBuffer buldImportJsFile(List<FileTransfer> listPathFiles){
		StringBuffer results = new StringBuffer();		
		for(int i=0; i< listPathFiles.size(); i++){
			FileTransfer fileTransfer = listPathFiles.get(i);
			if(fileTransfer.getFileType().equals("js")){ 
				results.append("\t<script");
				prepareAttribute(results,"type","text/javascript");
				prepareAttribute(results,"language","JavaScript");
				prepareAttribute(results,"src",req.getContextPath()+ fileTransfer.getFileDestination()+fileTransfer.getFileName());
				results.append(" >");
				results.append("</script>\n");
			}			   
		}
		return results;
	}	
	public StringBuffer buldImportCssFile(List<FileTransfer> listPathFiles){
		StringBuffer results = new StringBuffer();		
		for(int i=0; i< listPathFiles.size(); i++){
			FileTransfer fileTransfer = listPathFiles.get(i);
			if(fileTransfer.getFileType().equals("css")){
				results.append("\t<link ");
				prepareAttribute(results,"href",req.getContextPath()+ fileTransfer.getFileDestination()+fileTransfer.getFileName());
				results.append(" type=\"text/css\" rel=\"stylesheet\"");
				results.append(" />");			    
			}			   
		}
		return results;
	}		
}