package org.mentawai.tag.html.dyntag.menu;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author Robert Willian
 * 
 */

public class Menu extends SimpleTagSupport {

	private String menuElement;

	StringWriter result = new StringWriter();

	@Override
	public void doTag() throws JspException, IOException {
		Object obj = getJspContext().findAttribute(menuElement);
		if (obj == null || !(obj instanceof MenuElement))
			throw new JspException("Cannot find MenuRoot with name: " + menuElement);

		MenuElement bar = (MenuElement) obj;

		addMenuElement(bar);

		getJspContext().getOut().print(result.getBuffer());

	}

	private void addMenuElement(MenuElement menuElement) {
		if (menuElement.getText() == null || menuElement.getHref() == null) {
			result.write("<table class=\"mtwClearFix\"><tr><td>\n");
			result.write("\n<ul id=\"mtwMenu\" class=\"MM\">");
		}

		for (MenuElement mE : menuElement.getChilds()) {

			result.write("\n\t<li>");

			 result.write("\n\t\t<a href=\"" + mE.getHref() + "\"");
	 			
			 if(mE.getTarget() != null) {
			 	result.write(" target=\""+mE.getTarget()+"\"");
			 }
			 			
			 result.write("> " + mE.getText() + " </a>\n");

			if (mE.getChilds() != null && mE.getChilds().size() > 0) {
				result.write("\t<ul>");
				addMenuElement(mE);
				result.write("\t</ul>");
			} else {
				result.write("\t</li>");
			}

		}

		if (menuElement.getText() == null || menuElement.getHref() == null) {
			result.write("\n</ul>\n");
			result.write("</td></tr></table>\n\n");
		}

	}

	public void setMenuElement(String menuRoot) {
		this.menuElement = menuRoot;
	}
}
