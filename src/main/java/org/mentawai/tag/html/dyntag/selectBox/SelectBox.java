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
package org.mentawai.tag.html.dyntag.selectBox;
import javax.servlet.jsp.JspException;

import org.mentawai.tag.html.dyntag.BaseTag;
public class SelectBox extends BaseTag {

	private String listFrom=null;
	private String listTo=null;
	private String klassButton=null;
	private String klassStyleButton=null;
	private String sort="false";
	private String moveListFrom=null;
	private String moveListTo=null;
	/**
	 * Construtor Padrao
	 */
	public SelectBox(){
      // TODO Auto-generated constructor stub
	}
	/** Method to build tag */
    protected StringBuffer buildTag() {
		String s,c=null;
		////////////////////////////////
		// VERIFICA SE TERA ORDENACAO //
		////////////////////////////////
		if((!getSort().equals(null))&& (getSort().equals("true"))){
		  	s="true";
		}
		else{
			s="false";
		}
		///////////////////////////
		// VERIFICA SE TEM CLASS //
		///////////////////////////
		if(getKlassButton()!=null){
		  c = "class=\""+getKlassButton().trim()+"\"";
		}
		else{
		  c="";
		}
    	StringBuffer results = new StringBuffer("");
		results.append("\n\t\t<table cellpadding=\"1\" cellspacing=\"0\" border=\"0\">");
    	results.append("\n\t\t  <tr>");
    	results.append("\n\t\t     <td>&nbsp;</td>");
    	results.append("\n\t\t     <td><input type=\"button\" "+c+" name=\""+getName()+"_btnLeft\" value=\"&gt;\" onclick=\"moveSelectedOptions(this.form['"+getListFrom()+"'],this.form['"+getListTo()+"'],"+s.trim()+",'')\" style=\"width:24px;height:24px;"+getKlassStyle()+"\"/></td>");
    	results.append("\n\t\t	  <td>&nbsp;</td>");
    	results.append("\n\t\t  </tr>");
    	results.append("\n\t\t  <tr>");

    	//VERIFICA SE MOVE A LIST_FROM
    	if((getMoveListFrom()!= null) && (getMoveListFrom().equals("true"))){
    	  results.append("\n\t\t     <td><input type=\"button\" name=\""+getName()+"_btnLeftUp\"  "+c+" value=\"&Lambda;\" onclick=\"moveOptionUp(this.form['"+getListFrom()+"'])\" style=\"width:24px;height:24px\"/></td>");
    	}
    	else{
    	  results.append("\n\t\t     <td>&nbsp;</td>");
    	}

    	results.append("\n\t\t     <td><input type=\"button\" name=\""+getName()+"_btnLeftAll\" "+c+" value=\"&gt;&gt;\" onclick=\"moveAllOptions(this.form['"+getListFrom()+"'],this.form['"+getListTo()+"'],"+s.trim()+",'')\" style=\"width:24px;height:24px;"+getKlassStyle()+"\"></td>");

    	//VERIFICA SE MOVE A LIST_TO
    	if((getMoveListTo()!= null) && (getMoveListTo().trim().equals("true"))){
    	  results.append("\n\t\t 	   <td><input type=\"button\" name=\""+getName()+"_btnRightUp\" "+c+" value=\"&Lambda;\" onclick=\"moveOptionUp(this.form['"+getListTo()+"'])\" style=\"width:24px;height:24px\"/></td>");
    	}
    	else{
    	  results.append("\n\t\t 	   <td>&nbsp;</td>");
    	}

    	results.append("\n\t\t  </tr>");
    	results.append("\n\t\t  <tr>");

    	//VERIFICA SE MOVE A LIST_FROM
    	if((getMoveListFrom()!= null) && (getMoveListFrom().trim().equals("true"))){
    	  results.append("\n\t\t     <td><input type=\"button\" name=\""+getName()+"_btnLeftDown\"  "+c+" value=\"V\" onclick=\"moveOptionDown(this.form['"+getListFrom()+"'])\" style=\"width:24px;height:24px\"/></td>");
    	}
    	else{
    	  results.append("\n\t\t     <td>&nbsp;</td>");
        }

    	results.append("\n\t\t     <td><input type=\"button\" name=\""+getName()+"_btnRight\"     "+c+" value=\"&lt;\" onclick=\"moveSelectedOptions(this.form['"+getListTo()+"'],this.form['"+getListFrom()+"'],"+s.trim()+",'')\" style=\"width:24px;height:24px;"+getKlassStyle()+"\"/></td>");

    	//VERIFICA SE MOVE A LIST_TO
    	if((getMoveListTo()!= null) && (getMoveListTo().equals("true"))){
    	  results.append("\n\t\t     <td><input type=\"button\" name=\""+getName()+"_btnRightDown\" "+c+" value=\"V\" onclick=\"moveOptionDown(this.form['"+getListTo()+"'])\" style=\"width:24px;height:24px\"/></td>");
        }
    	else{
    	  results.append("\n\t\t     <td>&nbsp;</td>");
        }

    	results.append("\n\t\t  </tr>");
    	results.append("\n\t\t  <tr>");
    	results.append("\n\t\t     <td>&nbsp;</td>");
    	results.append("\n\t\t     <td><input type=\"button\" name=\""+getName()+"_btnRightAll\" "+c+" value=\"&lt;&lt;\" onclick=\"moveAllOptions(this.form['"+getListTo()+"'],this.form['"+getListFrom()+"'],"+s.trim()+",'')\" style=\"width:24px;height:24px;"+getKlassStyle()+"\"/></td>");
    	results.append("\n\t\t     <td>&nbsp;</td>");
    	results.append("\n\t\t  </tr>");
    	results.append("\n\t\t</table>");
    	return results;
	}

    public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}

	/**
	 * @return Returns the klassButton.
	 */
	public String getKlassButton() {
		return klassButton;
	}
	/**
	 * @param klassButton The klassButton to set.
	 */
	public void setKlassButton(String klassButton) {
		this.klassButton = klassButton;
	}

	/**
	 * @return Returns the klassStyleButton.
	 */
	public String getKlassStyleButton() {
		return klassStyleButton;
	}
	/**
	 * @param klassStyleButton The klassStyleButton to set.
	 */
	public void setKlassStyleButton(String klassStyleButton) {
		this.klassStyleButton = klassStyleButton;
	}

	/**
	 * @return Returns the listFrom.
	 */
	public String getListFrom() {
		return listFrom;
	}
	/**
	 * @param listFrom The listFrom to set.
	 */
	public void setListFrom(String listFrom) {
		this.listFrom = listFrom;
	}
	/**
	 * @return Returns the listTo.
	 */
	public String getListTo() {
		return listTo;
	}
	/**
	 * @param listTo The listTo to set.
	 */
	public void setListTo(String listTo) {
		this.listTo = listTo;
	}

	/**
	 * @return Returns the sort.
	 */
	public String getSort() {
		return sort;
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	/**
	 * @return Returns the moveListFrom.
	 */
	public String getMoveListFrom() {
		return moveListFrom;
	}
	/**
	 * @param moveListFrom The moveListFrom to set.
	 */
	public void setMoveListFrom(String moveListFrom) {
		this.moveListFrom = moveListFrom;
	}
	/**
	 * @return Returns the moveListTo.
	 */
	public String getMoveListTo() {
		return moveListTo;
	}
	/**
	 * @param moveListTo The moveListTo to set.
	 */
	public void setMoveListTo(String moveListTo) {
		this.moveListTo = moveListTo;
	}

}
