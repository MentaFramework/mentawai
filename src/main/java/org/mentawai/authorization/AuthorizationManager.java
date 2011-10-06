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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.core.Context;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.list.ListData;
import org.mentawai.list.ListItem;

/**
 * @author Sergio Oliveira
 */
public class AuthorizationManager implements Serializable {

    protected static Set<Group> groups = new HashSet<Group>();
    protected static Map<String, Group> groupsMap = new HashMap<String, Group>();
    protected static Map<Integer, Group> groupsMapId = new HashMap<Integer, Group>();

    public static void addListGroup(ListData data) {

    	List<ListItem> list = data.getValues(LocaleManager.getDefaultLocale());

    	Iterator<ListItem> iter = list.iterator();

    	while(iter.hasNext()) {

    		ListItem item = iter.next();

    		String key = item.getKey();

    		try {

    			addGroup(new Group(Integer.parseInt(key), item.getValue()));

    		} catch(NumberFormatException e) {

    			throw new IllegalArgumentException("List data has non-integer keys: " + key);
    		}
    	}
    }

    public static void addGroup(Group g) {
        groups.add(g);
        groupsMap.put(g.getName(), g);
        if (g.getId() != -1) {
        	groupsMapId.put(g.getId(), g);
        }
    }

    public static boolean hasGroup(Group g) {
    	return groups.contains(g);
    }

    public static boolean hasGroup(String name) {
        return groupsMap.containsKey(name);
    }

    public static boolean hasGroup(int id) {
    	return groupsMapId.containsKey(id);
    }

    public static Group getGroup(String groupName) {

    	return groupsMap.get(groupName);
    }

    public static Group getGroup(int id) {

    	return groupsMapId.get(id);
    }

    public static boolean check(String group, String permission) {
        Group g = getGroup(group);
        if (g != null) {
        	return g.hasPermission(permission);
        }
        return false;
    }

    public static boolean checkAny(String group, List<String> permissions) {
        Iterator<String> iter = permissions.iterator();
        boolean ok = false;

        while(iter.hasNext()) {
            String permission = iter.next();

            if (permission.startsWith("!")){
            	if (check(group, permission.substring(1))) {
                   	return false;
                }
            }else{
                if (check(group, permission)) {
                   	ok = true;
                }
            }

        }
        return ok;
    }

	/**
     * Check if the permission exists in the group.
     *
     * @return true if the permission exists.
     */
    public static boolean check(Group group, String permission) {

        if (group != null) {
           	 return group.hasPermission(permission);
        }
        return false;
    }

    /**
     * check if the any permissions exists in the group
     *
     * @return true if any permission exists.
     */
    public static boolean checkAny(Group group, List<String> permissions) {
        Iterator<String> iter = permissions.iterator();
        boolean ok = false;

        while(iter.hasNext()) {
            String permission = iter.next();

            if (permission.startsWith("!")){
            	if (check(group, permission.substring(1))) {
                   	return false;
                }
            }else{
                if (check(group, permission)) {
                   	ok = true;
                }
            }

        }
        return ok;
    }

    public static void setUserGroup(String group, Context session) {

    	BaseLoginAction.setUserGroup(group, session);
    }

    public static void setUserGroups(List groups, Context session) {

        BaseLoginAction.setUserGroups(groups, session);

    }

    public static void setUserGroup(Group g, Context session) {

    	BaseLoginAction.setUserGroup(g, session);
    }

    public static void setUserGroup(int groupId, Context session) {

    	BaseLoginAction.setUserGroup(groupId, session);

    }

    /**
     * User setUserGroups(Context session, String ... groups) instead...
     *
     * @param groups
     * @param session
     * @deprecated
     */
    public static void setUserGroups(String groups, Context session) {

        BaseLoginAction.setUserGroups(groups, session);
    }

}