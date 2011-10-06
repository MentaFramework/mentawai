package org.mentawai.tag.html;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.i18n.I18N;
import org.mentawai.i18n.LocaleManager;

public class Submit extends HTMLTag {
	
	public static String DEFAULT_TYPE = "button";
	
	private String id = null;
	private String name = null;
	private String klass = null;
	private String style = null;
	private String act = null;
	private String method = null;
	private String value = null;
	private String src = null;
	private String type = null;
	
	public void setId(String id) { this.id = id; }
	public void setName(String name) { this.name = name; }
	public void setKlass(String klass) { this.klass = klass; }
	public void setStyle(String style) { this.style = style; }
	public void setAction(String act) { this.act = act; }
	public void setMethod(String method) { this.method = method; }
	public void setValue(String value) { this.value = value; }
	public void setType(String type) { this.type = type; }
	public void setSrc(String src) { this.src = src; }
	
	public String getStringToPrint() throws JspException {
		
		Tag parent = findAncestorWithClass(this, Form.class);
		
		if (parent == null) throw new JspException("Tag submit not enclosed by form!");
		
		Form form = (Form) parent;
		
		String formMethod = form.getMethod();
		
		String formAction = form.getAction();
		
		if (formAction == null && act == null) {
			
			throw new JspException("No action to submit!");
		}
		
		String submitAction = act != null ? act : formAction;
      
		if (formMethod == null && method == null) {
			
			throw new JspException("No method was specified!");
		}
		
		String submitMethod = method != null ? method : formMethod;
		
		String functionName = submitAction.replace('.', '_').replace('/', '_');

		
		StringBuilder sb = new StringBuilder(512);
		
		sb.append("<input");
		
		if (id != null) {
			
			sb.append(" id=\"").append(id).append("\"");
			
		}
		
		if (name != null) {
			
			sb.append(" name=\"").append(name).append("\"");
			
		}
		
		if (klass != null) {
			
			sb.append(" class=\"").append(klass).append("\"");
		}
		
		if (style != null) {
			
			sb.append(" style=\"").append(style).append("\"");
		}
		
		if (value != null) {
			
			if (value.startsWith("!") && value.endsWith("!") && value.length() > 2) {
				
				I18N[] props = (I18N[]) pageContext.getAttribute("_i18n");
				String prefix = (String) pageContext.getAttribute("_prefix");
				
				String key = value.substring(1, value.length() - 1);
				
				if (prefix != null) {
					key = prefix + "." + key;
				}
				
				String newValue = new String(value);
				
				if (props != null) for(int i=props.length-1;i>=0;i--) {
		        	
		            if (props[i] == null) continue;
		            
		            if (props[i].hasKey(key)) {
		            	
		            	String v = props[i].get(key);
		            	
		            	if (LocaleManager.I18N_DEBUG) {
		            		
		            		newValue = "{" + v + "}";
		            		
		            	} else {
		            	
		            		newValue = v;
		            		
		            	}
		            }
		        }
				
				sb.append(" value=\"").append(newValue).append("\"");
				
			} else {
			
				sb.append(" value=\"").append(value).append("\"");
				
			}
		}
		
		if (type != null) {
		
			sb.append(" type=\"").append(type).append("\"");
			
		} else {
			
			sb.append(" type=\"").append(DEFAULT_TYPE).append("\"");
		}
		
		if (src != null) {
			
			sb.append(" src=\"").append(src).append("\"");
		}
		
		sb.append(" onclick=\"return submit_").append(functionName).append("(this.form);\"");
		
		sb.append(">\n");
		
		sb.append("<script type=\"text/javascript\" language=\"JavaScript\">\n");
		
		sb.append("function submit_").append(functionName).append("(form) {\n");
      
      if (!submitAction.startsWith("/")) {
         
         submitAction = "/" + submitAction;
         
      }
		
		sb.append("\tform.action = \"").append(req.getContextPath()).append(submitAction).append("\";\n");
		
		sb.append("\tform.method = \"").append(submitMethod).append("\";\n");
		
		sb.append("\tform.submit();\n");
		
		sb.append("\treturn false;\n");
		
		sb.append("}\n");
		
		sb.append("</script>\n");
		
		return sb.toString();
		
	}


}