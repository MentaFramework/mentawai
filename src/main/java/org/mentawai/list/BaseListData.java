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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.mentawai.core.ApplicationManager;
import org.mentawai.i18n.I18N;
import org.mentawai.i18n.I18NMap;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.util.StringComparator;

/**
 * A base implementation for a ListData that will load files and sort.
 * 
 * The files should be i18n files inside a given directory (or the default).
 * 
 * Sorting is supported and can be by id, by value or by the order they appear in the file.
 * 
 * @author Sergio Oliveira
 */
public class BaseListData implements ListData {
	
	private static final String FULLDIR = ApplicationManager.getRealPath();
	private static final String SEP = File.separator;
	
	/** Sort the list according to the id (key) values, which can be integers */
	public static final int ORDER_BY_ID = 1;
	
	/** Sort the list according to the id (key) values, which can be strings */
	public static final int ORDER_BY_STRING_ID = 4;
	
	/** Sort the list according to item values, which are strings */
	public static final int ORDER_BY_VALUE = 2;
	
	/** Sort the list according to the order they appear inside the filename (don't sort) */
	public static final int ORDER_BY_FILE = 3;
	
	private String listname;
	private String listDir;
	private Map<Locale, List<ListItem>> map = new HashMap<Locale, List<ListItem>>();
	private Map<Locale, Map<String, ListItem>> values = new HashMap<Locale, Map<String, ListItem>>();
	private int size = 0;
	
	/**
	 * Creates a new BaseListData
	 * 
	 * @param listname The name of the list
	 * @param ordertype How this list should be sorted
	 * @param listDir The directory where to look for list files.
	 * 
	 * @throws IOException
	 */
	public BaseListData(String listname, int ordertype, String listDir) throws IOException {
		this.listname = listname;
		this.listDir = listDir;
		load(ordertype);
	}
	
	/**
	 * Creates a new BaseListData, looking for files inside the default directory "/lists".
	 * 
	 * @param listname The name of the list
	 * @param ordertype How this list should be sorted.
	 * @throws IOException
	 */
	public BaseListData(String listname, int ordertype) throws IOException {
		this(listname, ordertype, ListManager.LIST_DIR);
	}	
	
	/**
	 * Creates a new BaseListData, looking for files inside the default directory "/lists",
	 * and using the default sort order ORDER_BY_VALUE.
	 * 
	 * @param listname The name of the list
	 * @throws IOException
	 */
	public BaseListData(String listname) throws IOException {
		this(listname, ORDER_BY_VALUE, ListManager.LIST_DIR);
	}	
	
	/**
	 * The name of this list, for example "genders"
	 * 
	 * @return The name of the list.
	 */
	public String getName() {
		return listname;
	}
	
	/**
	 * The size of the list, for example a "genders" list should have 2 items.
	 * 
	 * @return The size of the list
	 */
	public int size() {
		return size;
	}
    
	/**
	 * Return a list of ListItem objects so you can print
	 * 
	 * the whole list for this locale.
	 * @param loc The locale of the list
	 * @return a list of ListItem objects
	 */
	public List<ListItem> getValues(Locale loc) {
		
		if (map.containsKey(loc)) {
		
			return map.get(loc);
			
		} else if (!map.isEmpty()) {
			
			return map.values().iterator().next();
			
		} else {
			
			return new ArrayList<ListItem>(0);
		}
	}
	
	public List<ListItem> getValues() {
		
		return getValues(LocaleManager.getDefaultLocale());
	}
    
	/**
	 * Given an id and a locale return the list item value.
	 * 
	 * @param id The id of the list item
	 * @param loc The locale of the list from where to get the value.
	 * @return The value of the list item
	 */
	public String getValue(String id, Locale loc) {
		
		Map<String, ListItem> m = null;
		
		if (values.containsKey(loc)) {
			
			m = values.get(loc);
		
		} else if (!values.isEmpty()) {
			
			m = values.values().iterator().next();
			
		} else {
			
			return "!" + id + "!";
		}
		
		ListItem item = m.get(id);
		
		if (item == null) {
			
			return "!!" + id + "!!";
		}
		
		return item.getValue();

	}
	
	public String getValue(String id) {
		
		String value = getValue(id, LocaleManager.getDefaultLocale());
		
		if (value == null) {
			
			// return from the first locale available...
			
			Iterator<Map<String, ListItem>> iter = values.values().iterator();
			
			while(iter.hasNext()) {
				
				Map<String, ListItem> map = iter.next();
				
				ListItem item = map.get(id);
				
				if (item != null) return item.getValue();
			}
		}
		
		return value;
	}
	
	public String getValue(int id) {
		return getValue(String.valueOf(id));
	}
	
	public String getValue(int id, Locale loc) {
		
		return getValue(String.valueOf(id), loc);
	}
	
	/*
	 * Load a i18n file to a list for further processing.
	 */
	private List<String> loadFileToList(Locale loc) throws IOException {
		File file = new File(FULLDIR + SEP + listDir + SEP + listname + "_" + loc.toString() + ".i18n");
		if (!file.exists() || file.isDirectory()) {
			throw new IOException("Could not find list file: " + file);
		}
		FileInputStream fis = new FileInputStream(file);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		List<String> lines = new ArrayList<String>(1000);
		String line;
		while((line = in.readLine()) != null) {
			lines.add(line);
		}
		in.close();
		return lines;
	}
	
	/*
	 * Try to load an i18n file for that locale.
	 * First try the full locale. If not fount try just the locale language.
	 */
	private I18N loadI18N(Locale loc) throws IOException {
        
		I18N i18n = I18NMap.getI18N(SEP + listDir + SEP + listname + "_" + loc.toString() + ".i18n");
        
        if (i18n != null) return i18n;
        
        i18n = I18NMap.getI18N(SEP + listDir + SEP + listname + "_" + loc.getLanguage() + ".i18n");
        
        if (i18n != null) return i18n;
        
		throw new IOException("Could not find i18n file for list: " + listname);
	}
	
	/*
	 * Get the list item id from the line.
	 */
	private String getIdFromLine(String line) {
      if (line.indexOf('=') > 0) {
   		StringTokenizer st = new StringTokenizer(line, "=");
   		String key = st.nextToken().trim();
   		return key;
      }
      return null;
	}
	
	/**
	 * Sort the list acording to the order type.
	 * If you create another order type, override this method to do the sorting.
	 * 
	 * @param list The list of ListItems to sort.
	 * @param ordertype The order type (how to sort)
	 */
	protected void sort(List<ListItem> list, int ordertype) {
		if (ordertype == ORDER_BY_ID) {
			Collections.sort(list, new IdComparator());
		} else if (ordertype == ORDER_BY_VALUE) {
			Collections.sort(list, new ValueComparator());
		} else if (ordertype == ORDER_BY_STRING_ID) {
			Collections.sort(list, new StringIdComparator());
		}
	}
	
	/*
	 * Load everything here. The order type might be relevant when
	 * deciding how to load, for example, ordering according to how
	 * they appear in the file requires a different loading approach.
	 * 
	 * The default loading approach is to load with an i18n and sort later
	 * by alling the sort method. 
	 * 
	 * @param ordertype The sort order desired for the list being loaded
	 * 
	 * @throws IOException
	 */
	private void load(int ordertype) throws IOException {
		
		Set<Locale> set = null;
		
		try {
			
			set = LocaleManager.scanLocales(listDir, listname);
			
		} catch(Throwable e) {
			
			e.printStackTrace();
			
		}
		
		if (set == null || set.isEmpty()) throw new IOException("Cannot find locales for list: " + listname + " / " + listDir);
		
		Locale locDef = LocaleManager.getDefaultLocale();
		
		if (!set.contains(locDef)) {
			
			locDef = set.iterator().next();
		}
		
		I18N i18nDef = loadI18N(locDef);
		
		if (ordertype != ORDER_BY_FILE) {
			
			Iterator<Locale> locs = set.iterator();
			
			while(locs.hasNext()) {
				
				Locale loc = locs.next(); 
				
				I18N i18n = loadI18N(loc);
				
				Iterator<String> iter = i18nDef.keys();
				
				List<ListItem> list = new ArrayList<ListItem>(1000);
				
				Map<String, ListItem> m = new HashMap<String, ListItem>();
				
				while(iter.hasNext()) {
					
					String key = iter.next();
					
					String value = i18n.get(key);
					
					if (value == null) {
						
						value = i18nDef.get(key);
						
						if (value == null) {
							
							value = "!NULL!";
						}
					}
					
					ListItem item = new ListDataItem(key, value);
					
					list.add(item);
					
					m.put(item.getKey(), item);
				}
				
				sort(list, ordertype);
				
				map.put(loc, list);
				
				values.put(loc, m);
				
				if (list.size() > size) size = list.size();
			}
			
		} else  {
			
			Iterator<Locale> locs = set.iterator();
			
			while(locs.hasNext()) {
				
				Locale loc = locs.next();
				
				List<String> lines = loadFileToList(loc);
				
				I18N i18n = loadI18N(loc);
				
				Iterator<String> iter = lines.iterator();
				
				List<ListItem> list = new ArrayList<ListItem>(1000);
				
				Map<String, ListItem> m = new HashMap<String, ListItem>();
				
				while(iter.hasNext()) {
					
					String line = iter.next();
					
					String key = getIdFromLine(line);
					
					if (key == null) continue; // should never happen...
					
					String value = i18n.get(key);
					
					if (value == null) {
						
						value = i18nDef.get(key);
						
						if (value == null) {
							
							value = "!NULL!";
						}
					}   
					
					ListItem item = new ListDataItem(key, value);
					
					list.add(item);
					
					m.put(item.getKey(), item);
				}
				
				map.put(loc, list);
				
				values.put(loc, m);
				
				if (list.size() > size) size = list.size();
				
			}
		}
	}
	
	private static class IdComparator implements Comparator<ListItem>  {
		
		public int compare(ListItem l1, ListItem l2) {
			
			int id1 = -1;
			int id2 = -1;
			
			try { id1 = Integer.parseInt(l1.getKey()); } catch(NumberFormatException e) { }
			try { id2 = Integer.parseInt(l2.getKey()); } catch(NumberFormatException e) { }
			
			return id1 - id2;
		}
	}
	
	private static class StringIdComparator extends StringComparator  {
		
		public int compare(Object o1, Object o2) {
			ListItem l1 = (ListItem) o1;
			ListItem l2 = (ListItem) o2;
			
			String id1 = l1.getKey();
			String id2 = l2.getKey();
			
			return super.compare(id1, id2);
		}
	}

	private static class ValueComparator extends StringComparator {
		
		public int compare(Object o1, Object o2) {
			ListItem l1 = (ListItem) o1;
			ListItem l2 = (ListItem) o2;
			
			return super.compare(l1.getValue(), l2.getValue());
		}
	}
}
			
			
			
	
	
		
		
	
	