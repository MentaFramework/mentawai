package org.mentawai.tag.html;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class Form extends HTMLTag {
	
	public static String DEFAULT_ID = "form";
	public static String DEFAULT_NAME = "form";
	public static String DEFAULT_METHOD = "post";
	
	private String id = null;
	private String name = null;
	private String klass = null;
	private String style = null;
	private String act = null;
	private String method = null;
	private String enctype = null;
	
	public void setId(String id) { this.id = id; }
	public void setName(String name) { this.name = name; }
	public void setKlass(String klass) { this.klass = klass; }
	public void setStyle(String style) { this.style = style; }
	public void setAction(String act) { this.act = act; }
	public void setMethod(String method) { this.method = method; }
	public void setEnctype(String enctype) { this.enctype = enctype; }
	
	public String getId() {
		
		return id != null ? id : DEFAULT_ID;
	}
	
	public String getName() {
		
		return name != null ? name : DEFAULT_NAME;
	}
	
	public String getAction() {
		
		return act;
	}
	
	public String getMethod() {
		
		return method != null ? method : DEFAULT_METHOD;
	}
	
	public String getStringToPrint() throws JspException {
		
		StringBuilder sb = new StringBuilder(512);
		
		sb.append("<form");
		
		if (id != null) {
			
			sb.append(" id=\"").append(id).append("\"");
			
		} else {
			
			sb.append(" id=\"").append(DEFAULT_ID).append("\"");
		}
		
		if (name != null) {
			
			sb.append(" name=\"").append(name).append("\"");
			
		} else {
			
			sb.append(" name=\"").append(DEFAULT_NAME).append("\"");
		}
		
		if (klass != null) {
			
			sb.append(" class=\"").append(klass).append("\"");
		}
		
		if (style != null) {
			
			sb.append(" style=\"").append(style).append("\"");
		}
		
		if (act != null) {
			
			if( !act.startsWith("/") ) act = "/" + act;		// inserted by robert gil
			
			sb.append(" action=\"").append(req.getContextPath()).append(act).append("\"");
		}
		
		if (method != null) {
			
			sb.append(" method=\"").append(method).append("\"");
			
		} else {
			
			sb.append(" method=\"").append(DEFAULT_METHOD).append("\"");
		}
		
		if (enctype != null) {
			
			sb.append(" enctype=\"").append(enctype).append("\"");
		}
		
		// inserted by robert
		sb.append( getExtraAttributes() );
		
		sb.append(">");
		
		BodyContent bc = getBodyContent();
		
		if (bc != null) {
			
			sb.append(bc.getString());
		}
		
		sb.append("</form>");
		
		return sb.toString();
		
	}
	
	
	
}