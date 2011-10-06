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
package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.util.InjectionUtils;

/**
 * A filter that takes all the properties of a bean and place them in the action output.
 * Use this filter, for example, if you want to show all properties of an User bean,
 * but you don't want to manually place them in the action output, in other words,
 * you place the User bean in the output and use the OVFilter to accomplish the same result.
 *<br>
 * Note that this filter uses the method Class.getMethods() in order to find the getters, in other words,
 * getters from the class and its superclasses will be called for properties.
 *
 * @author Sergio Oliveira
 */
public class OVFilter implements Filter {
	
	public static char PREFIX_SEPARATOR = '.';
	
	private String key;
    private String prefix = null;
    private boolean overwrite = true;
	
    /**
     * Creates a OVFilter that will be applied to the bean in the action output with the given key.
     *
     * @param key The key with which the object (bean) is placed in the action output.
     */
	public OVFilter(String key) {
		this.key = key;
	}
    
    /**
     * Creates a OVFilter that will be applied to the bean in the action output with the given key.
     *
     * @param key The key with which the object (bean) is placed in the action output.
     * @param prefix The prefix will be appended to the attribute name on the action output.
     */
    public OVFilter(String key, String prefix) {
        this(key);
        this.prefix = prefix;
    }

    /**
     * Creates a OVFilter that will be applied to the bean in the action output with the given key.
     *
     * @param key The key with which the object (bean) is placed in the action output.
     * @param overwrite Overwrite the value in the output if it is already there? (default is true)
     */
    public OVFilter(String key, boolean overwrite) {
        this.key = key;
        this.overwrite = overwrite;
    }

    /**
     * Creates a OVFilter that will be applied to the bean in the action output with the given key.
     *
     * @param key The key with which the object (bean) is placed in the action output.
     * @param prefix The prefix will be appended to the attribute name on the action output.
     * @param overwrite Overwrite the value in the output if it is already there? (default is true)
     */
    public OVFilter(String key, String prefix, boolean overwrite) {
        this(key, overwrite);
        this.prefix = prefix;
    }    
    
    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("OVFilter: Key=").append(key).append(" Prefix=").append(prefix != null ? prefix : "NULL").append(" Overwrite=").append(overwrite);
        return sb.toString();
    }
    
	public String filter(InvocationChain chain) throws Exception {
		
        String result = chain.invoke();
		Action action = chain.getAction();
        Output output = action.getOutput();
        Object bean = output.getValue(key);
        
        InjectionUtils.setObject(bean, output, prefix, overwrite);

        return result;
	}
    
    public void destroy() { }
}
		