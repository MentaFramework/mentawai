package org.mentawai.tag.html.ajax;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.html.dyntag.BaseTag;

@SuppressWarnings("serial")
public class AjaxCompleter extends BaseTag {

	private String url;
	private String source;
	private String target;
	private String onSuccess;
	private String minChars = "1";
	
	
	
	@Override
	public String getStringToPrint() throws JspException {
		
		StringBuffer sb = new StringBuffer();
		String hc = String.valueOf( super.hashCode() );
		sb.append("<script type=\"text/javascript\">\n");
		sb.append("\t var ac"+ hc +" = new mtw.autoCompleter();\n");
		sb.append("\t\t ac"+ hc +".setUrl(\""+ req.getContextPath() +"/"+ url +"\");\n");
		sb.append("\t\t ac"+ hc +".setSource(\""+ source +"\");\n");
		sb.append("\t\t ac"+ hc +".setTarget(\""+ target +"\");\n");
		sb.append("\t\t ac"+ hc +".setMinChars(\""+ minChars +"\");\n");
		sb.append("\t\t ac"+ hc +".onSuccess("+ onSuccess +");\n");
		sb.append("\t\t ac"+ hc +".start();\n");
		sb.append("\t</script>\n");
		
		return sb.toString();
	}



	public void setUrl(String url) {
		this.url = url;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setOnSuccess(String onSuccess) {
		this.onSuccess = onSuccess;
	}

	public void setMinChars(String minChars) {
		this.minChars = minChars;
	}
	
}
