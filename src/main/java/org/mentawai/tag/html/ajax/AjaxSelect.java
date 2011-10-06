package org.mentawai.tag.html.ajax;

import javax.servlet.jsp.JspException;

import org.mentawai.i18n.I18N;
import org.mentawai.tag.html.dyntag.BaseTag;

/**
 * This class create de javascript and
 * the <select> html tag
 * 
 * @author Robert Willian Gil
 *
 */


public class AjaxSelect extends BaseTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sourceValue = null;

	private String url = null;

	private String functionName = null;

	private boolean emptyField = false;
    
	private String emptyFieldValue = null;
    
	private String defEmptyFieldValue = null;
	
	private String swapValue = null;
    
	private String keyValue = null;
	
	private boolean useLoadingMessage = true;
	
	private StringBuffer buildTag() {
		
		String[] values = findValues(getName(), false, true);

		
		if ((getId() == null) || (getId().trim().equals("")))
			setId(getName());

		StringBuffer sb = new StringBuffer();

		sb.append("<select ");
		prepareAttribute(sb, "name", getName());
		prepareAttribute(sb, "id", getId());
		prepareAttribute(sb, "onchange", getOnchange());
		prepareAttribute(sb, "class", getKlass());
		prepareAttribute(sb, "style", getKlassStyle());
		sb.append(getExtraAttributes());
		sb.append(">\n");
		
		buildDefaultValue(sb);
		
		sb.append("</select>\n");
		
		
		sb.append("<script type=\"text/javascript\">");
		sb.append("\nfunction " + functionName + "() {\n");
		sb.append("\tvar r = new mtw.request();\n");
		sb.append("\tr.addParameter(\"sourceValue\", mtw.util.getValue(\""+ sourceValue +"\") );\n");
		sb.append("\tr.setUrl(\""+ url +"\");\n");
		sb.append("\tr.onSuccess(\n");
		sb.append("\t\tfunction (trans){\n");
		sb.append("\t\t\tvar rq = new mtw.response(trans);\n");
		sb.append("\t\t\tmtw.removeOptions(\""+ getId() +"\");\n");
		
		if(emptyField) {
			sb.append("\t\t\tmtw.addOptions(\""+ getId() +"\", rq, '"+ keyValue +"', '"+ swapValue +"');\n");
			
		} else {
			sb.append("\t\t\tmtw.addOptions(\""+ getId() +"\", rq);\n");
			
		}
		whenValidating(sb, values);
		sb.append("\t\t}\n");
		sb.append("\t);\n");
		
		if( useLoadingMessage )
			sb.append("\tr.useLoadingMessage();\n");
		
		sb.append("\tr.send();\n");
		sb.append("}\n");
		
		if(values != null && values.length > 0) {
			sb.append("Event.observe(window, 'load', function() {\n");
			sb.append("\t"+ functionName +"();\n");
			sb.append("});\n");
		}
		
		sb.append("</script>\n\n");

		return sb;
	}
	
	
	private void buildDefaultValue(StringBuffer sb){
		
		keyValue = defEmptyFieldValue;

		if (defEmptyFieldValue == null) {
			keyValue = "";
		}

		if (emptyField) {

	         if (emptyFieldValue != null) {

	        	String value = null;

	        	if (emptyFieldValue.length() >= 3 && emptyFieldValue.startsWith("!") && emptyFieldValue.endsWith("!")) {

	        		// try to get this the same way we are doing in the PrintI18N tag...

	                I18N [] props = (I18N []) pageContext.getAttribute("_i18n");
	                String prefix = (String) pageContext.getAttribute("_prefix");

	                if (props != null) {

	                	String key = emptyFieldValue.substring(1, emptyFieldValue.length() - 1);

	                	if (prefix != null) {

	                		String prefixKey = prefix + "." + key;

	                		for(int i=props.length-1;i>=0;i--) {
	                            if (props[i] == null) continue;
	                            if (props[i].hasKey(prefixKey)) {
	                                 value = props[i].get(prefixKey);
	                                 break;
	                            }
	                        }
	                	}

	                	if (value == null) {

	                		for(int i=props.length-1;i>=0;i--) {
	                            if (props[i] == null) continue;
	                            if (props[i].hasKey(key)) {
	                                value = props[i].get(key);
	                                break;
	                            }
	                        }
	                	}
	                }
	        	}

	        	if (value != null) {

	        		sb.append("<option value=\""+keyValue+"\">").append(value).append("</option>\n");
	        		swapValue = value;
	        		
	        	} else {

	        		sb.append("<option value=\""+keyValue+"\">").append(emptyFieldValue).append("</option>\n");
	        		swapValue = emptyFieldValue;
	        	}

	         } else {

	            sb.append("<option value=\""+keyValue+"\"> - </option>\n");
	            swapValue = " - ";
	         }
		}
	}
	
	
	private void whenValidating(StringBuffer sb, String[] values){
		if(values != null && values.length > 0) {
			sb.append("\t\t\tvar arr = new Array();\n");
			for(int i = 0; i < values.length; i++){
				sb.append("\t\t\tarr["+ i +"] = \""+ values[i].toString() + "\";\n");
			}
			sb.append("\t\t\tmtw.util.setValue(\""+ getId() +"\", arr);\n");
		}
	}

	@Override
	public String getStringToPrint() throws JspException {
		if(sourceValue == null)
			throw new JspException("sourceValue cannot be null!");
		StringBuffer result = new StringBuffer();
		result.append(buildTag().toString());
		setId(null);
		return result.toString();
	}

	protected String prepareEventHandlers() {
		StringBuffer handlers = new StringBuffer();
		prepareMouseEvents(handlers);
		prepareKeyEvents(handlers);
		prepareTextEvents(handlers);
		prepareFocusEvents(handlers);
		return handlers.toString();
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public void setSourceValue(String source) {
		this.sourceValue = source;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setEmptyField(boolean emptyField) {
		this.emptyField = emptyField;
	}

	public void setEmptyFieldValue(String emptyFieldValue) {
		this.emptyFieldValue = emptyFieldValue;
	}

	public void setDefEmptyFieldValue(String defEmptyFieldValue) {
		this.defEmptyFieldValue = defEmptyFieldValue;
	}


	public void setUseLoadingMessage(boolean useLoadingMessage) {
		this.useLoadingMessage = useLoadingMessage;
	}

}
