package org.mentawai.tag.html.ajax;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.html.dyntag.BaseConfig;
import org.mentawai.tag.html.dyntag.FileTransfer;

/**
 * 
 * @author Robert Willian Gil
 *
 */
public class AjaxConfig extends BaseConfig {

	private static final long serialVersionUID = 1L;
	public static final String MENTAAJAX_DEFAULT_CHARSET = "_MENTA_DEFAULT_CHARSET";
	
	private boolean loadPrototype = false;
	private String charset	=	"UTF-8";

	protected StringBuffer buildTag(){

		List<FileTransfer> listFiles = new LinkedList<FileTransfer>();
		
/*		if(AjaxListener.LIST_PATH_FILES == null){
			AjaxListener listener = new AjaxListener();
			listener.contextInitialized(Controller.getApplication());
		}*/

		StringBuffer serializeForm = null;
		
		if(loadPrototype){
			
			listFiles.add(new FileTransfer("css","auto_complete.css","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			listFiles.add(new FileTransfer("js","prototype.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			listFiles.add(new FileTransfer("js","mentaAjax.js?v=2.6.1","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			listFiles.add(new FileTransfer("js","util.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			listFiles.add(new FileTransfer("js","effects.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			listFiles.add(new FileTransfer("js","ac.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			listFiles.add(new FileTransfer("js","mentaInPlace.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			
			serializeForm = new StringBuffer();
			
			serializeForm.append("\n		<script type=\"text/javascript\">");
			serializeForm.append("\n\t			mtw.request.prototype.serializeForm = function(formId){");
			serializeForm.append("\n\t\t			this.PARS = $(formId).serialize();");
			serializeForm.append("\n\t			}");
			serializeForm.append("\n		</script>");
			 
		} else {
			
			listFiles.add(new FileTransfer("js","mentaAjax.js?v=2.6.1","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			listFiles.add(new FileTransfer("js","mentaInPlace.js","/org/mentawai/tag/html/ajax/lib/","/mtwAjax/"));
			
		}
		
		StringBuffer result = new StringBuffer();

		result.append(  "\n\n\t<!-- Default MentaAjax charset request -->\n\t");
		result.append(	"<script type=\"text/javascript\">");
		result.append(  String.format("\t	var _MENTA_DEFAULT_CHARSET = \"%s\"; \t"	, this.charset ) );
		result.append(	"</script>\n\n");
		
		result.append(buldImportJsFile(listFiles).toString());
		result.append(buldImportCssFile(listFiles).toString());
		
		if(loadPrototype)
			result.append(serializeForm.toString());
		
		return result;
	}
	
	
	@Override
	public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}


	public void setLoadPrototype(boolean loadPrototype) {
		this.loadPrototype = loadPrototype;
	}


	public void setCharset(String charset) {
		this.charset = Charset.availableCharsets().containsKey( charset ) ? charset : this.charset;
	}

	
	
}
