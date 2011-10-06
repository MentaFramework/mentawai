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
package org.mentawai.tag.html.dyntag.inputDate;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAdapter;

import net.sf.json.JSONObject;

import org.mentawai.formatter.Formatter;
import org.mentawai.formatter.FormatterManager;
import org.mentawai.tag.html.ajax.InPlaceTag;
import org.mentawai.tag.html.dyntag.inputDate.listener.InputDateListener;
import org.mentawai.tag.html.dyntag.inputText.InputText;

public class InputDate extends InputText {

	private static final long serialVersionUID = 1L;

	private String dateFormat   = "dd/mm/yyyy";
	private String titleButton  = null;
	/** Retorna o Formato da Data. */
	public String getDateFormat() {
		return dateFormat;
	}
	/** Seta o Formato da Data */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTitleButton() {
		return titleButton;
	}
	public void setTitleButton(String titleButton) {
		this.titleButton = titleButton;
	}


	/**
	 * Este metodo verifica se esta tag esta dentro da tag inplace
	 * caso esteja, retorna true e nao printa a tag, e apenas passa
	 * os parametros para a tag inplace
	 *
	 * @see InPlaceTag
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

			JSONObject options = new JSONObject();

			options.put("par1", getDateFormat().replaceAll("[dmy]","9"));
			options.put("par2", getDateFormat().replaceFirst("yyyy","y"));

			// Seta os parametros da tag
			ipt.setMaskerOptions(options);
			ipt.setTypeMask(InPlaceTag.INPUTDATE);


			StringBuffer sb = new StringBuffer(20);
			prepareValue(sb);
			String[] splited = sb.toString().split("=");
			if(splited.length == 2)
				ipt.setValue(splited[1].replaceAll("\"", ""));

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

			return true;
		}

		return false;
	}


	/** Method to build tag */
    public StringBuffer buildTag() {

		// if true, print nothing
		if(isInPlaceTagIntegration()) {
			return new StringBuffer(1);
		}

    	StringBuffer results = new StringBuffer(super.buildTag().toString());
    	prepareAttribute(results,"maxlenght", String.valueOf(getDateFormat().length()));
    	results.append(getTagClose());
    	results.append("&nbsp;");
	    results.append("<img");
	    prepareAttribute(results,"id", getName() + "-button");
	    prepareAttribute(results,"align", "absmiddle");
	    prepareAttribute(results,"height", "16");
	    prepareAttribute(results,"width", "16");
	    prepareAttribute(results,"style", "cursor:pointer");
	    prepareAttribute(results,"src", req.getContextPath() + InputDateListener.IMG_COMPONTENT);
        prepareAttribute(results,"title", getTitleButton());
		prepareValue(results);
        results.append(getTagClose());
        results.append("\n");
        results.append("\t\t<script type=\"text/javascript\">");
        results.append("Calendar.setup({");
	    results.append("inputField: \""+ getId() +"\",");
	    results.append("button: \"" + getName()+"-button" +"\",");
	    
	    results.append("ifFormat: \"" + getNewFormat(getDateFormat()) + "\",");    
	    results.append("align: \"Tr\"");
	    results.append("});");
        results.append("</script>");
        results.append("\n");
        results.append("\t\t<script type=\"text/javascript\">");
        results.append("inputDateMask(document.getElementById(\"" + getId() 
        		+ "\"),\"" + getDateFormat().replaceAll("[dmy]","9") + "\");");
	    results.append("</script>");
	    return results;
	}

	/** Method to prepare value component */
	protected void prepareValue(StringBuffer results){
		Object value = findObject(getName());
		if (value != null) {
			if (value instanceof Date) {

				Formatter f = FormatterManager.getFixedDateFormatter();

				SimpleDateFormat formatter = new SimpleDateFormat(getDateFormat().replace('m','M'));

				Date date = (Date) value;

				if (f == null) {

					results.append(" value=\"").append(formatter.format(date)).append("\"");

				} else {

					results.append(" value=\"").append(f.format(date, loc)).append("\"");
				}

			} else {
				results.append(" value=\"").append(value).append("\"");
			}
		} else if (getValue() != null) {
			results.append(" value=\"").append(getValue()).append("\"");
		}
	}

	public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}
	
	public static String getNewFormat(final String currentFormat) {
		String newFormat = currentFormat;
		if (currentFormat.trim().equals("dd/mm/yyyy")) {
			newFormat = newFormat.replace("yyyy", "yY");
	    } else if (currentFormat.trim().equals("mm/dd/yyyy")) {
	    	newFormat = newFormat.replace("yyyy", "yY");
	    } 
		newFormat = newFormat.replaceFirst("d", "%");
		newFormat = newFormat.replaceFirst("m", "%");
		newFormat = newFormat.replaceFirst("y", "%");
		return newFormat;
	}

}