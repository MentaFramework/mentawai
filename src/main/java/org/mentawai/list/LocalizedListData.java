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
package org.mentawai.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mentawai.i18n.LocaleManager;

/**
 * This class implements a ListData that supports multiple locales.
 * 
 * It is a ListData that you can construct by-hand by adding the items using the add methods.
 * 
 * The difference of LocalizedListData from SimpleListData is that SimpleListData does not support locales, in other words,
 * it can only be used to construct lists that will return the same list for every locale. (non-localized list)
 * 
 * @author Sergio Oliveira
 * @since 1.9
 */
public class LocalizedListData implements ListData {
	
	private Map<Locale, List<ListItem>> lists = new HashMap<Locale, List<ListItem>>();
	private Map<Locale, Map<String, ListItem>> items = new HashMap<Locale, Map<String, ListItem>>();
    private String name;
    
    public LocalizedListData(String name) {
        this.name = name;
    }
    
    public void clear() {
    	
    	lists.clear();
    	
    	items.clear();
    }
    
    public void add(int id, String value, Locale loc) {
    	
    	add(String.valueOf(id), value, loc);
    }
    
    public void add(String id, String value, Locale loc) {
    	
    	List<ListItem> list = lists.get(loc);
    	
    	if (list == null) {
    		
    		list = new LinkedList<ListItem>();
    		
    		lists.put(loc, list);
    	}
    	
    	Map<String, ListItem> map = items.get(loc);
    	
    	if (map == null) {
    		
    		map = new HashMap<String, ListItem>();
    		
    		items.put(loc, map);
    	}
    	
    	ListItem item = new ListDataItem(id, value);
    	
    	list.add(item);
    	
    	map.put(item.getKey(), item);
    }
    
    public String getValue(int id, Locale loc) {
    	
    	return getValue(String.valueOf(id), loc);
    }
    
    private Locale getAnyLocale() {
    	
    	if (items.isEmpty()) return null;
    	
    	return items.keySet().iterator().next();
    }
    
    public String getValue(String id) {
    	
    	String value = getValue(id, LocaleManager.getDefaultLocale());
    	
    	if (value == null) {
    		
    		Locale loc = getAnyLocale();
    		
    		if (loc == null) return null;
    		
    		return getValue(id, loc);
    	}
    	
    	return value;
    }
    
    public String getValue(int id) {
    	
    	return getValue(String.valueOf(id));
    }
    
	public String getValue(String id, Locale loc) {
        
		Map<String, ListItem> map = items.get(loc);
		
		if (map == null || !map.containsKey(id)) return null;
		
		ListItem item = map.get(id);
		
		return item.getValue();
    }

	
	public List<ListItem> getValues(Locale loc) {
		
		List<ListItem> list = lists.get(loc);
		
		if (list == null) list = lists.get(LocaleManager.getDefaultLocale());
		
		if (list == null) {
			
			Locale l = getAnyLocale();
			
			if (l != null) list = lists.get(l);
		}
		
		if (list == null) list = new ArrayList<ListItem>(0);
		
		return list;
		
        
    }
	
	public List<ListItem> getValues() {

		return getValues(LocaleManager.getDefaultLocale());
	}
    
	public String getName() {
        return name;
    }

	public int size() {
        
		Iterator<List<ListItem>> iter = lists.values().iterator();
		
		if (!iter.hasNext()) return 0;
		
		List<ListItem> list = iter.next();
		
		return list.size();
		
    }

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder(32);
		
		sb.append("LocalizedListData: name=").append(name).append(" size=").append(size());
		
		return sb.toString();
	}
}