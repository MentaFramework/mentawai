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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class State {
   
   private static Map<Integer, State> STATES = new HashMap<Integer, State>();
   
   private int id;
   
   private String name;
   
   private int countryId;
   
   private List<City> cities = null;
   
   public State(int id, String name, int countryId) {
      
      this.id = id;
      
      this.name = name;
      
      this.countryId = countryId;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
   
   public int getCountryId() {
      
      return countryId;
   }
   
   public String toString() {
      
      return id + ": " + name;
   }
   
   void setCities(List<City> cities) {
      
      this.cities = cities;
   }
   
   public List<City> getCities() {
      
      return cities;
   }
   
   public static State getById(int id) {
      
      return STATES.get(id);
   }
   
   private static TreeMap<String, State> getTreeMap(Map<Integer, TreeMap<String, State>> cache, int countryId) {
      
      TreeMap<String, State> treeMap = cache.get(countryId);
      
      if (treeMap != null) return treeMap;
      
      treeMap = new TreeMap<String, State>();
      
      cache.put(countryId, treeMap);
      
      return treeMap;
      
   }
   
   static void init() throws IOException {
      
      InputStream is = Country.class.getResourceAsStream("/org/mentawai/csc/states.txt");
      
      if (is == null) throw new IOException("Cannot load states.txt");
      
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
      
      Map<Integer, TreeMap<String, State>> cache = new HashMap<Integer, TreeMap<String, State>>();
      
      String line = null;
      
      while((line = br.readLine()) != null) {
         
         String[] s = line.split("\\t");
         
         int id = Integer.parseInt(s[0].trim());
         
         int countryId = Integer.parseInt(s[1].trim());
         
         String name = s[2].trim();
         
         State state = new State(id, name, countryId);
         
         STATES.put(id, state);
         
         TreeMap<String, State> treeMap = getTreeMap(cache, countryId);
         
         treeMap.put(name, state);
      }
      
      Iterator<Integer> iter = cache.keySet().iterator();
      
      while(iter.hasNext()) {
         
         int countryId = iter.next();
         
         TreeMap<String, State> treeMap = cache.get(countryId);
         
         List<State> states = new ArrayList<State>(treeMap.size());
         
         Iterator<State> iter2 = treeMap.values().iterator();
         
         while(iter2.hasNext()) {
            
            states.add(iter2.next());
            
         }
         
         Country c = Country.getById(countryId);
         
         c.setStates(states);
      }
   }
}