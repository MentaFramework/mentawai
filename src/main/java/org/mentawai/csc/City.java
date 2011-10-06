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

public class City {
   
   private static Map<Integer, City> CITIES = new HashMap<Integer, City>();
   
   private int id;
   
   private String name;
   
   private boolean isCapital;
   
   private int stateId;
   
   public City(int id, String name, int stateId, boolean isCapital) {
      
      this.id = id;
      
      this.name = name;
      
      this.stateId = stateId;
      
      this.isCapital = isCapital;
   }

   public int getId() {
      return id;
   }

   public boolean isCapital() {
      return isCapital;
   }

   public String getName() {
      return name;
   }

   public int getStateId() {
      return stateId;
   }
   
   public String toString() {
      
      return id + ": " + name;
   }
   
   public static City getById(int id) {
      
      return CITIES.get(id);
   }
   
   private static TreeMap<String, City> getTreeMap(Map<Integer, TreeMap<String, City>> cache, int stateId) {
      
      TreeMap<String, City> treeMap = cache.get(stateId);
      
      if (treeMap != null) return treeMap;
      
      treeMap = new TreeMap<String, City>();
      
      cache.put(stateId, treeMap);
      
      return treeMap;
      
   }
   
   static void init() throws IOException {
      
      InputStream is = Country.class.getResourceAsStream("/org/mentawai/csc/cities.txt");
      
      if (is == null) throw new IOException("Cannot load cities.txt");
      
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
      
      Map<Integer, TreeMap<String, City>> cache = new HashMap<Integer, TreeMap<String, City>>();
      
      String line = null;
      
      while((line = br.readLine()) != null) {
         
         String[] s = line.split("\\t");
         
         int id = Integer.parseInt(s[0].trim());
         
         int stateId = Integer.parseInt(s[1].trim());
         
         String name = s[2].trim();
         
         boolean isCapital = s[3].trim().equals("T");
         
         City city = new City(id, name, stateId, isCapital);
         
         CITIES.put(id, city);
         
         TreeMap<String, City> treeMap = getTreeMap(cache, stateId);
         
         treeMap.put(name, city);
      }
      
      Iterator<Integer> iter = cache.keySet().iterator();
      
      while(iter.hasNext()) {
         
         int stateId = iter.next();
         
         TreeMap<String, City> treeMap = cache.get(stateId);
         
         List<City> cities = new ArrayList<City>(treeMap.size());
         
         City capital = removeCapital(treeMap);
         
         if (capital != null) {
            
            cities.add(capital);
         }
         
         Iterator<City> iter2 = treeMap.values().iterator();
         
         while(iter2.hasNext()) {
            
            cities.add(iter2.next());
            
         }
         
         State state = State.getById(stateId);
         
         state.setCities(cities);
      }
   }
   
   private static City removeCapital(TreeMap<String, City> treeMap) {
      
      Iterator<City> iter = treeMap.values().iterator();
      
      while(iter.hasNext()) {
         
         City c = iter.next();
         
         if (c.isCapital()) {
            
            iter.remove();
            
            return c;
         }
      }
      
      return null;
   }
}