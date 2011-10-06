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

public class FileTransfer {
    
	private String fileType        = null;
	private String fileName        = null;
	private String fileOrigin      = null;
	private String fileDestination = null;
	
	public FileTransfer(String fileType,String fileName,String fileRes,String fileDest){
		this.fileType = fileType;
		this.fileName = fileName;
		this.fileOrigin  = fileRes;
		this.fileDestination = fileDest;
	}
	
	public String getFileType() {
		return fileType;
	}
	public String getFileDestination() {
		return fileDestination;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFileOrigin() {
		return fileOrigin;
	}
}