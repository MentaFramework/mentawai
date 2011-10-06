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
package org.mentawai.tag.html.dyntag.inputText;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAdapter;

import org.mentawai.tag.html.ajax.InPlaceTag;
import org.mentawai.tag.html.dyntag.BaseTag;

public class InputText extends BaseTag {

	private static final long serialVersionUID = 1L;

    private static final String SEPARATOR = "#";

    private String extra;

    private String separator = SEPARATOR;

	//Attribute private class
	/** Maxlength componente */
	private String maxlength = null;
	/** Size component */
	private String size = null;
	/** Readonly component */
	private String readonly = null;

	/** Constructor Standard */
	public InputText(){

	}


	/**
	 * Este metodo verifica se esta tag esta dentro da tag inplace
	 * caso esteja, retorna true e nao printa a tag, e apenas passa
	 * os parametros para a tag inplace
	 *
	 * @return boolean true if inner InPlaceTag
	 */
	public boolean isInPlaceTagIntegration() {

		Tag parent = getParent();

		if(parent != null){

			InPlaceTag ipt = null;

			try {

				ipt = (InPlaceTag) ((TagAdapter) parent).getAdaptee();

			} catch (ClassCastException e) {
				return false;
			}

			ipt.setType(InPlaceTag.TEXT);

			if(getName() != null)
				ipt.setName(getName());

			if(getSize() != null)
				ipt.setSize(Short.valueOf(getSize()));

			if(getKlass() != null)
				ipt.setKlass(getKlass());

			if(getKlassStyle() != null)
				ipt.setStyle(getKlassStyle());

			if(getMaxlength() != null)
				ipt.setMaxlength( getMaxlength() );

			if(getValue() != null)
				ipt.setValue( getValue() );
			
			return true;
		}

		return false;
	}



	/** Method to build tag */
    protected StringBuffer buildTag() {
    	StringBuffer results = new StringBuffer("<input");
		prepareAttribute(results, "type","text");
		prepareAttribute(results, "name",getName());
		prepareAttribute(results, "class",getKlass());
		prepareAttribute(results, "style","text-align:"+getTextAlign()+";"+getKlassStyle());
		prepareAttribute(results, "size",getSize());
		prepareAttribute(results, "maxlength",getMaxlength());
		if((getDisabled()!= null) && (getDisabled().equals("true"))){
		  results.append(" disabled=\"disabled\"");
		}
		if((getReadonly() != null) && (getReadonly().equals("true"))){
		   results.append(" readonly=\"readonly\"");
		}
		if((getId() == null) || (getId().trim().equals(""))){
			setId(getName());
		}
		prepareAttribute(results, "id",getId());
		prepareAttribute(results, "title",getTitle());
		prepareValue(results);
		results.append(this.prepareEventHandlers());
		results.append(getExtraAttributes());
		return results;
	}

	/** Method to prepare value component */
	protected void prepareValue(StringBuffer results){
		Object value = findObject(getName());
		if (value != null) results.append(" value=\"").append(value).append("\"");
        else if (getValue() != null) results.append(" value=\"").append(getValue()).append("\"");
	}

	/** Method to prepare events JavaScript in compoenent */
	protected String prepareEventHandlers() {
		StringBuffer handlers = new StringBuffer();
		prepareMouseEvents(handlers);
		prepareKeyEvents(handlers);
		prepareTextEvents(handlers);
		prepareFocusEvents(handlers);
		return handlers.toString();
	}

	public String getStringToPrint() throws JspException {

		// if true do print nothing
		if(isInPlaceTagIntegration())
			return "";



		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		results.append(this.getTagClose());
		return results.toString();
	}


    protected String getExtraAttributes() {

        if (extra == null) return "";

        StringBuffer sb = new StringBuffer(512);

        String[] s = extra.split("\\" + separator);

        for(int i=0;i<s.length;i++) {

            String[] ss = s[i].split("=");

            if (ss.length != 2) continue;

            sb.append(" ").append(ss[0].trim()).append("=\"").append(ss[1].trim()).append('"');

        }

        return sb.toString();
    }


	/** Return Maxlength in component */
	public String getMaxlength() {
		return maxlength;
	}
	/** Sets Maxlength in component*/
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	/** Return Size component */
	public String getSize() {
		return size;
	}
	/** Set size component */
	public void setSize(String size) {
		this.size = size;
	}
	/** Return state readonly component */
	public String getReadonly() {
		return readonly;
	}
	/** Set state readonly component. */
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
    public void setExtra(String extra) {

        this.extra = extra;

    }

    public void setSeparator(String separator) {

        this.separator = separator;

    }
}