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
public class ClassMessageContext extends AbstractMessageContext {
	
	private String path;
	
	public ClassMessageContext(Object obj) {
		this(obj.getClass());
	}
	
	public ClassMessageContext(Class<? extends Object> klass) {
		this(klass, null);
	}
    
    public ClassMessageContext(Class<? extends Object> klass, String dir) {
		if (dir != null) setDir(dir);
        StringBuffer sb = new StringBuffer(getDir() + "/");
		String file = klass.getName();
		int x = file.lastIndexOf(".");
		if (x != -1) {
			file = file.substring(x + 1);
		}
        sb.append(file);
        this.path = sb.toString();
    }
	
	protected String getPath() {
		return path;
	}

}

    
    
