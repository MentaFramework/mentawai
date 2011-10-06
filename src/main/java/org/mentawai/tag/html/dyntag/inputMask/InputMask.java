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
package org.mentawai.tag.html.dyntag.inputMask;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAdapter;

import net.sf.json.JSONObject;

import org.mentawai.tag.html.ajax.InPlaceTag;
import org.mentawai.tag.html.dyntag.inputText.InputText;

public class InputMask extends InputText {

	private static final long serialVersionUID = 1L;

	private String maskDefined  = null;
	private String maskCustom   = null;

	/**
	 * @return Returns the maskCustom.
	 */
	public String getMaskCustom() {
		return maskCustom;
	}
	/**
	 * @param maskCustom The maskCustom to set.
	 */
	public void setMaskCustom(String maskCustom) {
		this.maskCustom = maskCustom;
	}
	/**
	 * @return Returns the maskDefined.
	 */
	public String getMaskDefined() {
		return maskDefined;
	}
	/**
	 * @param maskDefined The maskDefined to set.
	 */
	public void setMaskDefined(String maskDefined) {
		this.maskDefined = maskDefined;
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

			if(getMaskCustom() != null || maskDefined != null){
				if(getMaskDefined() != null){

					if(getMaskDefined().equalsIgnoreCase("CEP")){
						options.put("par1", "99.999-999");

					} else

					if(getMaskDefined().equalsIgnoreCase("CNPJ")){
						options.put("par1", "99.999.999/9999-99");

					} else

					if(getMaskDefined().equalsIgnoreCase("CPF")){
						options.put("par1", "999.999.999-99");

					} else

					if(getMaskDefined().equalsIgnoreCase("FONE")){
						options.put("par1", "(99)9999-9999");

					}
				} else {

					if(getMaskCustom() != null){
						options.put("par1", getMaskCustom());
					}

				}
			}


			// Seta os parametros da tag
			ipt.setMaskerOptions(options);
			ipt.setTypeMask(InPlaceTag.INPUTMASK);


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
		if(isInPlaceTagIntegration())
			return new StringBuffer(1);


    	StringBuffer results = new StringBuffer(super.buildTag().toString()+getTagClose());
    	if(getMaskDefined() != null || (getMaskCustom() != null)){
		    	results.append("\n");
		        results.append("\t\t<script type=\"text/javascript\">");
			    if(getMaskDefined()!= null){
			       if(getMaskDefined().toUpperCase().equals("CEP")){
			    	  results.append("inputMask(document.getElementById(\""+getId()+"\"),\"99.999-999\");");
			   	   }
			       if(getMaskDefined().toUpperCase().equals("CNPJ")){
			    	   results.append("inputMask(document.getElementById(\""+getId()+"\"),\"99.999.999/9999-99\");");
				   }
			       if(getMaskDefined().toUpperCase().equals("CPF")){
			    	   results.append("inputMask(document.getElementById(\""+getId()+"\"),\"999.999.999-99\");");
				   }
			       if(getMaskDefined().toUpperCase().equals("FONE")){
			    	   results.append("inputMask(document.getElementById(\""+getId()+"\"),\"(99)9999-9999\");");
				   }
			    }
			    else{
			      if(getMaskCustom() != null){
			    	  results.append("inputMask(document.getElementById(\""+getId()+"\"),\""+getMaskCustom()+"\");");
			      }
			    }
			    results.append("</script>");
    	}
	    return results;
	}

	public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}
}