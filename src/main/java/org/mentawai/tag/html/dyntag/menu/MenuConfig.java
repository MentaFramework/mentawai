package org.mentawai.tag.html.dyntag.menu;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.html.dyntag.BaseConfig;
import org.mentawai.tag.html.dyntag.menu.listener.MenuListener;

/**
 * 
 * @author Robert Willian
 * 
 */

public class MenuConfig extends BaseConfig {
	private static final long serialVersionUID = 1L;

	/*
	 * Name of the skin to load
	 */
	private String skinName = null;

	/*
	 * Set the position of menu Horizontal or Vertical.
	 */
	private String horizontalOrVertical = null;

	/*
	 * Set the minimum width of de submenu.
	 */
	private String subMenuMinWidth = null;

	/*
	 * Set the maximum width of de submenu.
	 */
	private String subMenuMaxWidth = null;

	/*
	 * Open menu onclick ? false / true
	 */
	private String openMenuOnClick = null;

	/*
	 * Open submenu onclick ? false / true
	 */
	private String openSubMenuOnClick = null;

	/*
	 * Width of menu when vertical
	 */
	private String onVerticalWidth = null;

	/*
	 * Set if skin will load every page load @Default true
	 */
	private Boolean flush = true;

	public StringBuffer buildTag() throws JspException {
		StringBuffer results = new StringBuffer();
		Map<String, String> pars = new HashMap<String, String>();

		pars.put("horizontalOrVertical", horizontalOrVertical);
		pars.put("minWidth", subMenuMinWidth);
		pars.put("maxWidth", subMenuMaxWidth);
		pars.put("openMenuOnClick", openMenuOnClick);
		pars.put("openSubMenuOnClick", openSubMenuOnClick);
		pars.put("onVerticalWidth", onVerticalWidth);

		if (MenuListener.loadedSkin == null || MenuListener.loadedSkin != skinName || flush) {
			new MenuListener().loadSkin(skinName);
		}

		results.append(MenuConfigSuport.doConfig(pars));
		results.append(buldImportCssFile(MenuListener.SKINS.get(MenuListener.loadedSkin)).toString());
		results.append(buldImportJsFile(MenuListener.SKINS.get(MenuListener.loadedSkin)).toString());

		return results;
	}

	@Override
	public String getStringToPrint() throws JspException {
		StringBuffer result = new StringBuffer();
		result.append(buildTag().toString());
		return result.toString();
	}

	public void setSkinName(String skinName) {
		this.skinName = skinName.trim().toLowerCase();
	}

	public void setPosition(String horizontalOrVertical) {
		this.horizontalOrVertical = horizontalOrVertical;
	}

	public void setOpenMenuOnClick(String openMenuOnClick) {
		this.openMenuOnClick = openMenuOnClick;
	}

	public void setOpenSubMenuOnClick(String openSubMenuOnClick) {
		this.openSubMenuOnClick = openSubMenuOnClick;
	}

	public void setOnVerticalWidth(String onVerticalWidth) {
		this.onVerticalWidth = onVerticalWidth;
	}

	public void setSubMenuMaxWidth(String subMenuMaxWidth) {
		this.subMenuMaxWidth = subMenuMaxWidth;
	}

	public void setSubMenuMinWidth(String subMenuMinWidth) {
		this.subMenuMinWidth = subMenuMinWidth;
	}

	public void setFlush(Boolean flush) {
		this.flush = flush;
	}
}
