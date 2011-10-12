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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class implements the simplest possible data list, with no locales or i18n files.
 * Use this class if you don't want to deal with internationalization and i18n files,
 * in other words, you just want a list.
 *
 * @author Sergio Oliveira
 * @since 1.1.1
 */
public class SimpleListData implements ListData {
    
    private List<ListItem> list = new LinkedList<ListItem>();
    private Map<String, String> map = new HashMap<String, String>();
    private String name;
    
    public SimpleListData() {
        this("");
    }
    
    public SimpleListData(String name) {
        this.name = name;
    }
    
    public void clear() {
    	
    	list.clear();
    	
    	map.clear();
    }
    
    public void add(int id, String msg) {
    	
    	add(String.valueOf(id), msg);
    }
    
    public void add(String id, String msg) {

    	if (!map.containsKey(id)) {
            map.put(id, msg);
            list.add(new ListDataItem(id, msg));
        }    	
    	
    }
    
    @SuppressWarnings("rawtypes")
	public void add(Enum e, String msg) {
    	add(e.toString(), msg);
    }
    
    public String getValue(String id) {
    	
    	return getValue(id, null);
    }
    
    public String getValue(int id) {
        
        return getValue(String.valueOf(id), null);
        
    }
    
	public String getValue(String id, Locale loc) {
        return map.get(id);
    }

	
	public String getValue(int id, Locale loc) {
        return getValue(String.valueOf(id), loc);
    }
    
    public List<ListItem> getValues() {
        
        return getValues(null);
    }
    
	public List<ListItem> getValues(Locale loc) {
        return list;
    }
    
	public String getName() {
        return name;
    }

	public int size() {
        return list.size();
    }

	@Override
	public String toString() {
		if (this.list.isEmpty())
			return "{}";

		Iterator<ListItem> i = this.list.iterator();

		StringBuilder sb = new StringBuilder();
		sb.append('{');

		while (i.hasNext()) {
			ListItem item = i.next();
			sb.append(item.getKey());
			sb.append('=');
			sb.append(item.getValue());
			if (i.hasNext())
				sb.append(',').append('\n');
		}

		sb.append('}');
		return sb.toString();
	}
	
	public static ListData getDaysOfTheMonth(String name) {
		
		SimpleListData list = new SimpleListData(name);
		
		for(int i=1;i<=31;i++) list.add(i, String.valueOf(i));
		
		return list;
	}
	
	public static ListData getYears(String name, int begin, int end) {
		
		SimpleListData list = new SimpleListData(name);
		
		while(begin <= end) {
			
			list.add(String.valueOf(begin), String.valueOf(begin));
			
			begin++;
		}
		
		return list;
	}
}