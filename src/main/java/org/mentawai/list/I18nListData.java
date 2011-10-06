/*
 * Mentawai Web Framework http://www.mentaframework.org/
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

import static org.mentawai.list.ListManager.LIST_DIR;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.mentawai.core.ApplicationManager;
import org.mentawai.i18n.I18N;
import org.mentawai.i18n.I18NHolder;
import org.mentawai.i18n.I18NMap;
import org.mentawai.i18n.LocaleManager;

/**
 * This class implements a more sophisticated data list then a SimpleListData,
 * with locales and i18n files. Use this class if you want to deal with
 * internationalization and i18n files, in other words, you want a
 * internacionalizable list.
 * 
 * @author Marvin Froeder
 * @since 1.9
 */
public class I18nListData implements ListData {

	private static final String SEP = File.separator;

	private static final String FULLDIR = ApplicationManager.getRealPath();

	private Map<String, String> map = new LinkedHashMap<String, String>();

	private I18NHolder i18n = new I18NHolder();
	
	private Set<Locale> locales = new HashSet<Locale>();

	private String name;

	/**
	 * @param listname the list name, must be the same name of the i18n file.
	 * @see ListManager
	 */
	public I18nListData(String listname) {
		this.name = listname;

		File root = new File(FULLDIR + SEP + LIST_DIR);

		if (!root.exists())
			return;

		File[] files = root.listFiles();

		for (File file : files) {

			if (file.isDirectory())
				continue;

			String filename = file.getName();

			if (!filename.endsWith(".i18n") || !filename.startsWith(name + "_"))
				continue; // only load i18n files...

			Locale l = getLocale(file, listname);

			I18N i18n = I18NMap.getI18N(file);
			
			if (i18n == null) continue;

			this.i18n.add(l, i18n);
			
			this.locales.add(l);
		}
	}

	/**
	 * Extract the locale from a i18n file name.
	 */
	private Locale getLocale(File file, String listname) {
		String filename = file.getName();
		filename = filename.substring(0, filename.indexOf(".i18n"));
		filename = filename.substring(listname.length() + 1);
		String[] s = filename.split("_");
		if (s.length == 1) {
			return new Locale(s[0]);
		}
		if (s.length == 2) {
			return new Locale(s[0], s[1]);
		}
		return LocaleManager.getDefaultLocale();
	}
	
	private Locale getAnyLocale() {
		
		for(Locale loc: locales) {
			
			return loc;
		}
		
		return null;
	}

	/**
	 * @param id the id value
	 * @param i18nKey the i18n key
	 */
	public void add(String id, String i18nKey) {
		if (!map.containsKey(id)) {
			map.put(id, i18nKey);
		}
	}

	public String getValue(String id, Locale loc) {
		
		I18N i18n = this.i18n.get(loc);
		
		if (i18n == null) return null;
		
		String key = map.get(id);
		
		if (key == null) return null;
		
		return i18n.get(key);
	}
	
	public String getValue(String id) {
		
		String value = getValue(id, LocaleManager.getDefaultLocale());
		
		if (value == null) {
			
			Iterator<Locale> iter = locales.iterator();
			
			while(iter.hasNext()) {
				
				Locale loc = iter.next();
				
				value = getValue(id, loc);
				
				if (value != null) return value;
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

	public List<ListItem> getValues() {
		
		return getValues(LocaleManager.getDefaultLocale());

	}

	public List<ListItem> getValues(Locale loc) {
		
		I18N i18n = this.i18n.get(loc);
		
		if (i18n == null) i18n = this.i18n.get(LocaleManager.getDefaultLocale());
		
		if (i18n == null) {
			
			Locale l = getAnyLocale();
			
			if (l != null) i18n = this.i18n.get(l);
		}
		
		if (i18n == null) return new ArrayList<ListItem>(0);
		
		Set<String> keys = this.map.keySet();
		
		List<ListItem> list = new ArrayList<ListItem>();
		
		for (String key : keys) {
			
			String value = i18n.get(key);
			
			if (value == null) continue;
			
			list.add(new ListDataItem(key, value));
			
		}
		return list;
	}

	public String getName() {
		return name;
	}

	public int size() {
		return map.size();
	}

	@Override
	public String toString() {
		if (this.map.isEmpty())
			return "{}";

		Set<String> keys = this.map.keySet();

		StringBuilder sb = new StringBuilder();

		for (String key : keys) {
			if (sb.length() == 0) {
				sb.append('{');
			} else {
				sb.append(',').append('\n');
			}
			sb.append(key);
			sb.append('=');
			String value = map.get(key);
			sb.append(value != null ? value : "NULL");
			
		}

		sb.append('}');
		return sb.toString();
	}
}