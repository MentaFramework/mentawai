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
package org.mentawai.core;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

/**
 * A simple wrapper for a FileItem commons upload.
 * 
 * @author Sergio Oliveira Jr.
 */
public class FileUploaded {
	
	private final FileItem fileItem;
	
	public FileUploaded(FileItem fileItem) {
		this.fileItem = fileItem;
	}
	
	public void deleteTempFile() {
		fileItem.delete();
	}
	
	public byte[] toByteArray() {
		return fileItem.get();
	}
	
	public String getContentType() {
		return fileItem.getContentType();
	}
	
	public String getFieldName() {
		return fileItem.getFieldName();
	}
	
	public String getFilename() {
		return fileItem.getName();
	}
	
	public long getSize() {
		return fileItem.getSize();
	}
	
	public String asString() {
		return fileItem.getString();
	}
	
	public String asString(String encoding) {
		try {
			return fileItem.getString(encoding);
		} catch(Exception e) {
			throw new org.mentawai.util.RuntimeException(e);
		}
	}
	
	public boolean isInMemory() {
		return fileItem.isInMemory();
	}
	
	public InputStream getInpuStream() throws IOException {
		return fileItem.getInputStream();
	}
	
	public FileItem getFileItem() {
		return fileItem;
	}
}