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
package org.mentawai.authorization;

import java.io.Serializable;

/**
 * @author Sergio Oliveira
 */
public class Permission implements Serializable {
    
	private int id;
    private String name;
    
    public Permission(String name) {
    	this.id = -1;
        this.name = name;
    }
    
    public Permission(int id, String name) {
    	this.id = id;
    	this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
    	if (id != -1) {
    		
    		return id + ": " + name;
    	}
        return name;
    }
    
    public int hashCode() {
    	
    	if (id != -1) return id;
    	
        return name.hashCode();
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            Permission p = (Permission) obj;
            
            if (p.id != -1 && this.id != -1) {
            	
            	return p.id == this.id;
            }
            
            if (p.name.equalsIgnoreCase(this.name)) return true;
        }
        return false;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
	
