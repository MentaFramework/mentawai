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
package org.mentawai.message;


/**
 * @author Sergio Oliveira
 */
public class FileMessageContext extends AbstractMessageContext {
	
	private String path;
	
	public FileMessageContext(String file) {
		this(file, null);
	}
    
    public FileMessageContext(String file, String dir) {
		if (dir != null) setDir(dir);
        if ((file.startsWith("/") || file.startsWith("\\")) && file.length() > 1) {
            file = file.substring(1);
        }
        StringBuffer sb = new StringBuffer(getDir() + "/");
        sb.append(file);
        this.path = sb.toString();
    }
	
	protected String getPath() {
		return path;
    }
}

    
    