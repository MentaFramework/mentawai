package org.mentawai.tag.html.dyntag.menu;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Robert Willian
 */

public class MenuElement {

	public static final String TARGET_BLANK = "_blank";
	public static final String TARGET_SELF = "_self";
	public static final String TARGET_PARENT = "_parent";
	public static final String TARGET_TOP = "_top";
	
	private String text;

	private String href;
	
	private String target;

	private Set<MenuElement> childs;

	public MenuElement() {
	}

	public MenuElement(String text, String href) {
		this.text = text;
		this.href = href;
	}
	
	 public MenuElement(String text, String href, String target) {
		 	this.text = text;
		 	this.href = href;
		 	this.target = target;
		 }

	public void add(MenuElement menuElement) {
		if (this.getChilds() == null)
			this.childs = new LinkedHashSet<MenuElement>();
		this.childs.add(menuElement);
	}

	/**
	 * @return the childs
	 */
	public Set<MenuElement> getChilds() {
		return childs;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	
	public String getTarget() {
		return target;
	}
	
}
