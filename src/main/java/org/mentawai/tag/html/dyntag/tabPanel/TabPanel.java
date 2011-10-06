
/**
 *	@author Alex Fortuna
 *	@version 0.1
 *	@since 0.1
 */
package org.mentawai.tag.html.dyntag.tabPanel;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
/**
 *	@author Alex Fortuna
 *	@since 0.1
 *	 
 */
public final class TabPanel extends SimpleTagSupport {
		
	private String name;
	private List<TabPage> children;	
	private StringWriter out = new StringWriter();
	
	List<TabPage> getChildren() {
		if (null == children) {
			children = new ArrayList<TabPage>();
		}
		return children;
	}
	
	int getChildCount() {
		return getChildren().size();
	}
	
	void addChild(final TabPage child) {
		getChildren().add(child);
	}
	
	public void doTag() throws JspException, IOException {		
		StringWriter evalResult    = new StringWriter();
		out.write("\t<div class=\"tab-pane\" id=\""+getName()+"\"");
		out.write(" >");
		out.write("\n\t<script type=\"text/javascript\">");					
		out.write("\n\t\t var "+getName()+" = new WebFXTabPane( document.getElementById( \""+ getName() +"\" ) )");
		out.write("\n\t</script>");		
		getJspBody().invoke(evalResult);
		evalResult.getBuffer().delete(0,evalResult.getBuffer().length());
		children = null;
		getJspBody().invoke(evalResult);
		//Mostra os dados das pags
		for (Iterator<TabPage> iter = children.iterator(); iter.hasNext();) {
			TabPage tabPane = iter.next();
			tabPane.getName();
		}		
		out.write(evalResult.getBuffer().toString());		
		out.write("\n\t</div>");	
		getJspContext().getOut().print(out.getBuffer());
		
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
}
