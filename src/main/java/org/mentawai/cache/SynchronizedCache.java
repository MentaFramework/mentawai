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

import java.lang.reflect.Constructor;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.blocks.PullPushAdapter;
import org.jgroups.util.Util;

public class SynchronizedCache implements Cache, MessageListener {
    
    public static final String JGROUPS_PROTOCOL_STACK = "UDP:" +
	                                                    "PING:" +
	                                                    "FD(timeout=5000):" +
	                                                    "STABLE:" +
	                                                    "VERIFY_SUSPECT(timeout=1500):" +
	                                                    "MERGE2:" +
	                                                    "NAKACK:" +
	                                                    "UNICAST(timeout=5000):" +
	                                                    "FRAG:" +  
	                                                    "FLUSH:" +
	                                                    "GMS:" +
                                                        "VIEW_ENFORCER:" +
	                                                    "QUEUE";
    
    private Cache cache;
    private JChannel channel;
    private PullPushAdapter adapter;
    private String groupname;
    
    public SynchronizedCache(String name, String groupname, int capacity, Class cacheImpl, String jgroups_protocol_stack) {
        try {
            Constructor c = cacheImpl.getConstructor(new Class[] { String.class, int.class });
            this.cache = (Cache) c.newInstance(new Object[] { name, new Integer(capacity) });
            initChannel(groupname, jgroups_protocol_stack);
        } catch(Exception e) {
            throw new RuntimeException("Error creating synchronized cache!", e);
        }
    }
    
    public SynchronizedCache(String name, String groupname, int capacity, Class cacheImpl) {
        this(name, groupname, capacity, cacheImpl, JGROUPS_PROTOCOL_STACK);
    }    
    
    public SynchronizedCache(String name, String groupname, int capacity, float load, Class cacheImpl, String jgroups_protocol_stack) {
        try {
            Constructor c = cacheImpl.getConstructor(new Class[] { String.class, int.class, float.class });
            this.cache = (Cache) c.newInstance(new Object[] { name, new Integer(capacity), new Float(load) });
            initChannel(groupname, jgroups_protocol_stack);
        } catch(Exception e) {
            throw new RuntimeException("Error creating synchronized cache!", e);
        }
    }
    
    public SynchronizedCache(String name, String groupname, int capacity, float load, Class cacheImpl) {
        this(name, groupname, capacity, load, cacheImpl, JGROUPS_PROTOCOL_STACK);
    }    
    
    private void initChannel(String groupname, String stack) throws Exception {
        this.groupname = groupname;
        this.channel = new JChannel(stack);
        channel.connect(groupname);        
        this.adapter = new PullPushAdapter(channel, this);
    }
    
    public Object get(Object key) {
        return cache.get(key);
    }
    
    public Object put(Object key, Object value) {
        Object obj = cache.put(key, value);
        if (obj != null) {
            try {
                channel.send(null, null, (java.io.Serializable) key);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
    
    public Object remove(Object key) {
        return cache.remove(key);
    }
    
    public void clear() {
        cache.clear();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(1000);
        sb.append("SynchronizedCache: GroupName=").append(groupname).append("\n");
        sb.append(cache.toString());
        return sb.toString();
    }
    
    public void receive(Message msg) {
        try {
            Object key = Util.objectFromByteBuffer(msg.getBuffer());
            cache.remove(key);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setState(byte [] state) { }
    
    public byte [] getState() { return null; }
}
        