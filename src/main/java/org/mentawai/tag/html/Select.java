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
package org.mentawai.tag.html;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAdapter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.mentawai.i18n.I18N;
import org.mentawai.list.ListData;
import org.mentawai.list.ListItem;
import org.mentawai.list.ListManager;
import org.mentawai.tag.Out;
import org.mentawai.tag.html.ajax.InPlaceTag;

/**
 * @author Sergio Oliveira
 */
public class Select extends HTMLTag {

	private String name;
	private String listname;
	private boolean noSelect = false;
	private int size = -1;
	private boolean emptyField = false;
    private String emptyFieldValue = null;
    private String defEmptyFieldValue = null;
	private String klass = null;
    private String style = null;
	private String id = null;
    private boolean multiple = false;
    private String defValue = null;

	public void setName(String name) { this.name = name; }
	public void setList(String listname) { this.listname = listname; }
	public void setNoSelect(boolean noSelect) { this.noSelect = noSelect; }
    public void setEmptyFieldValue(String emptyFieldValue) { this.emptyFieldValue = emptyFieldValue; }
    public void setDefEmptyFieldValue(String defEmptyFieldValue) { this.defEmptyFieldValue = defEmptyFieldValue; }
	public void setSize(int size) { this.size = size; }
	public void setEmptyField(boolean emptyField) { this.emptyField = emptyField; }
	public void setKlass(String klass) { this.klass = klass; }
    public void setStyle(String style) { this.style = style; }
	public void setId(String id) { this.id = id; }
    public void setMultiple(boolean multiple) { this.multiple = multiple; }
    public void setDefValue(String defValue) { this.defValue = defValue; }

	protected StringBuffer buildTag() {
		StringBuffer sb = new StringBuffer(150);
		sb.append("<select name=\"");
		sb.append(name);
		sb.append("\"");
		if (size > 1) sb.append(" size=\"").append(size).append("\"");
		if (klass != null) sb.append(" class=\"").append(klass).append("\"");
        if (style != null) sb.append(" style=\"").append(style).append("\"");
		if (id != null) sb.append(" id=\"").append(id).append("\"");
        if (multiple) sb.append(" multiple=\"true\"");

        sb.append(getExtraAttributes());

		return sb;
	}


	/**
	 * Este metodo verifica se esta tag esta dentro da tag inplace
	 * caso esteja, retorna true e nao printa a tag, e apenas passa
	 * os parametros para a tag inplace
	 *
	 * @return boolean true if inner InPlaceTag
	 * @throws JspException
	 * @throws Exception
	 */
	public boolean isInPlaceTagIntegration() throws JspException {

		Tag parent = getParent();

		if(parent != null){

			InPlaceTag ipt = null;

			try {

				ipt = (InPlaceTag) ((TagAdapter) parent).getAdaptee();

			} catch (ClassCastException e) {
				return false;
			}

			// Set options to parent tag inplace
			try {
				Iterator<ListItem> iterator = findValuesOut().iterator();

				JSONObject obj = new JSONObject();

				JSONArray array = new JSONArray();

				while (iterator.hasNext()) {
					ListItem li = (ListItem) iterator.next();
					JSONObject o = new JSONObject();
					o.put("key", li.getKey());
					o.put("value", li.getValue());
					array.add(o);
				}

				obj.put("obj", array);

				ipt.setSelectOptions(obj);

			}  catch (Exception e) {
				throw new JspException(e);
			}

			ipt.setType(InPlaceTag.SELECT);

			if(size > 0)
				ipt.setSize(Short.parseShort( String.valueOf(size) ));

			if(klass != null)
				ipt.setKlass(klass);

			if(style != null)
				ipt.setStyle(style);

			if(multiple)
				ipt.setMultiple(multiple);

			if(name != null)
				ipt.setName(name);

			return true;
		}

		return false;
	}


	private List<ListItem> findValuesOut() throws JspException {
		ListData list = null;

        Object obj = Out.getValue(listname, pageContext, false);

        if (obj != null && obj instanceof ListData) {

            list = (ListData) obj;

        } else if (obj != null && obj instanceof Map) {

        	list = ListManager.convert(listname, (Map) obj);

        } else if (obj != null && obj instanceof Collection) {

        	list = ListManager.convert(listname, (Collection) obj);
        	  
        } else if (obj != null && obj.getClass().isArray()) {
        	
        	Object[] array = (Object[]) obj;

        	list = ListManager.convert(listname, Arrays.asList(array));
        	
        } else {

            list = ListManager.getList(listname);

        }

        if (list == null) throw new JspException("Cannot find list: " + listname);


        List<ListItem> items = list.getValues(loc);

        if (items == null) {

        	throw new JspException("Cannot find list for the locale: " + listname + " / " + loc);
        }

        return items;
	}


    public String getStringToPrint() throws JspException {

    	// if true, print nothing
    	if(isInPlaceTagIntegration())
    		return "";


        String[] values = findValues(name, false, true);

        List<ListItem> findValuesOut = findValuesOut();

        StringBuffer sb = new StringBuffer(findValuesOut.size() * 50);

        Iterator<ListItem> iter = findValuesOut.iterator();

        if (!noSelect) {
            sb.append(buildTag().toString());
            sb.append(">\n");
        }

        String keyValue = defEmptyFieldValue;

		if (defEmptyFieldValue == null) {
			keyValue = "";
		}

		if (emptyField) {

	         if (emptyFieldValue != null) {

	        	String value = null;

	        	if (emptyFieldValue.length() >= 3 && emptyFieldValue.startsWith("!") && emptyFieldValue.endsWith("!")) {

	        		// try to get this the same way we are doing in the PrintI18N tag...

	                I18N [] props = (I18N []) pageContext.getAttribute("_i18n");
	                String prefix = (String) pageContext.getAttribute("_prefix");

	                if (props != null) {

	                	String key = emptyFieldValue.substring(1, emptyFieldValue.length() - 1);

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

	        	if (value != null) {

	        		sb.append("<option value=\""+keyValue+"\">").append(value).append("</option>\n");

	        	} else {

	        		sb.append("<option value=\""+keyValue+"\">").append(emptyFieldValue).append("</option>\n");

	        	}

	         } else {

	            sb.append("<option value=\""+keyValue+"\"> - </option>\n");

	         }
		}

        while(iter.hasNext()) {
			ListItem item = iter.next();
			String id = item.getKey();
			String n = item.getValue();

            sb.append("<option value=\"");
            sb.append(id);
            sb.append("\"");

            if ((values == null || values.length == 0) && defValue != null && defValue.equals(id)) {

        		sb.append(" selected=\"selected\">");

            } else if (contains(values, id)) {
                //sb.append(" SELECTED>");
                sb.append(" selected=\"selected\">");
            } else {
                sb.append(">");
            }

            sb.append(n).append("</option>\n");
        }

        if (!noSelect) {
            sb.append("</select>\n");
        }

        return sb.toString();
    }

}
