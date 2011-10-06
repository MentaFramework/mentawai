package org.mentawai.tag.html.ajax;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import net.sf.json.JSONObject;

import org.mentawai.tag.Out;
import org.mentawai.tag.html.Select;
import org.mentawai.tag.util.Context;
import org.mentawai.tag.util.PrintTag;

public class InPlaceTag extends SimpleTagSupport {

	public static final byte INPUTMONEY	=	1;
	public static final byte INPUTDATE	=	2;
	public static final byte INPUTMASK	=	3;

	public static final String TEXT	=	"text";
	public static final String SELECT	=	"select";


	private String name;
	private String value = "";
	private String url;
	private String extra;
	private String separator = "#";
	private JSONObject settings = new JSONObject();
	private int hashCode = super.hashCode();


	@Override
	public void doTag() throws JspException, IOException {


		// Try to find value on tag body
		// Body has preference.
		if(getJspBody() != null && !getJspBody().equals("")) {
			StringWriter sw = new StringWriter();
			getJspBody().invoke(sw);

			if(!sw.toString().trim().equals("")){
				this.value = sw.toString().trim();
			}
		}

		// Try to find value on context
		if(!value.equals("")) {

			JspTag parent = findAncestorWithClass(this, Context.class);
			if(parent != null){

				// Procura o objeto direto "anything"
				Context ctx = (Context) parent;
		        Object obj = ctx.getObject();
		         if (obj != null) {
		            Object object = Out.getValue(obj, value, false);
		            if(object != null){
		            	this.value = object.toString();

		            } else {

		            	// Procura como EL "prefix.anything"
		            	Object valueOnContext = PrintTag.getValue(value, (PageContext) getJspContext(), false);
		    			if(valueOnContext != null){
		    				this.value = valueOnContext.toString();
		    			}

		           }
				}
			}
		}

		StringBuffer sb = new StringBuffer();

		sb.append(String.format("\n	<span id=\"%s\" %s>%s</span>", this.hashCode, getExtraAttributes(), this.value));

		sb.append(String.format("\n	<script type=\"text/javascript\">"							));
		sb.append(String.format("\n\t	implace(\"%s\", \"%s\", \"%s\" %s);"			, this.hashCode, this.name, this.url, getOptions()));
		sb.append(String.format("\n	</script>"													));

		getJspContext().getOut().print(sb.toString());

		super.doTag();
	}


    /**
     * Return a String of settings separated by ","
     *
     * @return String
     */
	private String getOptions(){

		if(!settings.isEmpty()){
			return ", ".concat(settings.toString());
		}

		return "";
	}

    /**
     * Set a extras atributtes
     *
     * @return String
     */
    protected String getExtraAttributes() {

        if (extra == null) return "";

        StringBuffer sb = new StringBuffer(512);

        String[] s = extra.split("\\" + separator);

        for(int i=0;i<s.length;i++) {

            String[] ss = s[i].split("=");

            if (ss.length != 2) continue;

            sb.append(" ").append(ss[0].trim()).append("=\"").append(ss[1].trim()).append('"');

        }

        return sb.toString();
    }


	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setIndicator(String indicator) {
		settings.put("indicator", indicator);
	}

	public void setKlass(String klass) {
		settings.put("cssclass", klass);
	}

	public void setStyle(String style) {
		settings.put("style", style);
	}

	public void setWidth(String width) {
		settings.put("width", width);
	}

	public void setPlaceHolder(String p){
		settings.put("placeHolder", p);
	}

	public void setEvent(String e){
		settings.put("eventDefault", e);
	}

	public void setSize(short size){
		settings.put("size", size);
	}

	public void setMaxlength(String maxl){
		settings.put("maxlength", maxl);
	}

	public void setOnValueClean(String valueToClean) {
		settings.put("onValueClean", valueToClean);
	}
	
	/**
	 * This setter is invoked by inner tags.
	 *
	 * @param maskerOptions
	 */
	public void setMaskerOptions(JSONObject maskerOptions) {
		this.settings.put("maskerOptions", maskerOptions);
	}

	/**
	 * This setter is invoked by inner tags
	 * @param type
	 */
	public void setTypeMask(byte type){

		if(type == INPUTMONEY){
			this.settings.put("typeMask", "money");
		} else

		if(type == INPUTMASK){
			this.settings.put("typeMask", "custom");
		} else

		if(type == INPUTDATE){
			this.settings.put("typeMask", "date");
		}

	}

	/**
	 * This method will be acessed by inner tag select
	 */
	public void setSelectOptions(JSONObject json){
		this.settings.put("selectOptions", json );
	}

	/**
	 * This method will be acessed by inner tag select
	 */
	public void setMultiple(boolean multiple){
		this.settings.put("multiple", true);
	}

	/**
	 * This setter is invoked by inner tags.
	 */
	public void setType(String type){
		this.settings.put("type", type);
	}
	
}