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
package org.mentawai.tag.authorization;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.util.ConditionalTag;
import org.mentawai.util.StringUtils;

/**
 * Check if user logged is on JAAS role.
 * @author helio frota
 *
 */
public class IsUserInRole extends ConditionalTag {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 3738775998405568039L;
	
	/**
	 * Attribute roles of IsUserInRole tag.
	 */
	private String roles;
	
	/**
	 * Default method testCondition of ConditionalTag.
	 * @return boolean
	 */
	public boolean testCondition() throws JspException {
		
		boolean isUserInRole = false;
		
		if (!StringUtils.isEmpty(roles)) {
			
			roles = roles.trim();
			
			String[] roleArray = roles.split(",");
			
			for (String s : roleArray) {
				
				if (req.isUserInRole(s.trim())) {
					
					isUserInRole = true;
					
					break;
					
				}
				
			}
			
		}
		
		return isUserInRole;
	}

	/**
	 * Gets roles of IsUserInRole.
	 * @return String
	 */
	public String getRoles() {
		return roles;
	}

	/**
	 * Sets roles of IsUserInRole.
	 */
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	

}
