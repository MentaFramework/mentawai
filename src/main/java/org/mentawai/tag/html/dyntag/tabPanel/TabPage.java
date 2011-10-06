package org.mentawai.tag.html.dyntag.tabPanel;
import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *	@author Alex Fortuna
 *	@since 0.1
 */

public final class TabPage extends SimpleTagSupport {
		
	private String name,caption;
	private TabPanel tabContainer;
	private StringWriter out = new StringWriter();
	
			
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

	public void doTag() throws JspException, IOException {
		addToContainer();
		buldTag();
	}
	
	public TabPanel getTabContainer() {
		if (null == tabContainer) {
			tabContainer = (TabPanel)
					findAncestorWithClass(this,TabPanel.class);
		}
		return tabContainer;
	}
	
	private void addToContainer() {
		getTabContainer().addChild(this);
	}
	
	private void buldTag() throws JspException, IOException {					
		out.write("\t\t<div class=\"tab-page\" id=\""+getTabContainer().getName()+"_"+getName()+"\">");		
		out.write("\n\t\t\t<h2 class=\"tab\">"+getCaption()+"</h2>");				
		out.write("\n\t\t<script type=\"text/javascript\">");	
		out.write(getTabContainer().getName()+".addTabPage( document.getElementById( \""+getTabContainer().getName()+"_"+getName()+"\" ) );");
		out.write("</script>");
		getJspBody().invoke(out);		
		out.write("\n\t\t</div> ");
			getJspContext().getOut().print(out.getBuffer());
	}
	
}
