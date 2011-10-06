package org.mentawai.ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentawai.core.Action;
import org.mentawai.core.Consequence;
import org.mentawai.core.ConsequenceException;

public class AjaxRedirectConsequence implements Consequence {

	private String path;
	
	public AjaxRedirectConsequence(String path) {
		this.path = path;
	}
	
	@Override
	public void execute(Action a, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {

		StringBuilder sb = new StringBuilder(64);

        if (path.indexOf("://") > 0) {

           // absolute URL: there is no need to add the context path...

           sb.append(path);

        } else if (path.startsWith("//")) {

           // url relative to the ROOT of the web server...

           sb.append(path.substring(1, path.length()));

        } else {

           // url relative to the context path...

           sb.append(req.getContextPath());

           if (!path.startsWith("/")) {
              
              // we do not support request-related redirect... 99.9999% of the cases this is not needed...
              // (you can implement your own consequence if you want this...)

              sb.append("/");
           }

           sb.append(path);
        }
        
        res.setContentType( AjaxRenderer.APP_JS );
        
        try {
        	
        	String fn = "function(){window.location='"+ sb.toString() +"'}";
        	
        	PrintWriter writer = res.getWriter();
        	writer.print( fn );
        	writer.flush();
        	
        } catch (IOException e) {
            throw new ConsequenceException(
                    "Exception while writing the renderized document: "
                            + e.getMessage(), e);
        }
		
	}

}
