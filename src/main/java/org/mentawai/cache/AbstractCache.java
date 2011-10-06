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
package org.mentawai.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractCache implements Cache, Serializable {
    
    protected static final float LOAD = 0.75F;
    
    protected final String name;
    protected final Map<Object, Object> map;
    protected final int capacity;
    protected transient volatile int misses = 0;
    protected transient volatile int hits = 0;
    protected final transient Date startDate = new Date();

    public AbstractCache(String name, int capacity) {
        this(name, capacity, LOAD);
    }

    public AbstractCache(String name, int capacity, float load) {
    	this.name = name;
        this.capacity = capacity;
        int initialCapacity = (int) Math.ceil(capacity / load) + 1;
        map = createMap(initialCapacity, load);
    }
    
    protected abstract Map<Object, Object> createMap(int initialCapacity, float load);
    
    public synchronized Object remove(Object key) {
        return map.remove(key);
    }

    public synchronized Object get(Object key) {
        Object obj = map.get(key);
        if (obj == null) misses++;
        else hits++;
        return obj;
    }

    public synchronized Object put(Object key, Object value) {
        return map.put(key, value); 
    }
    
    public synchronized void clear() {
        map.clear();
    }

    public synchronized int getSize() {
        return map.size();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(250);
        sb.append("NAME=").append(name).append("\n");
        sb.append("STARTDATE=").append(startDate).append("\n");
        sb.append("CAPACITY=").append(capacity).append("\n");
        sb.append("SIZE=").append(getSize()).append("\n");
        int misses = this.misses;
        int hits = this.hits;
        int total = misses + hits;
        sb.append("MISSES=").append(misses).append("\n");
        sb.append("HITS=").append(hits).append("\n");
        sb.append("TOTAL=").append(total).append("\n");
        
        if (getSize() < 25) { // for debugging...
            sb.append("ELEMENTS: ");
            synchronized(this) {
                Iterator<Object> iter = map.values().iterator();
                while(iter.hasNext()) {
                    Object value = iter.next();
                    sb.append("[").append(value).append("]");
                }
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
        