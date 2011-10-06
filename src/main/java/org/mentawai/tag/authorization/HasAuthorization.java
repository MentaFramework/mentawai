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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.authorization.AuthorizationManager;
import org.mentawai.authorization.Group;
import org.mentawai.tag.util.ConditionalTag;

/**
 * @author Sergio Oliveira
 */
public class HasAuthorization extends ConditionalTag {

    private List<String> groups;
    private List<String> permissions;
    private List<Group> listGroups;
    private boolean both = false;

    public void setGroup(String group) {
    	this.groups = new ArrayList<String>(1);
    	this.groups.add(group.trim());
    }
    
    public void setGroups(String groups) {
        StringTokenizer st = new StringTokenizer(groups, ",");
        this.groups = new ArrayList<String>(st.countTokens());
        while(st.hasMoreTokens()) {
            this.groups.add(st.nextToken().trim());
        }
    }

    public void setPermission(String permission) {
        setPermissions(permission);
    }

    public void setBoth(boolean both) {
    	if (this.permissions!=null && this.permissions.size()>0) {
    		this.both = both;
    	}else{
    		this.both = false;
    	}
    }

    public void setPermissions(String permissions) {
        StringTokenizer st = new StringTokenizer(permissions, ",");
        this.permissions = new ArrayList<String>(st.countTokens());
        while(st.hasMoreTokens()) {
            this.permissions.add(st.nextToken().trim());
        }
    }

    /**
     * Verify if the parameters of the groups or permissions exist or are not denied for the user
     *
     *
     * @return int
     */
    public boolean testCondition() throws JspException {

        List usergroups = BaseLoginAction.getUserGroups(session);

        boolean haveGroup = false;
        boolean havePermission = false;

        if (usergroups == null || usergroups.size() == 0) {
			return false;
		}

        if (groups != null && groups.size() > 0) {

            for(int i=0;i<groups.size();i++) {
                for(int j=0;j<usergroups.size();j++) {

                    if (usergroups.get(j) instanceof String) {

                		if ( groups.get(i).toString().equalsIgnoreCase(usergroups.get(j).toString()) ) {
                			haveGroup = true;

                		} else if (groups.get(i).toString().equalsIgnoreCase("!"+usergroups.get(j).toString())) {
               				return false;
            			}

                    } else if ( usergroups.get(j) instanceof Group ) {

                    	Group g = (Group) usergroups.get(j);

                    	if ( groups.get(i).toString().equalsIgnoreCase(g.getName()) ) {
                    		haveGroup = true;

                		}else if (groups.get(i).toString().equalsIgnoreCase("!"+g.getName())) {

               				return false;
            			}

                    } else if (usergroups.get(j) instanceof List) {

                		listGroups = (List<Group>) usergroups.get(j);
                    	for(int k=0;k<listGroups.size();k++) {

                    		Group g = (Group) listGroups.get(k);

                    		if ( groups.get(i).toString().equalsIgnoreCase(g.getName().toString() ) ) {
                    			haveGroup = true;

                    		}else if (groups.get(i).toString().equalsIgnoreCase("!"+g.getName().toString())) {
                   				return false;
                			}

                    		if (haveGroup) {
                    			k = listGroups.size();
                    		}
                    	}

                    }
                }
            }
        }

        if (permissions != null && permissions.size() > 0) {

        	for(int j=0;j<usergroups.size();j++) {

                if (usergroups.get(j) instanceof String) {
                	if (AuthorizationManager.checkAny(usergroups.get(j).toString(), permissions)) {
                		havePermission = true;
                    }

                } else if (usergroups.get(j) instanceof Group) {

                	Group g = (Group) usergroups.get(j);
                	if (AuthorizationManager.checkAny(g, permissions)) {
                		havePermission = true;
                    }

                } else if (usergroups.get(j) instanceof List) {
                	listGroups = (List<Group>) usergroups.get(j);
					for(int k=0;k<listGroups.size();k++) {
                		 Group g = (Group) listGroups.get(k);
                		 if (AuthorizationManager.checkAny(g, permissions)) {
                			 havePermission = true;
                         }else{
                        	 havePermission = false;
                         }
                	 }
                }

            }

        }

        if (both) {
        	if (haveGroup && havePermission){
        		return true;
        	}else{
        		return false;
        	}
        } else {
        	if (haveGroup || havePermission){
        		return true;
        	}else{
        		return false;
        	}
        }

    }

}