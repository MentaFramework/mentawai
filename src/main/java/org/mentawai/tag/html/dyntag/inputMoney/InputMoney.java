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
package org.mentawai.tag.html.dyntag.inputMoney;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAdapter;

import net.sf.json.JSONObject;

import org.mentawai.tag.html.ajax.InPlaceTag;
import org.mentawai.tag.html.dyntag.inputText.InputText;

public class InputMoney extends InputText {

	private static final long serialVersionUID = 1L;

	private int decimals = 2;

	private String dec_point = ",";

	private String thousands_sep = ".";


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

			JSONObject options = new JSONObject();

			options.put("par1", decimals);
			options.put("par2", thousands_sep);
			options.put("par3", dec_point);

			// Seta os parametros da tag
			ipt.setMaskerOptions(options);
			ipt.setTypeMask(InPlaceTag.INPUTMONEY);

			if(getValue() != null) {
				ipt.setValue( customizeMask(getDecimals(), getThousands_sep(), getDec_point(), getValue()) );
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

			return true;
		}

		return false;
	}


	/** Method to build tag */
	public StringBuffer buildTag() {

		// if true, print nothing
		if(isInPlaceTagIntegration())
			return new StringBuffer(1);


		StringBuffer results = new StringBuffer(super.buildTag().toString());
		results.append(getTagClose());
		
		if( !(isDisabled() || isReadOnly()) ){
			results.append("\n");
			results.append("\t\t<script type=\"text/javascript\">");
			results.append("inputMoneyMask(document.getElementById(\"" + getId() + "\"),"
					+ getDecimals() + ",\"" + getThousands_sep() + "\",\""
					+ getDec_point() + "\")");
			results.append("</script>");
		}
		
		return results;
	}

	/*
	 * Formatador
	 */
	protected void prepareValue(StringBuffer results) {
		Object value = findObject(getName());
		if (value != null)
			results.append(" value=\"").append(
					customizeMask(getDecimals(), getThousands_sep(),
							getDec_point(), value)).append("\"");
		else if (getValue() != null)
			results.append(" value=\"").append(
					customizeMask(getDecimals(), getThousands_sep(),
							getDec_point(), getValue())).append("\"");
	}

	protected String makeZeros(int dec) {

      StringBuilder sb = new StringBuilder(dec);

      for(int i=0;i<dec;i++) sb.append("0");

      return sb.toString();

	}

	protected String formatNumber(Object object, int decimals, String decimal) {

		if(new Float(object.toString()) == 0.0)		// Inserted by Robert
			return "0." + makeZeros(decimals);

		if (object instanceof Float) {
			return new DecimalFormat("#,##0." + makeZeros(decimals), new DecimalFormatSymbols(new Locale("en", "US"))).format((Float) object);
		} else if (object instanceof String) {
			return new DecimalFormat("#,##0." + makeZeros(decimals), new DecimalFormatSymbols(new Locale("en", "US"))).format(new Float(object.toString()));
		} else if (object instanceof Double){
			return new DecimalFormat("#,##0." + makeZeros(decimals), new DecimalFormatSymbols(new Locale("en", "US"))).format(new Double(object.toString()));
		}else if (object instanceof BigDecimal) {
 			return new DecimalFormat("#,##0." + makeZeros(decimals), new DecimalFormatSymbols(new Locale("en", "US"))).format((BigDecimal) object);
 		}
		return object.toString();
	}

	protected String customizeMask(int decimals, String thousands,String decimal, Object number) {

      Float f = convert(number);

      if (f == null) return number.toString();

      String fmtNumber = formatNumber(f, decimals, decimal);

      if (!thousands.equals(",")) {

         fmtNumber = fmtNumber.replace(',', 'T');

      }

      if (!decimal.equals(".")) {

         fmtNumber = fmtNumber.replace('.', 'D');

      }

      fmtNumber = fmtNumber.replace('T', thousands.charAt(0));

      fmtNumber = fmtNumber.replace('D', decimal.charAt(0));

      return fmtNumber;

	}

   public Float convert(Object numero) {

      if (numero == null) return null;

      String s = numero.toString().trim();

      Float f;

      try {

         f = new Float(s);

         return f;

      } catch(NumberFormatException e) {

      }

      // tenta ajeitar o numero...
      if (s.indexOf(",") > 0){
         s = s.replace(".", "");
         s = s.replace(",", ".");
      }

      try {

         f = new Float(s);

         return f;

      } catch(NumberFormatException e) {

      }

      return null;
   }

	/*
	 * fim formatador
	 */

	public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}

	/**
	 * @return Returns the dec_point.
	 */
	public String getDec_point() {
		return dec_point;
	}

	/**
	 * @param dec_point
	 *            The dec_point to set.
	 */
	public void setDec_point(String dec_point) {
		this.dec_point = dec_point;
	}

	/**
	 * @return Returns the decimals.
	 */
	public int getDecimals() {
		return decimals;
	}

	/**
	 * @param decimals
	 *            The decimals to set.
	 */
	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}

	/**
	 * @return Returns the thousands_sep.
	 */
	public String getThousands_sep() {
		return thousands_sep;
	}

	/**
	 * @param thousands_sep
	 *            The thousands_sep to set.
	 */
	public void setThousands_sep(String thousands_sep) {
		this.thousands_sep = thousands_sep;
	}

}