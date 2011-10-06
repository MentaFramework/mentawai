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
package org.mentawai.tag.html.dyntag.formSkin;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.mentawai.i18n.I18N;
/**
 *	@author Alex Fortuna
 *	@since 1.0
 */
public class FormSkin extends SimpleTagSupport {
	
	//Atributos padrao
	/**
	 * This attribute specifies a form processing agent. 
	 * User agent behavior for a value other than an HTTP URI is undefined. 
	 */
	private String  action = null;
	/**
	 * This attribute specifies which HTTP method will be used to submit the form data set. 
	 * Possible (case-insensitive) values are "get" (the default) and "post". See the section on form submission for usage information. 
	 */
	private String  method="get";
	/**
	 * This attribute specifies a comma-separated list of content types that a server processing this form will handle correctly. 
	 * User agents may use this information to filter out non-conforming files when prompting a user to select files to be sent to the server (cf. the INPUT element when type="file"). 
	 */
	private String  accept= null; 
	/**
	 * This attribute specifies the content type used to submit the form to the server (when the value of method is "post"). 
	 * The default value for this attribute is "application/x-www-form-urlencoded". 
	 * The value "multipart/form-data" should be used in combination with the INPUT element, type="file". 
	 */
	private String  enctype=null;
	/**
	 * This attribute specifies the list of character encodings for input data that is accepted by the server processing this form. 
	 * The value is a space- and/or comma-delimited list of charset values. The client must interpret this list as an exclusive-or list, i.e., the server is able to accept any single character encoding per entity received. 
     * The default value for this attribute is the reserved string "UNKNOWN". User agents may interpret this value as the character encoding that was used to transmit the document containing this FORM element.
	 */
	private String  accept_charset= null; 
	/**
	 * This attribute assigns a class name or set of class names to an element. Any number of elements may be assigned the same class name or names. 
	 * Multiple class names must be separated by white space characters.
	 */
	private String  klass= null; 
	/**
	 * This attribute specifies the base direction of directionally neutral text (i.e., text that doesn't have inherent directionality as defined in [UNICODE]) in an element's content and attribute values. 
	 * It also specifies the directionality of tables. Possible values: 
     * LTR: Left-to-right text or table. 
     * RTL: Right-to-left text or table. 
	 */
	private String  dir= null; 
	/**
	 * This attribute assigns a name to an element. 
	 * This name must be unique in a document. 
	 */
	private String  id= null;
	/**
	 * This attribute specifies the base language of an element's attribute values and text content. 
	 * The default value of this attribute is unknown.
	 */
	private String  lang= null; 
	/**
	 * This attribute names the element so that it may be referred to from style sheets or scripts. Note. 
	 * This attribute has been included for backwards compatibility. 
	 * Applications should use the id attribute to identify elements.
	 */
	private String  name= null;
	/**
	 * The onclick event occurs when the pointing device button is clicked over an element. 
	 * This attribute may be used with most elements.
	 */
	private String  onclick= null;
	/**
	 * The ondblclick event occurs when the pointing device button is double clicked over an element. 
	 * This attribute may be used with most elements.
	 */
	private String  ondblclick= null;
	/**
	 * N/C
	 */
	private String	onhelp= null;
	/**
	 * The onkeydown event occurs when a key is pressed down over an element. 
	 * This attribute may be used with most elements.
	 */
	private String	onkeydown= null;
	/**
	 * The onkeypress event occurs when a key is pressed and released over an element. 
	 * This attribute may be used with most elements.
	 */
	private String	onkeypress= null;
	/**
	 * The onkeyup event occurs when a key is released over an element. 
	 * This attribute may be used with most elements.
	 */
	private String	onkeyup= null;
	/**
	 * The onmousedown event occurs when the pointing device button is pressed over an element. 
	 * This attribute may be used with most elements.
	 */
	private String	onmousedown= null;
	/**
	 * The onmousemove event occurs when the pointing device is moved while it is over an element. 
	 * This attribute may be used with most elements.
	 */
	private String	onmousemove= null;
	/**
	 * The onmouseout event occurs when the pointing device is moved away from an element. 
	 * This attribute may be used with most elements.
	 */
	private String	onmouseout= null;
	/**
	 * The onmousemove event occurs when the pointing device is moved while it is over an element. 
	 * This attribute may be used with most elements.
	 */
	private String 	onmouseover= null;
	/**
	 * The onmouseup event occurs when the pointing device button is released over an element. 
	 * This attribute may be used with most elements. 
	 */
	private String 	onmouseup= null;
	/**
	 * The onreset event occurs when a form is reset. 
	 * It only applies to the FORM element.
	 */
	private String 	onreset= null;
	/**
	 * The onsubmit event occurs when a form is submitted. 
	 * It only applies to the FORM element.
	 */
	private String 	onsubmit= null;
	/**
	 * This attribute specifies style information for the current element.
	 */
	private String  klassStyle= null;
	/**
	 * This attribute specifies the name of a frame where a document is to be opened. 
	 */
	private String  target= null;
	/**
	 * This attribute offers advisory information about the element for which it is set. 
	 */
	private String  title= null;
	
	//Atributos novos
	/**
	 * This attribute specifies the caption of form
	 */
	private String  caption= null;
	/**
	 * This attribute specifies the event onclick of button close of form
	 */
	private String  btnCloseOnclick= null;
	/**
	 * This attribute offers advisory information about the element button close for which it is set. 
	 */
	private String  btnCloseTitle= null;
	/**
	 * This attribute align of form, Possible values:
	 * LEFT   - it aligns the form the left
	 * CENTER - it aligns the form the center
	 * RIGHT  - it aligns the form the right
	 */
	private String  align= "center";
	
	private String  width= "100%";
	
	private String  captionI18nKey;
    private boolean noPrefix = false;
	
	/**
	 * build componente
	 */
	private StringWriter result = new StringWriter();
	public void doTag() throws JspException, IOException {
		StringWriter evalResult = new StringWriter();
		result.write("<form");
		prepareAttribute(result,"action",getAction());
		prepareAttribute(result,"method",getMethod());
		prepareAttribute(result,"accept",getAccept());
		prepareAttribute(result,"enctype",getEnctype());
		prepareAttribute(result,"accept-charset",getAccept_charset());
		prepareAttribute(result,"class",getKlass());
		prepareAttribute(result,"dir",getDir());
		prepareAttribute(result,"id",getId());
		prepareAttribute(result,"lang",getLang());
		prepareAttribute(result,"name",getName());
		prepareAttribute(result,"onclick",getOnclick());
		prepareAttribute(result,"ondblclick",getOndblclick());
		prepareAttribute(result,"onhelp",getOnhelp());
		prepareAttribute(result,"onkeydown",getOnkeydown());
		prepareAttribute(result,"onkeypress",getOnkeypress());
		prepareAttribute(result,"onkeyup",getOnkeyup());
		prepareAttribute(result,"onmousedown",getOnmousedown());
		prepareAttribute(result,"onmousemove",getOnmousemove());
		prepareAttribute(result,"onmouseout",getOnmouseout());
		prepareAttribute(result,"onmouseover",getOnmouseover());
		prepareAttribute(result,"onmouseup",getOnmouseup());
		prepareAttribute(result,"onreset",getOnreset());
		prepareAttribute(result,"onsubmit",getOnsubmit());
		prepareAttribute(result,"style",getKlassStyle());
		prepareAttribute(result,"target",getTarget());
		prepareAttribute(result,"title",getTitle());
		result.write(">");
		result.write("\n\t<div");
		prepareAttribute(result,"align",getAlign());
		result.write(">");
		//Cabecalho do Formulario
		result.write("\n\t<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\""+getWidth()+"\">");
		result.write("\n\t  <tr>");		
		result.write("\n\t    <td class=\"mtwFormSupLeft\">&nbsp;</td>");
		//Verifica primeiro se tem internacionalizacao
		if((captionI18nKey != null) && (!captionI18nKey.equals(""))){
			result.write("\n\t    <td class=\"mtwFormSupCenter\">"+getCaptionI18nKey()+"</td>");	
		}
		else{
			if((getCaption() != null) && (!getCaption().equals(""))){
			   result.write("\n\t    <td class=\"mtwFormSupCenter\">"+getCaption()+"</td>");
			}
			else{
			   result.write("\n\t    <td class=\"mtwFormSupCenter\">&nbsp;</td>"); 					
			}   
		}
		result.write("\n\t    <td class=\"mtwFormSupRight\"><input type=\"button\" class=\"mtwFormButtonClose\" title=\""+ getBtnCloseTitle()+"\" onclick=\""+getBtnCloseOnclick()+"\" />&nbsp;&nbsp;</td>");
		result.write("\n\t  </tr>");
		result.write("\n\t</table>");
		//Corpo do Formulario
		result.write("\n\t<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\""+getWidth()+"\">");
		result.write("\n\t  <tr>");		
		result.write("\n\t    <td class=\"mtwFormBorderLeft\">&nbsp;</td>");
		result.write("\n\t    <td class=\"mtwFormBody\">");
		result.write("\n\t    <!-- BEGIN BODY FORM -->");
		getJspBody().invoke(evalResult);
		evalResult.getBuffer().delete(0,evalResult.getBuffer().length());
		getJspBody().invoke(evalResult);
		result.write(evalResult.getBuffer().toString());
		result.write("\t    <!-- END BODY FORM -->");
		result.write("\n\t    </td>");
		result.write("\n\t    <td class=\"mtwFormBorderRight\">&nbsp;</td>");
		result.write("\n\t  </tr>");
		result.write("\n\t</table>");
		result.write("\n\t</div>"); //Fecha DIV
		result.write("\n</form>");
		getJspContext().getOut().print(result.getBuffer());
	}	
	
	protected void prepareAttribute(StringWriter handlers, String name, Object value) {
        if ((value != null) && (value != "")) {
            handlers.write(" ");
            handlers.write(name);
            handlers.write("=\"");
            handlers.write(((String)value));
            handlers.write("\"");
        }
    }
	
	/**
	 * @return Returns the accept.
	 */
	public String getAccept() {
		return accept;
	}
	/**
	 * @param accept The accept to set.
	 */
	public void setAccept(String accept) {
		this.accept = accept;
	}
	/**
	 * @return Returns the accept_charset.
	 */
	public String getAccept_charset() {
		return accept_charset;
	}
	/**
	 * @param accept_charset The accept_charset to set.
	 */
	public void setAccept_charset(String accept_charset) {
		this.accept_charset = accept_charset;
	}
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return Returns the btnCloseOnclick.
	 */
	public String getBtnCloseOnclick() {
		return btnCloseOnclick;
	}
	/**
	 * @param btnCloseOnclick The btnCloseOnclick to set.
	 */
	public void setBtnCloseOnclick(String btnCloseOnclick) {
		this.btnCloseOnclick = btnCloseOnclick;
	}
	/**
	 * @return Returns the btnCloseTitle.
	 */
	public String getBtnCloseTitle() {
		return btnCloseTitle;
	}
	/**
	 * @param btnCloseTitle The btnCloseTitle to set.
	 */
	public void setBtnCloseTitle(String btnCloseTitle) {
		this.btnCloseTitle = btnCloseTitle;
	}
	/**
	 * @return Returns the caption.
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * @return Returns the dir.
	 */
	public String getDir() {
		return dir;
	}
	/**
	 * @param dir The dir to set.
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	/**
	 * @return Returns the enctype.
	 */
	public String getEnctype() {
		return enctype;
	}
	/**
	 * @param enctype The enctype to set.
	 */
	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the klass.
	 */
	public String getKlass() {
		return klass;
	}
	/**
	 * @param klass The klass to set.
	 */
	public void setKlass(String klass) {
		this.klass = klass;
	}
	/**
	 * @return Returns the lang.
	 */
	public String getLang() {
		return lang;
	}
	/**
	 * @param lang The lang to set.
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	/**
	 * @return Returns the method.
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method The method to set.
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the onclick.
	 */
	public String getOnclick() {
		return onclick;
	}
	/**
	 * @param onclick The onclick to set.
	 */
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	/**
	 * @return Returns the ondblclick.
	 */
	public String getOndblclick() {
		return ondblclick;
	}
	/**
	 * @param ondblclick The ondblclick to set.
	 */
	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}
	/**
	 * @return Returns the onhelp.
	 */
	public String getOnhelp() {
		return onhelp;
	}
	/**
	 * @param onhelp The onhelp to set.
	 */
	public void setOnhelp(String onhelp) {
		this.onhelp = onhelp;
	}
	/**
	 * @return Returns the onkeydown.
	 */
	public String getOnkeydown() {
		return onkeydown;
	}
	/**
	 * @param onkeydown The onkeydown to set.
	 */
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}
	/**
	 * @return Returns the onkeypress.
	 */
	public String getOnkeypress() {
		return onkeypress;
	}
	/**
	 * @param onkeypress The onkeypress to set.
	 */
	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}
	/**
	 * @return Returns the onkeyup.
	 */
	public String getOnkeyup() {
		return onkeyup;
	}
	/**
	 * @param onkeyup The onkeyup to set.
	 */
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}
	/**
	 * @return Returns the onmousedown.
	 */
	public String getOnmousedown() {
		return onmousedown;
	}
	/**
	 * @param onmousedown The onmousedown to set.
	 */
	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}
	/**
	 * @return Returns the onmousemove.
	 */
	public String getOnmousemove() {
		return onmousemove;
	}
	/**
	 * @param onmousemove The onmousemove to set.
	 */
	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}
	/**
	 * @return Returns the onmouseout.
	 */
	public String getOnmouseout() {
		return onmouseout;
	}
	/**
	 * @param onmouseout The onmouseout to set.
	 */
	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}
	/**
	 * @return Returns the onmouseover.
	 */
	public String getOnmouseover() {
		return onmouseover;
	}
	/**
	 * @param onmouseover The onmouseover to set.
	 */
	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}
	/**
	 * @return Returns the onmouseup.
	 */
	public String getOnmouseup() {
		return onmouseup;
	}
	/**
	 * @param onmouseup The onmouseup to set.
	 */
	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}
	/**
	 * @return Returns the onreset.
	 */
	public String getOnreset() {
		return onreset;
	}
	/**
	 * @param onreset The onreset to set.
	 */
	public void setOnreset(String onreset) {
		this.onreset = onreset;
	}
	/**
	 * @return Returns the onsubmit.
	 */
	public String getOnsubmit() {
		return onsubmit;
	}
	/**
	 * @param onsubmit The onsubmit to set.
	 */
	public void setOnsubmit(String onsubmit) {
		this.onsubmit = onsubmit;
	}

    public String getKlassStyle() {
		return klassStyle;
	}
	/**
	 * @param klassStyle The style to set.
	 */
	public void setKlassStyle(String klassStyle) {
		this.klassStyle = klassStyle;
	}
	/**
	 * @return Returns the target.
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target The target to set.
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the align.
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @param align The align to set.
	 */
	public void setAlign(String align) {
		this.align = align;
	}

	/**
	 * @return Returns the width.
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	
	/**
	 * @return Returns the captionI18nKey.
	 * @throws JspException 
	 */
	public String getCaptionI18nKey() throws JspException {
		I18N [] props = (I18N []) getJspContext().getAttribute("_i18n");
        Locale loc = (Locale) getJspContext().getAttribute("_locale");
        String prefix = (String) getJspContext().getAttribute("_prefix");
        
        if (prefix != null && ! isNoPrefix()) {
            setCaptionI18nKey(prefix + "." + captionI18nKey);
        }
		
		if (props == null || loc == null) {
			throw new JspException("i18n tag needs a useI18N tag in the same page!");
		}

        for(int i=props.length-1;i>=0;i--) {
            if (props[i] == null) continue;
            if (props[i].hasKey(captionI18nKey)) {
                return props[i].get(captionI18nKey);
            }
        }

        StringBuffer sb = new StringBuffer(35);
        sb.append('!');
        sb.append(loc.toString());
        sb.append('.');
        sb.append(captionI18nKey);
        sb.append('!');
        
		return sb.toString();
	}

	/**
	 * @param captionI18nKey The captionI18nKey to set.
	 */
	public void setCaptionI18nKey(String captionI18nKey) {
		this.captionI18nKey = captionI18nKey;
	}

	/**
	 * @return Returns the noPrefix.
	 */
	public boolean isNoPrefix() {
		return noPrefix;
	}

	public void setNoPrefix(boolean noPrefix) {
        this.noPrefix = noPrefix;
    }

	
}
