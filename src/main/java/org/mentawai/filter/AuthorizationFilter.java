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
package org.mentawai.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.ajax.AjaxAction;
import org.mentawai.authorization.Authorizable;
import org.mentawai.authorization.AuthorizationManager;
import org.mentawai.authorization.Group;
import org.mentawai.authorization.Permission;
import org.mentawai.core.Action;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;

/**
 * A filter to handle user authorization.
 * You should use this filter to protect your actions from unauthorized access.
 *
 * @author Sergio Oliveira
 */
public class AuthorizationFilter implements Filter {

	public static final String ACCESSDENIED = "accessdenied";

	public static final String AJAX_DENIED = AuthenticationFilter.AJAX_DENIED;

    private List<String> groups = null;
    private List<String> permissions = null;
    private List<Group> listGroups;

    public AuthorizationFilter() {

    	// we want to use dynamic validation...
    }

	public AuthorizationFilter(String ... groups) {

		this.groups = new ArrayList<String>(groups.length);

		if (groups.length == 1) {

			String[] s = groups[0].split("\\s*\\,\\s*");

			if (s.length > 1) {

				// we are talking about the old version... without varargs...

				for(int i=0;i<s.length;i++) {

					this.groups.add(s[i]);
				}

				return;
			}
		}

        for(int i=0;i<groups.length;i++) {

            this.groups.add(groups[i]);

        }
	}

    public AuthorizationFilter(List<Object> groups) {
        this.groups = new ArrayList<String>(groups.size());
        Iterator<Object> iter = groups.iterator();
        while(iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof Group) {
                Group g = (Group) obj;
                this.groups.add(g.getName());
            } else if (obj instanceof String) {
                String s = (String) obj;
                this.groups.add(s);

            } else if (obj instanceof Permission) {

            	if (this.permissions == null) {

            		this.permissions = new LinkedList<String>();
            	}

            	Permission p = (Permission) obj;

            	this.permissions.add(p.getName());
            }
        }
    }

    public AuthorizationFilter(Permission ... permissions) {

        this.permissions = new ArrayList<String>(permissions.length);

        for(int i=0;i<permissions.length;i++) {

        	this.permissions.add(permissions[i].getName());

        }

    }

    /**
     *
     * @param groups
     * @param permission
     * @deprecated
     */
    public AuthorizationFilter(String groups, Permission permission) {
        this(groups);
        this.permissions = new ArrayList<String>(1);
        this.permissions.add(permission.getName());
    }

    /**
     *
     * @param groups
     * @param permission
     */
    public AuthorizationFilter(List<Object> groups, Permission permission) {
        this(groups);
        this.permissions = new ArrayList<String>(1);
        this.permissions.add(permission.getName());
    }

    public AuthorizationFilter(List<Object> groups, Permission ... permissions) {
        this(groups);
        this.permissions = new ArrayList<String>(permissions.length);

        for(int i=0;i<permissions.length;i++) {
        	this.permissions.add(permissions[i].getName());
        }
    }

	/**
	 * The default implementation of this method returns true for everything.
	 *
	 * You can override this method to create the authorization logic for your entire application.
	 *
	 * @param action
	 * @param actionName
	 * @param innerAction
	 * @param user The user in the session (can be null)
	 * @param userGroups The user groups (can be null)
	 * @return true if authorized, false otherwise
	 */
	public boolean isAuthorized(Action action, String actionName, String innerAction, Object user, List userGroups) {

		return true;

	}

	@SuppressWarnings("unchecked")
	public String filter(InvocationChain chain) throws Exception {

        Action action = chain.getAction();

        Object pojo = chain.getPojo();

        Object actionImpl = pojo != null ? pojo : action;

        Context session = action.getSession();

        List usergroups = BaseLoginAction.getUserGroups(session);

        Object user = BaseLoginAction.getUserSession(session);

        boolean ok = false;

        if (this.groups == null && this.permissions == null) {

        	if (actionImpl instanceof Authorizable) {

        		Authorizable authorizable = (Authorizable) actionImpl;

        		ok = authorizable.authorize(chain.getInnerAction(), user, usergroups);

        	} else {

        		ok = isAuthorized(action, chain.getActionName(), chain.getInnerAction(), user, usergroups);
        	}

        } else {

	        if (usergroups == null || usergroups.size() == 0) {

	        	if (actionImpl instanceof AjaxAction) {
	        		return AJAX_DENIED;

	        	}
				return ACCESSDENIED;
			}

	        if (groups != null && groups.size() > 0) {

	            for(int i=0;i<groups.size();i++) {
	                for(int j=0;j<usergroups.size();j++) {

	                	String group = groups.get(i).toString();
	                	Object objGroup = usergroups.get(j);
	                	String usergroup = null;

	                	if (objGroup instanceof String) {
	                		usergroup = objGroup.toString();

		            	} else if (objGroup instanceof Group) {
		            		usergroup = ((Group) objGroup).getName();

		            	}

	                	if (group.startsWith("!")) {
	                		if (usergroup.equalsIgnoreCase(group.substring(1))) {
	                		    ok = false;
	                		}else{
	                			ok = true;
	                		    i = groups.size();
	 	                        j = usergroups.size();
	                		}

	                	}else {
	                		if (usergroup.equalsIgnoreCase(group)) {
	                			ok = true;
	                			 i = groups.size();
	 	                        j = usergroups.size();
	                		}
	                	}

	                }
	            }
	            if (!ok) {

	            	if (actionImpl instanceof AjaxAction) {

	            		return AJAX_DENIED;
	            	}

	                return ACCESSDENIED;
	            }
	        }

	        if (permissions != null && permissions.size() > 0) {

	            Iterator iter = usergroups.iterator();
	            String usergroup = null;

	            while(iter.hasNext()) {
	            	Object objGroup = iter.next();

	            	if (objGroup instanceof List) {
	            		listGroups = (List<Group>) objGroup;

	            		for(int k=0;k<listGroups.size();k++) {
                    		Group ggg = ((Group) objGroup);
                    		for(int a=0;a<permissions.size();a++) {
                    			String ppp = permissions.get(a).substring(1);
                    			if (AuthorizationManager.check(ggg, ppp)) {
   		                			ok = true;
    		                		break;
    		                	}
    		                }
                    	}

	            	} else if (objGroup instanceof String) {
	            			usergroup = objGroup.toString();

	            			for(int a=0;a<permissions.size();a++) {
			                	String ppp = permissions.get(a);
			                	if (AuthorizationManager.check(usergroup, ppp)) {
		                			ok = true;
			                		break;
			                	}
			                }

		            } else if (objGroup instanceof Group) {
		            	Group ggg = ((Group) objGroup);

		            	for(int a=0;a<permissions.size();a++) {
			               	String ppp = permissions.get(a);
			               	if (AuthorizationManager.check(ggg, ppp)) {
		               			ok = true;
			               		break;
			               	}
			             }
		            }

	            }

	            if (!ok) {

	            	if (actionImpl instanceof AjaxAction) {

	            		return AJAX_DENIED;

	            	}

	                return ACCESSDENIED;
	            }
	        }

        }

        if (!ok) {

        	if (actionImpl instanceof AjaxAction) {

        		return AJAX_DENIED;

        	}

            return ACCESSDENIED;
        }

		return chain.invoke();
	}

    public void destroy() { }

}
