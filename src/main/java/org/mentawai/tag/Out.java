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
package org.mentawai.tag;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.mentaregex.Regex;
import org.mentawai.core.Action;
import org.mentawai.core.Input;
import org.mentawai.core.Output;
import org.mentawai.formatter.Formatter;
import org.mentawai.formatter.FormatterManager;
import org.mentawai.i18n.I18N;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.list.ListData;
import org.mentawai.list.ListManager;
import org.mentawai.tag.util.Context;
import org.mentawai.tag.util.PrintTag;

/**
 * @author Sergio Oliveira
 */
public class Out extends PrintTag {

	private String value = null;
    private String list = null;
    private String formatter = null;
    private String onBlank = null;
    private String onFalse = null;
    private String onTrue = null;
    private boolean includeTime = false;
    private boolean includeTimeWithBR = false;
    private boolean onlyTime = false;
    
    private static final String  ZERO = "0";
    private static final String  ZEROf = "0.0";
    private static final String  BLANK = "";
    
    private boolean replaceAll = false;
    
    public void setReplaceAll(boolean all) {
    	this.replaceAll = all;
    }
    
    public void setIncludeTimeWithBR(boolean includeTimeWithBR) {
    	this.includeTimeWithBR = includeTimeWithBR;
    }
    
    public void setOnlyTime(boolean onlyTime) {
    	
    	this.onlyTime = onlyTime;
    }
    
    public void setIncludeTime(boolean includeTime) {
    	
    	this.includeTime = includeTime;
    }

	public void setValue(String value) {
		this.value = value;
	}
	
	public void setOnFalse(String onFalse) {
		
		this.onFalse = onFalse;
	}
	
	public void setOnTrue(String onTrue) {
		
		this.onTrue = onTrue;
	}
	
	public void setOnBlank(String onBlank) {
		this.onBlank = onBlank;
	}
    
    public void setList(String list) {
        this.list = list;
    }
    
    public void setFormatter(String formatter) {
    	this.formatter = formatter;
    }

    /*
	private static Object findValue(String value, PageContext pageContext) {
		if (action == null) return null;
		Output output = action.getOutput();
        Object obj = output.getValue(value);
        if (obj == null) {
            org.mentawai.core.Context session = action.getSession();
            obj = session.getAttribute(value);
            if (obj == null) {
                org.mentawai.core.Context application = action.getApplication();
                obj = application.getAttribute(value);
            }
        }
        return obj;
	}
    */
    
    public static Object getValue(Tag context, String value, PageContext pageContext, boolean tryBoolean) throws JspException {
    	
        if (context != null) {
        	
            Context ctx = (Context) context;
            
            Object obj = ctx.getObject();
            
            if (obj == null) {
            	
                if (value == null) {
                	
                    throw new JspException("Out tag cannot find value: context is null!");
                	
                } else {
                	
                    Object object = getValue(value, pageContext, tryBoolean);
                    
                    if (object == null) {
                    	
                        //throw new JspException("Out tag cannot find value: " + value);
                    	return "";
                    	
                    } else {
                    	
                        return object;
                    }
                }
                
            } else {
                if (value == null) {
                    // for loop...
                    return obj;
                } else {
                    Object object = null;
                    
                    /*
                    if (tryBoolean) {
                        object = getBooleanValue(obj, value);
                        if (object == null) {
                            object = getValue(obj, value, false);
                        }
                    } else {
                        object = getValue(obj, value, false);
                    }
                    */
                    
                    object = getValue(value, obj, tryBoolean);
                    
                    if (object != null) {
                        // for bean...
                        return object;
                    } else {
                        // try output...
                        object = getValue(value, pageContext, tryBoolean);
                        if (object == null) {
                            // not found in bean...
                            return null;
                        } else {
                            // for output...
                            return object;
                        }
                    }
                }
            }
        } else {
            if (value == null) {
                throw new JspException("Not inclosed by a context tag!");
            } else {
                //if (action == null) throw new JspException("No action in request for tag: " + value);
                Object obj = getValue(value, pageContext, tryBoolean);
                if (obj == null) return null;
                return obj;            
            }
        }   
    }
    
    public String getFromList(int i, Locale loc, String listname) throws JspException {
    	
    	return getFromList(i >= 0 ? String.valueOf(i) : null, loc, listname);
    }
    
    public String getFromList(String i, Locale loc, String listname) throws JspException {
    	
		ListData list = null;
    	
        Object obj = Out.getValue(listname, pageContext, false);
        
        if (obj != null && obj instanceof ListData) {
            
            list = (ListData) obj;
            
        } else if (obj != null && obj instanceof Map) {
        	
        	list = ListManager.convert(listname, (Map) obj);
        	
        } else if (obj != null && obj instanceof Collection) {
        	
        	list = ListManager.convert(listname, (Collection) obj);
            
        } else {
		
            list = ListManager.getList(listname);
            
        }

        if (list == null) throw new JspException("No data list could be found for listname: " + listname);
        
        String value = list.getValue(i, loc);
        
        if (value == null && i != null) {
        	
            //throw new JspException("No value inside listname " + listname + " for " + i + " / " + loc);
        	
        	return "???";
        }
        return value;
    }
    
    private String checkOnBlank(String value) {
    	
    	if ((value == null || value.equals(BLANK) || value.equals(ZERO) || value.equals(ZEROf)) && onBlank != null) {
    		
    		return onBlank;
    	}
    	
    	return value;
    }
    
    private Object findValue(Action action, String value) {
       
       if (action == null) return null; // avoid NPE here...
    	
		Output output = action.getOutput();
		Input input = action.getInput();
		
		Object obj = output.getValue(value);
		
		if (obj == null) {
			
			obj = input.getValue(value);
			
		}
		
		return obj;
    }
    
    public String getStringToPrint() throws JspException {
    	
    	String s = getStringToPrintImpl();
    	
    	String body = getBody();
    	
    	if (body == null) {
    		
    		return s;
    		
    	} else {
    		
    		if (s.contains("/")) {
    			
    			s = s.replaceAll("/", "#/");
    		}
    		
    		if (replaceAll) {
    		
    			return Regex.sub(body, "s/#$#{out#}/" + s + "/g", '#');
    			
    		} else {
    			
    			return Regex.sub(body, "s/#$#{out#}/" + s + "/", '#');
    		}
    	}
    }
    
	private String getStringToPrintImpl() throws JspException {
		
		Tag parent = findAncestorWithClass(this, Context.class);
		
		Object obj = null;
		
		try {
		
			obj = getValue(parent, value, pageContext, true);
			
		} catch(JspException e) {
			
			// anything is better than exception... try output and input before throwing exception...
			
	        if (action == null) throw e;
	        
	        obj = findValue(action, value);
	        
			if (obj == null) throw e;
		}
        
        if (obj == null) {
        	
        	obj = findValue(action, value);
        	
        	if (obj == null) {
        	
	        	if (onBlank == null) {
	        		
	    			return "";
	        		
	        	} else {
	        		
	        		return onBlank;
	        	}
        	
        	}
        }
        
        if (list != null) {
        	
            if (obj instanceof Integer) {
            	
                Integer i = (Integer) obj;
                
                String s = getFromList(i.intValue(), loc, list);
                
                return checkOnBlank(s);
                
            } else if (obj instanceof String) {
            	
                String s = getFromList(obj.toString(), loc, list);
                    
                return checkOnBlank(s);
                    
            } else if (obj instanceof int[]) {
            	
                int [] array = (int []) obj;
                StringBuffer sb = new StringBuffer(array.length * 40);
                for(int i=0;i<array.length;i++) {
                    String item = getFromList(array[i], loc, list);
                    if (i != 0) sb.append(", ");
                    sb.append(item);
                }
                
                String s = sb.toString();
                
                return checkOnBlank(s);
                
            } else if (obj instanceof String[]) {
            	
                String[] array = (String[]) obj;
                StringBuffer sb = new StringBuffer(array.length * 40);
                for(int i=0;i<array.length;i++) {
                    String item = getFromList(array[i], loc, list);
                    if (i != 0) sb.append(", ");
                    sb.append(item);
                }
                
                String s = sb.toString();
                
                return checkOnBlank(s);
                
            } else {
                throw new JspException("Could not get list: " + value + " / " + obj);
            }
        }
        
        // formatter logic here:
        
        if (formatter != null) {
            
            Formatter f = FormatterManager.getFormatter(formatter);
            
            if (f == null) throw new JspException("Cannot find formatter: " + formatter);
            
            return checkOnBlank(f.format(obj, loc));
        	
        } else if (obj instanceof java.util.Date) {
        	
        	// if formatter was not defined but we have a default data formatter, then use it...
        	
        	Formatter f;
        	
        	StringBuilder sb = new StringBuilder(32);
        	
        	if (!onlyTime) {
        	
	        	if (( f = FormatterManager.getFixedDateFormatter()) != null) {
	        		
	        		//return checkOnBlank(f.format(obj, loc));
	        		
	        		sb.append(f.format(obj, loc));
	        		
	        	}
	        	
	        	if (sb.length() == 0) {
	        	
		        	SimpleDateFormat format = LocaleManager.getSimpleDateFormat(loc);
		        	
		        	if (format != null) {
		        		
		        		synchronized(format) {
		        		
		        			//return checkOnBlank(format.format(obj));
		        			
		        			sb.append(format.format(obj));
		        			
		        		}
		        	}
	        	
	        	}
        	}
        	
        	if (!(includeTime || includeTimeWithBR) && !onlyTime) {
        		
        		return checkOnBlank(sb.toString());
        		
        	} else {
        		
        		boolean found = false;
        		
	        	if (( f = FormatterManager.getFixedTimeFormatter()) != null) {
	        		
	        		//return checkOnBlank(f.format(obj, loc));
	        		
	        		found = true;
	        		
	        		if (!onlyTime) {
	        			
	        			if (includeTimeWithBR) {
	        				
	        				sb.append("<br/>");
	        				
	        			} else {
	        				sb.append(' ');
	        			}
	        		}
	        		
	        		sb.append(f.format(obj, loc));
	        		
	        	}
	        	
	        	if (!found) {
	        	
		        	SimpleDateFormat format = LocaleManager.getSimpleTimeFormat(loc);
		        	
		        	if (format != null) {
		        		
		        		synchronized(format) {
		        		
		        			//return checkOnBlank(format.format(obj));
		        			
		        			if (!onlyTime) {
		        				
			        			if (includeTimeWithBR) {
			        				
			        				sb.append("<br/>");
			        				
			        			} else {
			        				sb.append(' ');
			        			}
		        			}
		        			
		        			sb.append(format.format(obj));
		        			
		        		}
		        	}
	        	}
	        	
	        	return checkOnBlank(sb.toString());
        	}
        	
        	
        } else if (obj instanceof Boolean) {
        	
        	boolean b = ((Boolean) obj).booleanValue();
        	
        	if (b && onTrue != null) return getInternacionalized(onTrue);
        	
        	if (!b && onFalse != null) return getInternacionalized(onFalse);
        }
        
        return checkOnBlank(obj.toString());
	}
	

	private String getInternacionalized(String onBoolean) {
		
		String value = null;
		
		if (onBoolean.length() >= 3 && onBoolean.startsWith("!") && onBoolean.endsWith("!")) {

    		// try to get this the same way we are doing in the PrintI18N tag...

            I18N [] props = (I18N []) pageContext.getAttribute("_i18n");
            String prefix = (String) pageContext.getAttribute("_prefix");

            if (props != null) {

            	String key = onBoolean.substring(1, onBoolean.length() - 1);

            	if (prefix != null) {

            		String prefixKey = prefix + "." + key;

            		for(int i=props.length-1;i>=0;i--) {
                        if (props[i] == null) continue;
                        if (props[i].hasKey(prefixKey)) {
                             value = props[i].get(prefixKey);
                             break;
                        }
                    }
            	}

            	if (value == null) {

            		for(int i=props.length-1;i>=0;i--) {
                        if (props[i] == null) continue;
                        if (props[i].hasKey(key)) {
                            value = props[i].get(key);
                            break;
                        }
                    }
            	}
            }
    	}
		
		return value == null ? onBoolean : value;
	}
}