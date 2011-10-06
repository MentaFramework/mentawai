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
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.authorization.AuthorizationManager;
import org.mentawai.authorization.Group;
import org.mentawai.core.Action;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Consequence;
import org.mentawai.core.ConsequenceException;
import org.mentawai.core.Forward;
import org.mentawai.filter.AuthorizationFilter;
import org.mentawai.i18n.LocaleManager;

/**
 * @author Sergio Oliveira
 */
public class RequiresAuthorization extends TagSupport {

    private static final String HEADER_KEY_PRAGMA = "Pragma";
    private static final String HEADER_KEY_CACHECONTROL = "Cache-Control";
    private static final String HEADER_VALUE_NOCACHE = "no-cache";

    protected HttpSession session = null;
    protected HttpServletRequest req = null;
    protected HttpServletResponse res = null;
	protected Action action = null;
	protected Locale loc = null;
	protected ApplicationManager appManager = null;

	private boolean both = false;
    private boolean cache = false;
    private List<String> groups;
    private List<String> permissions;
    private List listGroups;

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    /**
     * If you want both options to be valid set true for this value
     * If you want any options to be valid set false for this value
     * 
     * @param both
     */
    public void setBoth(boolean both) {
    	if (this.permissions!=null && this.permissions.size()>0) {
    		this.both = both;
    	}else{
    		this.both = false;
    	}
    }

    public void setGroups(String groups) {
        StringTokenizer st = new StringTokenizer(groups, ",");
        this.groups = new ArrayList<String>(st.countTokens());
        while(st.hasMoreTokens()) {
            this.groups.add(st.nextToken().trim());
        }
    }
    
    public void setGroup(String group) {
    	this.groups = new ArrayList<String>(1);
    	this.groups.add(group.trim());
    }

    public void setPermission(String permission) {
        setPermissions(permission);
    }

    public void setPermissions(String permissions) {
        StringTokenizer st = new StringTokenizer(permissions, ",");
        this.permissions = new ArrayList<String>(st.countTokens());
        while(st.hasMoreTokens()) {
            this.permissions.add(st.nextToken().trim());
        }
    }

    public int doStartTag() throws JspException {
        this.session = pageContext.getSession();
        this.req = (HttpServletRequest) pageContext.getRequest();
        this.res = (HttpServletResponse) pageContext.getResponse();
		this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);
		this.loc = LocaleManager.getLocale(req);
		this.appManager = ApplicationManager.getInstance();
        return super.doStartTag();
    }

    private void executeConsequence() throws JspException {
        Consequence c = appManager.getGlobalConsequence(AuthorizationFilter.ACCESSDENIED);
        if (c != null) {
            try {
                c.execute(action, AuthorizationFilter.ACCESSDENIED, req, res);
            } catch(ConsequenceException e) {
                e.printStackTrace();
                throw new JspException(e);
            }
        } else {

            throw new JspException("No global consequence defined for ACCESSDENIED!");

        }
    }

    /**
     * Verify if the parameters of the groups or permissions exist or are not denied for the user
     *
     * @return int
     */
    public int doEndTag() throws JspException {
        // cache policy... (default is no cache!)
        if (!cache) {
            res.setHeader(HEADER_KEY_PRAGMA, HEADER_VALUE_NOCACHE);
            res.setHeader(HEADER_KEY_CACHECONTROL, HEADER_VALUE_NOCACHE);
        }

        List usergroups = BaseLoginAction.getUserGroups(session);

        boolean haveGroup = false;
        boolean havePermission = false;

        if (usergroups == null || usergroups.size() == 0) {
            executeConsequence();
			return SKIP_PAGE;
		}

        if (groups != null && groups.size() > 0) {

            for(int i=0;i<groups.size();i++) {

                for(int j=0;j<usergroups.size();j++) {

                    if (usergroups.get(j) instanceof String) {
                    	if ( groups.get(i).toString().equalsIgnoreCase(usergroups.get(j).toString()) ) {
                    		haveGroup = true;
                		} else if (groups.get(i).toString().equalsIgnoreCase("!"+usergroups.get(j).toString())) {
                			haveGroup = false;

            			}

                    } else if (usergroups.get(j) instanceof Group) {
                    	Group g = (Group) usergroups.get(j);
                    	if ( groups.get(i).toString().equalsIgnoreCase(g.getName()) ) {
                    		haveGroup = true;

                		}else if (groups.get(i).toString().equalsIgnoreCase("!"+g.getName())) {
                			haveGroup = false;

            			}

                    } else if (usergroups.get(j) instanceof List) {

                    	listGroups = (List) usergroups.get(j);
                    	for(int k=0;k<listGroups.size();k++) {
                    		Group g = (Group) listGroups.get(k);

                    		if ( groups.get(i).toString().equalsIgnoreCase(g.getName().toString() ) ) {
                    			haveGroup = true;

                    		}else if (groups.get(i).toString().equalsIgnoreCase("!"+g.getName().toString())) {
                    			haveGroup = false;
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
                	listGroups = (List) usergroups.get(j);
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
        		return EVAL_PAGE;
        	}else{
        		executeConsequence();
                return SKIP_PAGE;
        	}
        } else {
        	if (haveGroup || havePermission){
        		 return EVAL_PAGE;
        	}else{
        		executeConsequence();
                return SKIP_PAGE;
        	}
        }

    }
}

