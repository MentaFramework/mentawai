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
package org.mentawai.core;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Defines the behavior of an action input.
 * An Input has parameters and headers.
 * Parameters can be any java object, and not only strings.
 * Headers can be only strings.
 * Parameters are not read-only, and actions and filters can modify it, if they need to.
 * Headers are read-only.
 *
 * @author Sergio Oliveira
 */
public interface Input {
	
	/**
	 * Gets a header value from this input.
	 *
	 * @param name the header name
	 * @return the header value
	 */
	public String getHeader(String name);

	/**
	 * Gets an iterator with all header names.
	 *
	 * @return an iterator with all header names
	 */
	public Iterator<String> getHeaderKeys();
	
	/**
	 * Checks if a value exists.
	 * 
	 * @param name The name of the key.
	 * 
	 * @return true if exists
	 * @since 1.11
	 */
	public boolean hasValue(String name);
	
	/**
	 * Check if the input has this key (Shorter verions of hasValue)
	 * @param key
	 * @since 2.0.1
	 */
	public boolean has(String key);


	/**
	 * Gets a property associated with this input.
	 *
	 * @param name the name of the property
	 * @return the value of the property as a String
	 */
	public String getProperty(String name);

	/**
	 * Gets a parameter value from this input as a String.
     * If this object is not a String, the toString() method is called.
	 *
	 * @param name the parameter's name
	 * @return the parameter's value as a string or null if it doesn't exist
	 * @deprecated Use getString instead
	 */
	public String getStringValue(String name);
	
	public String getString(String name);

	/**
	 * Gets a parameter value from this input as an int.
	 *
	 * @param name the parameter's name
	 * @return the parameters's value as an int
	 * @throws NullPointerException if the value does not exist.
	 * @throws InputException is the value cannot be converted to an int.
	 * @deprecated Use getInt instead
	 */
	public int getIntValue(String name);
	
	public int getInt(String name);

	/**
	 * Gets all parameter values with the given name as a string array.
	 *
	 * @param name the parameters'name
	 * @return a string array with all the values or null if they don't exist.
	 * @deprecated Use getStrings instead
	 */
	public String[] getStringValues(String name);
	
	public String[] getStrings(String name);

	/**
	 * Gets all parameter values with the given name as an int array.
	 *
	 * @param name the parameters'name
	 * @return a int array with all the values or null if the don't exist.
	 * @throws InputException if any of the values cannot be converted to an int.
	 * @deprecated Use getInts instead.
	 */
	public int[] getIntValues(String name);
	
	public int[] getInts(String name);

	/**
	 * Sets a parameter value with the given name.
	 * The parameter can be any object.
	 * If the parameter already exists the old value is substituted by the new one.
	 *
	 * @param name the name of the parameter
	 * @param value the parameter value (any object)
	 */
	public void setValue(String name, Object value);

	/**
	 * Gets a parameter value (any object) with the given name.
	 *
	 * @param name the name of the parameter
	 * @return the parameter value (any object) or null if it doesn't exist.
	 */
	public Object getValue(String name);

	/**
	 * Removes a parameter from this input.
	 *
	 * @param name the name of the parameter to remove.
	 */
	public void removeValue(String name);

	/**
	 * Gets an iterator with all the parameter names.
	 *
	 * @return an iterator with all the parameter names.
	 */
	public Iterator<String> keys();
    

    /**
     * Gets a parameter value from this input as an int.
     *
     * @param name the parameter's name
     * @param def default value to return
     * @return the parameters's value as an int or def if not found
     * @throws InputException is the value cannot be converted to an int.
     * @deprecated use getInt instead
     */
	public int getIntValue(String name, int def);
	
	public int getInt(String name, int def);
    
    
    /**
     * Gets a parameter value from this input as a long.
     *
     * @param name the parameter's name
     * @return the parameters's value as a long
     * @throws NullPointerException if the value does not exist.
     * @throws InputException is the value cannot be converted to a long
     * @deprecated getLong instead
     */
    public long getLongValue(String name);
    
    public long getLong(String name);
    
	/**
	 * Gets all parameter values with the given name as an long array.
	 *
	 * @param name the parameters'name
	 * @return a long array with all the values or null if the don't exist.
	 * @throws InputException if any of the values cannot be converted to an long.
	 */
	public long[] getLongs(String name);

    /**
     * Gets a parameter value from this input as a long.
     *
     * @param name the parameter's name
     * @param def default value to return
     * @return the parameters's value as a long or def if not found
     * @throws InputException is the value cannot be converted to a long.
     * @deprecated getLong instead
     */
    public long getLongValue(String name, long def);
    
    public long getLong(String name, long def);
    
    /**
     * Gets a parameter value from this input as a boolean.
     *
     * @param name the parameter's name
     * @return the parameters's value as a boolean
     * @throws NullPointerException if the value does not exist.
     * @throws InputException is the value cannot be converted to a boolean.
     * @deprecated use getBoolean instead
     */
    public boolean getBooleanValue(String name);
    
    public boolean getBoolean(String name);
    
    /**
     * Gets a parameter value from this input as a boolean.
     *
     * @param name the parameter's name
     * @param def default value to return
     * @return the parameters's value as a boolean or def if not found
     * @throws InputException is the value cannot be converted to a boolean.
     * @deprecated use getBoolean instead
     */
    public boolean getBooleanValue(String name, boolean def);
    
    public boolean getBoolean(String name, boolean def);
    
    /**
     * Gets a parameter value from this input as a float.
     *
     * @param name the parameter's name
     * @return the parameters's value as a float
     * @throws NullPointerException if the value does not exist.
     * @throws InputException is the value cannot be converted to a float.
     * @deprecated use getFloat instead
     */
    public float getFloatValue(String name);
    
    public float getFloat(String name);
    
    /**
     * Gets a parameter value from this input as a float.
     *
     * @param name the parameter's name
     * @param def default value to return
     * @return the parameters's value as a float or def if not found
     * @throws InputException is the value cannot be converted to a float.
     * @deprecated use getFloat instead
     */
    public float getFloatValue(String name, float def);
    
    public float getFloat(String name, float def);
    
    /**
     * Gets a parameter value from this input as a double.
     *
     * @param name the parameter's name
     * @return the parameters's value as a double
     * @throws NullPointerException if the value does not exist.
     * @throws InputException is the value cannot be converted to a double
     * @deprecated use getDouble instead
     */
    public double getDoubleValue(String name);
    
    public double getDouble(String name);
    
    /**
     * Gets a parameter value from this input as a double.
     *
     * @param name the parameter's name
     * @param def default value to return
     * @return the parameters's value as a double or def if not found
     * @throws InputException is the value cannot be converted to a double.
     * @deprecated use getDouble instead
     */
    public double getDoubleValue(String name, double def);
    
    public double getDouble(String name, double def);
    
    /**
     * Gets a populated object with the values from the action input.
     * 
     * OBS: Excelent idea given by Bruno Braga!
     * 
     * @param klass
     * @return The populated object
     * @since 1.8
     */
    public <E> E getObject(Class<? extends E> klass);
    
    /**
     * Gets a populated object with the values from the action input.
     * 
     * @param bean
     * @return The populated object
     * @since 1.11
     */    
    public <E> E getObject(E bean);
    
    /**
     * Gets a populated object with the values from the action input.
     * 
     * Use the prefix in front of every value name.
     * 
     * OBS: Excelent idea given by Bruno Braga!
     *
     * @param klass
     * @param prefix
     * @return The populated object
     * @since 1.8
     */
    public <E> E getObject(Class<? extends E> klass, String prefix);
    
    /**
     * Gets a populated object with the values from the action input.
     * 
     * Use the prefix in front of every value name.
     *
     * @param bean
     * @param prefix
     * @return The populated object
     * @since 1.11
     */
    public <E> E getObject(E bean, String prefix);
    
	/**
	 * Parse a parameter value from this input as a Date.
	 *
	 * @param name the parameter's name
	 * @return the parameter's value as a Date or null if it doesn't exist
	 * @throws InputException throws if the attributte can't be parsed
	 */
	public Date getDate(String name);
	
	/**
	 * Parse a parameter value from this action input as a Date using the given style.
	 * 
	 * @param name the parameter's name
	 * @param style the DateFormat style to be used
	 * @return the parameter's value as a Date or null if it doesn't exist
	 * @throws InputException throws if the attributte can't be parsed
	 */
	public Date getDate(String name, int style);

	/**
	 * Parse a parameter value from this action input as a Date using the given pattern.
	 * 
	 * @param name the parameter's name
	 * @param pattern The SimpleDateFormat pattern to be used
	 * @return the parameter's value as a Date or null if it doesn't exist
	 * @throws InputException throws if the attributte can't be parsed
	 */
	public Date getDate(String name, String pattern);
	
	/**
	 * Convert the parameter value from this action input as an Enum.
	 * 
	 * @param name the parameter's name
	 * @param enumClass the Enum's class
	 * @return Return the Enumeration
	 */
	public <E extends Enum<E>> E getEnum(String name, Class<E> enumClass);
	
	/**
	 * Convert the parameter value from this action input as an Array of Enums.
	 * 
	 * @param name the parameter's name
	 * @param enumClass the Enum's class
	 * @return Return the Enumeration
	 */
	public <E extends Enum<E>> Set<E> getEnums(String name, Class<E> enumClass);
}
