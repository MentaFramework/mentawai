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
package org.mentawai.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.mentawai.authorization.AuthorizationManager;
import org.mentawai.authorization.Group;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Context;
import org.mentawai.filter.AuthenticationFree;

/**
 * This action implements the basic functionality for a Login action. You should
 * override this class to create your Login action. You should also use its
 * static methods for retrieving the session user and for checking if the user
 * is logged.
 *
 * @author Sergio Oliveira
 */
public abstract class BaseLoginAction extends BaseAction implements AuthenticationFree {

    /** The default key used to store the user object in the session. */
    public static final String USER_KEY = "sessionUser";

    /**
     * The default key used to store the user authorization groups in the
     * session.
     */
    public static final String GROUPS_KEY = "sessionGroups";

    /** The default key used to store the user locale in the session. */
    public static final String LOCALE_KEY = "sessionLocale";

    /**
     * Place an user object in the session. Note that this can be any object.
     * <br>
     * <br>
     * <b><font color="red">Attention:</font>The session context is reset when
     * you call this method.</b>
     *
     * @param value
     *            The user object to place in the session.
     */
    public void setUserSession(Object value) {
        setUserSession(value, session);
    }
    
    public void setSessionObj(Object value) {
    	setUserSession(value);
    }
    
    /**
     * Replace the user object from the session for this new one
     * WITHOUT reseting the session.
     * 
     * @param newUser
     * @since 1.14
     */
    public void replaceUserSession(Object newUser) {
    	replaceUserSession(newUser, session);
    }
    
    public void replaceSessionObj(Object value) {
    	replaceUserSession(value, session);
    }

    /**
     * Place an user object in the session. Note that this can be any object.
     * <br>
     * <br>
     * <b><font color="red">Attention:</font>The session context is reset when
     * you call this method.</b>
     * ]
     * @param value
     *            The user object to place in the session.
     * @param session
     *            The session where to put the user.
     * @since 1.1.1
     */
    public static void setUserSession(Object value, Context session) {
        session.reset();
        session.setAttribute(USER_KEY, value);
    }
    
    public static void setSessionObj(Object value, Context session) {
    	
    	setUserSession(value, session);
    }

    /**
     * Replace the user object from the session for this new one
     * WITHOUT reseting the session.
     * 
     * @param newUser
     * @param session
     * @since 1.14
     */
    public static void replaceUserSession(Object newUser, Context session) {
    	session.setAttribute(USER_KEY, newUser);
    }
    
    public static void replaceSessionObj(Object value, Context session) {
    	
    	replaceUserSession(value, session);
    }
    
    /**
     * Replace the user object from the session for this new one
     * WITHOUT reseting the session.
     * 
     * @param newUser
     * @param session
     * @since 1.14
     */
    public static void replaceUserSession(Object newUser, Map<String, Object> session) {

    	if (session instanceof Context) {
    		
    		setUserSession(newUser, (Context) session);
    		
    	} else {
    		
    		session.put(USER_KEY, newUser);
    	}    	
    }
    
    public static void replaceSessionObj(Object value, Map<String, Object> session) {
    	
    	replaceUserSession(value, session);
    }
    
    public static void setUserSession(Object value, Map<String, Object> session) {

    	if (session instanceof Context) {
    		
    		setUserSession(value, (Context) session);
    		
    	} else {
    		
    		session.put(USER_KEY, value);
    	}
    }
    
    public static void setSessionObj(Object value, Map<String, Object> session) {
    	
    	setUserSession(value, session);
    }

    /**
     * Place the user locale in the session. If you call this method, the web
     * application will follow this locale and not the one sent by the browser.
     *
     * @param loc
     *            The user locale to place in the session.
     */
    public void setUserLocale(Locale loc) {
        setUserLocale(loc, session);
    }
    
    public void setSessionLocale(Locale loc) {
    	setUserLocale(loc, session);
    }
    
    public static void setSessionLocale(Locale loc, Context session) {
    	setUserLocale(loc, session);
    }
    
    public void setSessionLocale(String loc) {
    	setUserLocale(loc);
    }

    /**
     * Place the user locale in the session. If you call this method, the web
     * application will follow this locale and not the one sent by the browser.
     *
     * @param loc
     *            The user locale to place in the session.
     * @param session
     *            The session where to put the user locale.
     * @since 1.1.1
     */
    public static void setUserLocale(Locale loc, Context session) {

        if (!session.hasAttribute(USER_KEY)) {

            throw new IllegalStateException(
                    "Tried to set user locale, but there is no user in the session!");
        }

        session.setAttribute(LOCALE_KEY, loc);
    }
    
    public static void setUserLocale(String loc, Context session) {

       StringTokenizer st = new StringTokenizer(loc, "_");

       if (st.countTokens() == 1) {

          setUserLocale(new Locale(st.nextToken()), session);

       } else if (st.countTokens() == 2) {

          setUserLocale(new Locale(st.nextToken(), st.nextToken()), session);

       } else if (st.countTokens() == 3) {

          setUserLocale(new Locale(st.nextToken(), st.nextToken(), st.nextToken()), session);

       } else {

          throw new IllegalArgumentException("Bad locale: " + loc);
       }
    }

    public void setUserLocale(String loc) {

       setUserLocale(loc, session);
    }

    /**
     * Place the user authorization groups in the session.
     *
     * @param groups The user authorization groups to place in the session.
     */
    public void setUserGroups(List groups) {
        setUserGroups(groups, session);
    }
    
    public void setSessionGroup(String group) {
    	setUserGroups(session, group);
    }

    /**
     * Place the user authorization groups in the session.
     *
     * @param groups
     *            The user authorization groups to place in the session.
     * @param session
     *            The session where to put the authorization groups.
     * @since 1.1.1
     */
    public static void setUserGroups(List groups, Context session) {

        if (!session.hasAttribute(USER_KEY)) {
            throw new IllegalStateException(
                    "Tried to set user groups, but there is no user in the session!");
        }

        List<Object> list = new ArrayList<Object>(groups.size());

        if (groups != null && groups.size() > 0) {
        	 for(int i=0;i<groups.size();i++) {
        		 if (groups.get(i) instanceof String) {
        			 String s = (String) groups.get(i);
                     list.add(s);
        		 } else if (groups.get(i) instanceof Group) {
                     list.add(groups.get(i));
        		 } else if (groups.get(i) instanceof Integer) {
                	Group g = AuthorizationManager.getGroup((Integer) groups.get(i));
                 	if (g != null) list.add(g.getName());

                 }
        	 }
        }
        session.setAttribute(GROUPS_KEY, list);

    }

    public void setUserGroup(String group) {
       setUserGroup(group, session);
    }


    public static void setUserGroup(String group, Context session) {

        if (!session.hasAttribute(USER_KEY)) {

            throw new IllegalStateException(
                    "Tried to set user groups, but there is no user in the session!");
        }

        List<String> list = new ArrayList<String>(1);
        list.add(group);

        session.setAttribute(GROUPS_KEY, list);

    }
    
    public boolean bypassAuthentication(String innerAction) {
       
       return true;
    }

    public void setUserGroup(Group group) {
        setUserGroup(group, session);
     }

    public static void setUserGroup(Group group, Context session) {

        if (!session.hasAttribute(USER_KEY)) {

            throw new IllegalStateException(
                    "Tried to set user groups, but there is no user in the session!");
        }

        List<String> list = new ArrayList<String>(1);
        list.add(group.getName());

        session.setAttribute(GROUPS_KEY, list);
    }

    public static void setUserGroup(int groupId, Context session) {

    	Group g = AuthorizationManager.getGroup(groupId);

    	if (g != null) setUserGroup(g, session);
    }

    /**
     * Place the user authorization groups in the session.
     *
     * @param groups The user authorization groups to place in the session.
     * @deprecated
     */
    public void setUserGroups(String groups) {
        setUserGroups(groups, session);
    }

    /**
     * Place the user authorization groups in the session.
     *
     * @param groups
     *            The user authorization groups to place in the session.
     * @param session
     *            The session where to put the authorization groups.
     * @since 1.1.1
     * @deprecated
     */
    public static void setUserGroups(String groups, Context session) {

        if (!session.hasAttribute(USER_KEY)) {

            throw new IllegalStateException(
                    "Tried to set user groups, but there is no user in the session!");
        }

        StringTokenizer st = new StringTokenizer(groups, ",");
        List<String> list = new ArrayList<String>(st.countTokens());
        while (st.hasMoreTokens()) {
            list.add(st.nextToken().trim());
        }
        session.setAttribute(GROUPS_KEY, list);
    }

    /**
     *
     * @param groups
     * @since 1.11
     */
    public void setUserGroups(String ... groups) {
       setUserGroups(session, groups);
    }

    /**
     *
     * @param session
     * @param groups
     * @since 1.11
     */
    public static void setUserGroups(Context session, String ... groups) {

       if (!session.hasAttribute(USER_KEY)) {

          throw new IllegalStateException(
                  "Tried to set user groups, but there is no user in the session!");
      }

      List<String> list = new ArrayList<String>(groups.length);

      for(int i=0;i<groups.length;i++) {
         list.add(groups[i]);
      }

      session.setAttribute(GROUPS_KEY, list);
    }

    /**
    *
    * @param groups
    * @since 1.12
    */
   public void setUserGroups(Group ... groups) {
      setUserGroups(session, groups);
   }

   /**
    *
    * @param session
    * @param groups
    * @since 1.12
    */
   public static void setUserGroups(Context session, Group ... groups) {

      if (!session.hasAttribute(USER_KEY)) {
         throw new IllegalStateException(
                 "Tried to set user groups, but there is no user in the session!");
     }

     List<Group> list = new ArrayList<Group>(groups.length);
     for(int i=0;i<groups.length;i++) {
        list.add(groups[i]);
     }
     session.setAttribute(GROUPS_KEY, list);

   }


    /**
     * Check if the user is logged.
     *
     * @return true if the user is logged.
     */
    public boolean isLogged() {
        return isLogged(session);
    }

    /**
     * Return the user session object.
     *
     * @return The user session or null if the user is not logged.
     */
    public Object getUserSession() {
        return getUserSession(session);
    }

    /**
     * Return the user locale.
     *
     * @return The user locale or null if the user is not logged.
     */
    public Locale getUserLocale() {
        return getUserLocale(session);
    }

    /**
     * Return the user authorization groups.
     *
     * @return The user authorization groups or null if the user is not logged
     *         or user has no authorization groups.
     */
    public List<Object> getUserGroups() {
        return getUserGroups(session);
    }

    /**
     * Check if the user is logged.
     *
     * @param session
     *            The session context where to check for the user object.
     * @return true if the user is logged.
     */
    public static boolean isLogged(Context session) {
        return session.hasAttribute(USER_KEY);
    }

    /**
     * Check if the user is logged.
     *
     * @param session
     *            The http session where to check for the user object.
     * @return true if the user is logged.
     */
    public static boolean isLogged(HttpSession session) {
        return session.getAttribute(USER_KEY) != null;
    }

    /**
     * Return the user session object.
     *
     * @param session
     *            The session context where to check for the user object.
     * @return The user session or null if the user is not logged.
     */
    public static Object getUserSession(Context session) {
        return session.getAttribute(USER_KEY);
    }
    
    public static Object getSessionObj(Context session) {
    	
    	return session.getAttribute(USER_KEY);
    }
    
    public static Object getSessionObj(HttpSession session) {
    	
    	return session.getAttribute(USER_KEY);
    }
    
    public Object getSessionObj() {
    	
    	return getSessionObj(session);
    }
    
    public static Object getUserSession(Map<String, Object> session) {
    	return session.get(USER_KEY);
    }
    
    /**
     * Return the user session object.
     *
     * @param session
     *            The session context where to check for the user object.
     * @return The user session or null if the user is not logged.
     */
    public static Object getUserSession(HttpSession session) {
        return session.getAttribute(USER_KEY);
    }

    /**
     * Return the user locale.
     *
     * @param session
     *            The session context where to check for the user locale.
     * @return The user locale or null if the user is not logged.
     */
    public static Locale getUserLocale(Context session) {
        return (Locale) session.getAttribute(LOCALE_KEY);
    }

    /**
     * Return the user locale.
     *
     * @param session
     *            The session context where to check for the user locale.
     * @return The user locale or null if the user is not logged.
     */
    public static Locale getUserLocale(HttpSession session) {
        return (Locale) session.getAttribute(LOCALE_KEY);
    }

    /**
     * Return the user authorization groups.
     *
     * @param session
     *            The session context where to check for the user authorization
     *            groups.
     * @return The user authorization groups or null if the user is not logged
     *         or the user has no authorization groups.
     */
    public static List<Object> getUserGroups(Context session) {
        return (List<Object>) session.getAttribute(GROUPS_KEY);
    }

    /**
     * Return the user authorization groups.
     *
     * @param session
     *            The http session where to check for the user authorization
     *            groups.
     * @return The user authorization groups or null if the user is not logged
     *         or the user has no authorization groups.
     */
    public static List<Object> getUserGroups(HttpSession session) {
        return (List<Object>) session.getAttribute(GROUPS_KEY);
    }
}
