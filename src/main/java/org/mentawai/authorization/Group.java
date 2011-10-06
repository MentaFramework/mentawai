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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author Sergio Oliveira
 */
public class Group implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
    private String name;

    private Set<Permission> permissions;

    public Group(String name) {
    	this.id = -1;
        this.name = name;
    }

    public Group(int id, String name) {
    	this.id = id;
    	this.name = name;

    }

    public Group(String name, Set<Object> permissions) {

        this(name);

        this.permissions = getPermissions(permissions);
    }

    public Group(int id, String name, Set<Object> permissions) {

        this(id, name);

        this.permissions = getPermissions(permissions);
    }

    public Group(int id, String name, String ... permissions) {

    	this(id, name);

    	if (permissions != null) {

    		this.permissions = new HashSet<Permission>();

    		for(int i=0;i<permissions.length;i++) {

    			this.permissions.add(new Permission(permissions[i]));

    		}
    	}

    }

    public Group(String name, String ... permissions) {

    	this(-1, name, permissions);
    }

    public Group(int id, String name, Permission ... permissions) {

    	this(id, name);

    	if (permissions != null) {

    		this.permissions = new HashSet<Permission>();

    		for(int i=0;i<permissions.length;i++) {

    			this.permissions.add(permissions[i]);

    		}
    	}
    }

    public Group(String name, Permission ... permissions) {

    	this(-1, name, permissions);
    }

    /**
     * Use varargs instead...
     *
     * @param name
     * @param permissions
     * @deprecated
     */
    public Group(String name, String permissions) {
        this(name);
        StringTokenizer st = new StringTokenizer(permissions, ",");
        this.permissions = new HashSet<Permission>(st.countTokens());
        while(st.hasMoreTokens()) {
            this.permissions.add(new Permission(st.nextToken().trim()));
        }
    }

    public void setId(int id) {
    	this.id = id;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public int getId() {
    	return id;
    }

    private static Set<Permission> getPermissions(Set<Object> permissions) {

    	Set<Permission> set = new HashSet<Permission>();

    	Iterator<Object> iter = permissions.iterator();

    	while(iter.hasNext()) {

    		Object obj = iter.next();

    		if (obj instanceof Permission) {

    			set.add((Permission) obj);

    		} else if (obj instanceof String) {

    			set.add(new Permission((String) obj));

    		} else {

    			throw new IllegalArgumentException("Not a string or a permission: " + obj);
    		}
    	}

    	return set;
    }


    public Group addPermission(String permission) {
        return addPermission(new Permission(permission));
    }

    public Group addPermission(Permission p) {
        if (permissions == null) {
            permissions = new HashSet<Permission>();
        }
        permissions.add(p);
        return this;
    }

    public String getName() {
        return name;
    }

	/**
	 * Verify if the permission exists in the group and if it is not denied
	 * @return boolean
	 * @since 1.12
	 */
    public boolean hasPermission(String permission) {
        if (permissions == null) return false;
        Iterator<Permission> iter = permissions.iterator();
        while(iter.hasNext()) {
            Permission p = iter.next();

            if (permission.startsWith("!")){
            	if (p.getName().equalsIgnoreCase(permission.substring(1)))
            		return false;
            } else {
            	if (p.getName().equalsIgnoreCase(permission))
                    return true;
            }
       }
       return false;
    }

    public boolean hasPermission(Permission p) {
        if (permissions == null) return false;
        return permissions.contains(p);
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
        if (obj instanceof Group) {
            Group g = (Group) obj;

            if (g.id != -1 && this.id != -1) {

            	return g.id == this.id;
            }

            if (g.name.equalsIgnoreCase(this.name)) return true;

        }
        return false;
    }
}

