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
package org.mentawai.tag.html.dyntag;

import java.util.Locale;

import javax.servlet.jsp.JspException;

import org.mentawai.i18n.I18N;

public abstract class BaseTag extends Base {

	private static final long serialVersionUID = 1L;

	/**
	 * Diretorio onde serao copiados os resources das tags.
	 */
	public static String BASE_DIR = "/dyntags/";


	//Atributes Standards.

	/** Name in component . */
	private String name = null;
	/** Id in compoenent. */
	private String id = null;
	/** Class in compoenent. */
	private String klass = null;
	/** Disabled component. */
	private String disabled  = null;
	/** Style in component. */
	private String klassStyle = "";
	/** Title(Hint) in component. */
	private String title = null;
	/** Key i18n */
	private String keyI18n = null;
	/** noPrefix in i18n */
   	 private boolean noPrefix = false;
    	/** Value component */
	private String textAlign  = "left";
	/** Value component */
	private String value  = null;


	//  Mouse Events
    /** Mouse click event. */
    private String onclick = null;

    /** Mouse double click event. */
    private String ondblclick = null;

    /** Mouse over component event. */
    private String onmouseover = null;

    /** Mouse exit component event. */
    private String onmouseout = null;

    /** Mouse moved over component event. */
    private String onmousemove = null;

    /** Mouse pressed on component event. */
    private String onmousedown = null;

    /** Mouse released on component event. */
    private String onmouseup = null;

    //  Keyboard Events

    /** Key down in component event. */
    private String onkeydown = null;

    /** Key released in component event. */
    private String onkeyup = null;

    /** Key down and up together in component event. */
    private String onkeypress = null;

    // Text Events

    /** Text selected in component event. */
    private String onselect = null;

    /** Content changed after component lost focus event. */
    private String onchange = null;

    // Focus Events and States

    /** Component lost focus event. */
    private String onblur = null;

    /** Component has received focus event. */
    private String onfocus = null;


	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getId() {
		if( this.id==null || this.id.trim().equalsIgnoreCase("") )
			return this.name;

			return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getKlass() {
		return klass;
	}
	public void setKlass(String klass) {
		this.klass = klass;
	}
	public String getKlassStyle() {
		return klassStyle;
	}
	public void setKlassStyle(String klassStyle) {
		this.klassStyle = klassStyle;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}





	public String getStringToPrint() throws JspException {
		// TODO Auto-generated method stub
		return null;
	}

//	 Mouse Events

    /** Sets the onClick event handler. */
    public void setOnclick(String onClick) {
        this.onclick = onClick;
    }

    /** Returns the onClick event handler. */
    public String getOnclick() {
        return onclick;
    }

    /** Sets the onDblClick event handler. */
    public void setOndblclick(String onDblClick) {
        this.ondblclick = onDblClick;
    }

    /** Returns the onDblClick event handler. */
    public String getOndblclick() {
        return ondblclick;
    }

    /** Sets the onMouseDown event handler. */
    public void setOnmousedown(String onMouseDown) {
        this.onmousedown = onMouseDown;
    }

    /** Returns the onMouseDown event handler. */
    public String getOnmousedown() {
        return onmousedown;
    }

    /** Sets the onMouseUp event handler. */
    public void setOnmouseup(String onMouseUp) {
        this.onmouseup = onMouseUp;
    }

    /** Returns the onMouseUp event handler. */
    public String getOnmouseup() {
        return onmouseup;
    }

    /** Sets the onMouseMove event handler. */
    public void setOnmousemove(String onMouseMove) {
        this.onmousemove = onMouseMove;
    }

    /** Returns the onMouseMove event handler. */
    public String getOnmousemove() {
        return onmousemove;
    }

    /** Sets the onMouseOver event handler. */
    public void setOnmouseover(String onMouseOver) {
        this.onmouseover = onMouseOver;
    }

    /** Returns the onMouseOver event handler. */
    public String getOnmouseover() {
        return onmouseover;
    }

    /** Sets the onMouseOut event handler. */
    public void setOnmouseout(String onMouseOut) {
        this.onmouseout = onMouseOut;
    }

    /** Returns the onMouseOut event handler. */
    public String getOnmouseout() {
        return onmouseout;
    }

    // Keyboard Events

    /** Sets the onKeyDown event handler. */
    public void setOnkeydown(String onKeyDown) {
        this.onkeydown = onKeyDown;
    }

    /** Returns the onKeyDown event handler. */
    public String getOnkeydown() {
        return onkeydown;
    }

    /** Sets the onKeyUp event handler. */
    public void setOnkeyup(String onKeyUp) {
        this.onkeyup = onKeyUp;
    }

    /** Returns the onKeyUp event handler. */
    public String getOnkeyup() {
        return onkeyup;
    }

    /** Sets the onKeyPress event handler. */
    public void setOnkeypress(String onKeyPress) {
        this.onkeypress = onKeyPress;
    }

    /** Returns the onKeyPress event handler. */
    public String getOnkeypress() {
        return onkeypress;
    }

    // Text Events

    /** Sets the onChange event handler. */
    public void setOnchange(String onChange) {
        this.onchange = onChange;
    }

    /** Returns the onChange event handler. */
    public String getOnchange() {
        return onchange;
    }

    /** Sets the onSelect event handler. */
    public void setOnselect(String onSelect) {
        this.onselect = onSelect;
    }

    /** Returns the onSelect event handler. */
    public String getOnselect() {
        return onselect;
    }

    // Focus Events and States

    /** Sets the onBlur event handler. */
    public void setOnblur(String onBlur) {
        this.onblur = onBlur;
    }

    /** Returns the onBlur event handler. */
    public String getOnblur() {
        return onblur;
    }

    /** Sets the onFocus event handler. */
    public void setOnfocus(String onFocus) {
        this.onfocus = onFocus;
    }

    /** Returns the onFocus event handler. */
    public String getOnfocus() {
        return onfocus;
    }

	protected void prepareMouseEvents(StringBuffer handlers) {
        prepareAttribute(handlers, "onclick", getOnclick());
        prepareAttribute(handlers, "ondblclick", getOndblclick());
        prepareAttribute(handlers, "onmouseover", getOnmouseover());
        prepareAttribute(handlers, "onmouseout", getOnmouseout());
        prepareAttribute(handlers, "onmousemove", getOnmousemove());
        prepareAttribute(handlers, "onmousedown", getOnmousedown());
        prepareAttribute(handlers, "onmouseup", getOnmouseup());
    }

    protected void prepareKeyEvents(StringBuffer handlers) {
        prepareAttribute(handlers, "onkeydown", getOnkeydown());
        prepareAttribute(handlers, "onkeyup", getOnkeyup());
        prepareAttribute(handlers, "onkeypress", getOnkeypress());
    }

    protected void prepareTextEvents(StringBuffer handlers) {
        prepareAttribute(handlers, "onselect", getOnselect());
        prepareAttribute(handlers, "onchange", getOnchange());
    }

    protected void prepareFocusEvents(StringBuffer handlers) {
        prepareAttribute(handlers, "onblur", getOnblur());
        prepareAttribute(handlers, "onfocus", getOnfocus());
    }

    protected boolean isXhtml() {
        return true;
    }

    /** Method to close compoenent */
	protected String getTagClose() {
		return this.isXhtml() ? " />" : ">";
	}

	public String getI18nByKey(String _keyI18n,boolean _noPrefix) throws JspException {
		I18N [] props = (I18N []) pageContext.getAttribute("_i18n");
        Locale loc = (Locale) pageContext.getAttribute("_locale");
        String prefix = (String) pageContext.getAttribute("_prefix");

        if (prefix != null && ! _noPrefix) {
        	_keyI18n = prefix + "." + _keyI18n;
        }

		if (props == null || loc == null) {
			throw new JspException("i18n tag needs a useI18N tag in the same page!");
		}

        for(int i=props.length-1;i>=0;i--) {
            if (props[i] == null) continue;
            if (props[i].hasKey(_keyI18n)) {
                return props[i].get(_keyI18n);
            }
        }

        StringBuffer sb = new StringBuffer(35);
        sb.append('!');
        sb.append(loc.toString());
        sb.append('.');
        sb.append(_keyI18n);
        sb.append('!');

		return sb.toString();
	}
	/**
	 * @return Returns the keyI18n.
	 * @throws JspException
	 */
	public String getKeyI18n(){
		return this.keyI18n;
	}
	/**
	 * @param keyI18n The keyI18n to set.
	 */
	public void setKeyI18n(String keyI18n) {
		this.keyI18n = keyI18n;
	}
	/**
	 * @return Returns the noPrefix.
	 */
	public boolean isNoPrefix() {
		return noPrefix;
	}
	/**
	 * @param noPrefix The noPrefix to set.
	 */
	public void setNoPrefix(boolean noPrefix) {
		this.noPrefix = noPrefix;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}
	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

}