package org.mentawai.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.mentawai.util.InjectionUtils;
import org.mentawai.util.StringUtils;

public abstract class AbstractInput implements Input {

	/**
     * Calls getStringValue() and tries to convert the string to boolean.
     */
    public boolean getBoolean(String name) {
        
        String value = getString(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return false;
        }
        
        value = value.trim();
        
        if (value.equalsIgnoreCase("false")) return false;
        
        if (value.equalsIgnoreCase("true") || value.equals("on")) return true;
        
        int x = -1;
        
        try {
            x = Integer.parseInt(value);
            
            if (x == 0) return false;
            else if (x == 1) return true;
            
        } catch(Exception e) { }
        
        throw new InputException("Could not convert input to boolean: " + name + " (" + value + ")");
    }
    
    /**
     * @deprecated use getBoolean instead
     */
    public boolean getBooleanValue(String name) {
    	return getBoolean(name);
    }
	
	public boolean getBoolean(String name, boolean def) {
        
        String value = getString(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return def;
        }
        
        return getBoolean(name);
        
    }
	
	/**
	 * @deprecated use getBoolean instead
	 */
	public boolean getBooleanValue(String name, boolean def) {
		return getBoolean(name, def);
	}
	
	public Date getDate(String name) {
		return getDate(name, null, DateFormat.SHORT);
	}
	
	public Date getDate(String name, int style) {
		return getDate(name, null, style);
	}

	public Date getDate(String name, String pattern) {
		return getDate(name, pattern, -1);
	}
	
	private Date getDate(String name, String pattern, int style) {
		
		// first check whether we already have an Date...
		
		Object obj = getValue(name);
		
		if (obj instanceof Date) {
			
			return (Date) obj;
		}
		
		// if not parse...
		
		String date = getString(name);
		
		if(StringUtils.getInstance().isEmpty(date)){
			return null;
		}
		
		date = date.trim();
		
		try {
			
			DateFormat df;
			if(pattern != null){
				
				df = new SimpleDateFormat(pattern);
				
			} else if(style != -1){
				
				df = DateFormat.getDateInstance(style, getLocale());
				
			} else {
				
				throw new IllegalStateException("Should never be here!");
				
			}
			return df.parse(date);
			
		} catch(ParseException e) {
			
            throw new InputException("Could not convert input to date: " + name + " (" + date + ")");
		}
	}
	
	protected abstract Locale getLocale();
    
    
    /**
     * Calls getString() and tries to convert the string to a double.
     */
    public double getDouble(String name) {
        
        String value = getString(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return -1;
        }
        
        value = value.trim();

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
        	try { // Try to convert from Currency
				return StringUtils.parseCurrencyNumber(value, getLocale()).doubleValue();
			} catch (ParseException e1) {
	            throw new InputException("Could not convert input to double: " + name + " (" + value + ")");
			}
        }
    }
    
    /**
     * @deprecated use getDouble instead
     */
    public double getDoubleValue(String name) {
    	return getDouble(name);
    }
    
    public double getDouble(String name, double def) {
        
        String value = getString(name);

        if (value == null || value.trim().equals("")) {
            
            return def;
        }
        
        return getDouble(name);
    }
    
    /**
     * @deprecated use getDouble instead
     */    
    public double getDoubleValue(String name, double def) {
    	return getDouble(name, def);
    }
    
    /**
     * Calls getString() and tries to convert the string to a float.
     */
    public float getFloat(String name) {
        
        String value = getString(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return -1;
        }
        
        value = value.trim();

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
        	try { // Try to convert from Currency
				return StringUtils.parseCurrencyNumber(value, getLocale()).floatValue();
			} catch (ParseException e1) {
	            throw new InputException("Could not convert input to float: " + name + " (" + value + ")");
			}
        }
    }
    
    /**
     * @deprecated use getFloat instead
     */
    public float getFloatValue(String name) {
    	return getFloat(name);
    }
    
    public float getFloat(String name, float def) {
        
        String value = getString(name);

        if (value == null || value.trim().equals("")) {

            return def;
        }
        
        return getFloat(name);

    }
    
    /**
     * @deprecated use getFloat instead
     */
    public float getFloatValue(String name, float def) {
    	return getFloat(name, def);
    }
    
    /**
     * @deprecated use getInt instead
     */
    public int getIntValue(String name) {
    	return getInt(name);
    }
    
    public int getInt(String name) {
        String value = getString(name);
        if (value == null || value.trim().equals("")) {
            //throw new NullPointerException("No value defined for " + name);
            return -1;
        }
        
        value = value.trim();
        
        try {
            return Integer.parseInt(value);
        } catch(Exception e) {
            throw new InputException("Could not convert input to number: " + name + " (" + value + ")");
        }    	
    }
    
    /**
     * @deprecated use getInt instead
     */
    public int getIntValue(String name, int def) {
    	
    	return getInt(name, def);

    }
    
    public int getInt(String name, int def) {
        String value = getString(name);

        if (value == null || value.trim().equals("")) {
            return def;
        }
        
        return getInt(name);
    	
    }
    
    
    public int[] getInts(String name) {
    	
		String[] values = getStrings(name);

		if (values == null || values.length == 0) {
			return null;
		}

		int [] ret = new int[values.length];

		for(int i = 0; i <values.length; i++) {
			
			try {
				
				if (values[i] == null) throw new InputException("Cannot convert null value: " + name);
				
				ret[i] = Integer.parseInt(values[i].trim());
				
			} catch(NumberFormatException e) {
				
				throw new InputException("Could not convert input to numbers: " + name);
				
			}
		}
		return ret;
    }
    
    /**
     * @deprecated Use getInts instead
     */
    public int[] getIntValues(String name) {
    	
    	return getInts(name);
	}
    
    /**
     * Calls getString() and tries to convert the string to a long.
     */
    public long getLong(String name) {
        String value = getString(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return -1;
        }

        value = value.trim();
        
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new InputException("Could not convert input to long: "
                    + name + " (" + value + ")");
        }
    }
    

    
    public long[] getLongs(String name) {
    	
		String[] values = getStrings(name);

		if (values == null || values.length == 0) {
			return null;
		}

		long [] ret = new long[values.length];

		for(int i = 0; i <values.length; i++) {
			
			try {
				
				if (values[i] == null) throw new InputException("Cannot convert null value: " + name);
				
				ret[i] = Long.parseLong(values[i].trim());
				
			} catch(NumberFormatException e) {
				
				throw new InputException("Could not convert input to numbers: " + name);
				
			}
		}
		return ret;
    }
    
    
    /**
     * @deprecated use getLong instead
     */
    public long getLongValue(String name) {
    	return getLong(name);
    }

	public long getLong(String name, long def) {
        String value = getString(name);

        if (value == null || value.trim().equals("")) {
            return def;
        }
        
        return getLong(name);
    }
	
	/**
	 * @deprecated use getLong instead
	 */
	public long getLongValue(String name, long def) {
		return getLong(name, def);
	}

	
	public <E> E getObject(Class<? extends E> klass) {

		return getObject(getInstance(klass), null);

	}
	
	public <E> E getObject(Class<? extends E> klass, boolean convertBoolean) {

		return getObject(getInstance(klass), null, convertBoolean);

	}

	public <E> E getObject(Class<? extends E> klass, String prefix) {

		return getObject(getInstance(klass), prefix, false);
	}


	public <E> E getObject(Class<? extends E> klass, String prefix, boolean convertBoolean) {

		return getObject(getInstance(klass), prefix, true, true, convertBoolean);

	}
	
	private <E> E getInstance(Class<? extends E> klass) {
		
		try {
			
			return klass.newInstance();
			
		} catch(Exception e) {
			
			throw new InputException(e);
		}
	}
	
	public <E> E getObject(E bean) {
		
		return getObject(bean, null);
		
	}
	
	public <E> E getObject(E bean, boolean convertBoolean) {
		
		return getObject(bean, null, convertBoolean);
		
	}
	
	public <E> E getObject(E bean, String prefix) {
		
		return getObject(bean, prefix, false);
	}
	
	
	public <E> E getObject(E bean, String prefix, boolean convertBoolean) {
		
		return getObject(bean, prefix, true, true, convertBoolean);
	}

	public <E> E getObject(E target, String prefix, boolean tryField, boolean tryToConvert, boolean convertBoolean) {

		try {

			InjectionUtils.getObject(target, this, getLocale(), tryField, prefix, tryToConvert, convertBoolean, false);

			return target;

		} catch (Exception e) {

			throw new InputException(e);
		}
	}
	
	public <E extends Enum<E>> E getEnum(String key, Class<E> enumClass) {
		String value = getString(key);
		if (value == null)
			return null;
		try {
			return Enum.valueOf(enumClass, value);
		} catch (Exception e) {
			return null;
		}
	}
	
	public <E extends Enum<E>> Set<E> getEnums(String key, Class<E> enumClass) {
		String[] values = getStrings(key);
		
		if (values == null)
			return null;
		try {
			
			HashSet<E> result = new HashSet<E>(values.length);
			
			for (String v : values) {
				result.add( Enum.valueOf(enumClass, v) );
			}
			
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Boolean b;
	
	public static boolean isB() {
		return b;
	}
	
	public static void main(String[] args) {
		if (isB()) {
			System.out.println("AAAAA");
		} else {
			System.out.println("DDDDD");
		}
		
	}

}
