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
package org.mentawai.csc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.mentawai.list.ListData;
import org.mentawai.list.LocalizedListData;

public class Country {
   
   private static Map<Integer, Country> COUNTRIES = new HashMap<Integer, Country>();
   
   private static Map<Locale, List<Country>> COUNTRIES_BY_LOCALE = new HashMap<Locale, List<Country>>();
   
   private static Set<Locale> LOCALES = new HashSet<Locale>();
   
   private int id;
   
   private Map<Locale, String> names = new HashMap<Locale, String>();
   
   private List<State> states = null;
   
   private boolean hasStates;
   
   public Country(int id, boolean hasStates) {
      
      this.id = id;
      
      this.hasStates = hasStates;
   }
   
   public Country(int id) {
      
      this(id, false);
   }
   
   public int getId() { return id; }
   
   public boolean hasStates() { return hasStates; }
   
   public boolean getHasStates() { return hasStates; }
   
   public void addName(Locale loc, String name) {
      
      names.put(loc, name);
      
   }
   
   public String getName(Locale loc) {
      
      return names.get(loc);
   }
   
   public String toString() {
      
      StringBuilder sb = new StringBuilder(128);
      
      sb.append(id).append(" (").append(hasStates).append("):").append("\t");
      
      Iterator<String> iter = names.values().iterator();
      
      while(iter.hasNext()) {
         
         sb.append(iter.next()).append("\t");
      }
      
      return sb.toString();
   }
   
   void setStates(List<State> states) {
	   
	   // add a shorter version of the object, wihtout the states list... (for AJAX)
	   
	   Iterator<State> iter = states.iterator();
	   
	   List<State> list = new ArrayList<State>(states.size());
	   
	   while(iter.hasNext()) {
		   
		   State s = iter.next();
		   
		   State clone = new State(s.getId(), s.getName(), id);
		   
		   list.add(clone);
	   }
      
      this.states = list;
   }
   
   public List<State> getStates() {
      
      return states;
      
   }
   
   public static Country getById(int id) {
      
      return COUNTRIES.get(id);
   }
   
   public static Iterator<Country> getCountries(Locale loc) {
      
      List<Country> list = COUNTRIES_BY_LOCALE.get(loc);
      
      if (list == null) {
         
         // try only the language...
         
         Locale l = new Locale(loc.getLanguage());
         
         list = COUNTRIES_BY_LOCALE.get(l);
      }
      
      if (list == null) return null;
         
      
      return list.iterator();
   }
   
   public static ListData getListData(String name) {
	   
	   LocalizedListData list = new LocalizedListData(name);
	   
	   return getListData(list);
   }
   
   public static ListData getListData(LocalizedListData list) {
	   
	   Iterator<Locale> iter = COUNTRIES_BY_LOCALE.keySet().iterator();
	   
	   while(iter.hasNext()) {
		   
		   Locale loc = iter.next();
		   
		   List<Country> countries = COUNTRIES_BY_LOCALE.get(loc);
		   
		   Iterator<Country> iter2 = countries.iterator();
		   
		   while(iter2.hasNext()) {
			   
			   Country c = iter2.next();
			   
			   list.add(c.getId(), c.getName(loc), loc);
		   }
	   }
	   
	   return list;
	   
   }
   
   public static Set<Locale> getLocales() {
	   
	   return LOCALES;
   }
   
   public static void init() throws IOException {
      
      InputStream is = Country.class.getResourceAsStream("/org/mentawai/csc/countries.txt");
      
      if (is == null) throw new IOException("Cannot load countries.txt");
      
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
      
      String header = br.readLine();
      
      String[] s = header.split("\\t");
      
      Locale[] locales = new Locale[s.length - 2];
      
      List<TreeMap<String, Country>> treeMaps = new ArrayList<TreeMap<String, Country>>(s.length - 2);
      
      for(int i=2;i<s.length;i++) {
         
         String[] ss = s[i].split("_");
         
         locales[i - 2] = new Locale(ss[ss.length - 2], ss[ss.length - 1]);
         
         LOCALES.add(locales[i - 2]);
         
         treeMaps.add(new TreeMap<String, Country>());
      }
      
      String line = null;
      
      while((line = br.readLine()) != null) {
         
         String[] ss = line.split("\\t");
         
         int id = Integer.parseInt(ss[0].trim());
         
         boolean hasStates = ss[1].trim().equals("T");
         
         Country c = new Country(id, hasStates);
         
         for(int i=0;i<locales.length;i++) {
            
            String name = ss[2 + i].trim();
            
            c.addName(locales[i], name);
            
            TreeMap<String, Country> treeMap = treeMaps.get(i);
            
            treeMap.put(name, c);
         }
         
         COUNTRIES.put(id, c);
      }
      
      for(int i=0;i<locales.length;i++) {
         
         TreeMap<String, Country> treeMap = treeMaps.get(i);
         
         Iterator<Country> iter = treeMap.values().iterator();
         
         List<Country> list = new ArrayList<Country>(treeMap.size());
         
         while(iter.hasNext()) {
            
            list.add(iter.next());
         }
         
         COUNTRIES_BY_LOCALE.put(locales[i], list);
      }
      
      State.init();
      
      City.init();
   }
   
   public static void main(String[] args) throws Exception {
      
      Country.init();
      
      Iterator<Country> iter = Country.getCountries(new Locale("pt"));
      
      while(iter.hasNext()) {
         
         System.out.println(iter.next());
      }
      
      System.out.println();
      
      Country c = Country.getById(53);
      
      System.out.println(c);
      
      Iterator<State> states = c.getStates().iterator();
      
      while(states.hasNext()) {
         
         System.out.println(states.next());
      }
      
      State s = State.getById(333);
      
      Iterator<City> cities = s.getCities().iterator();
      
      while(cities.hasNext()) {
         
         System.out.println(cities.next());
      }
   }
}