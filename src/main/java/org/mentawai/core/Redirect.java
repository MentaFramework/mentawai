/*
 * Mentawai Web Framework http://mentawai.lohis.com.br/
 * Copyright (C) 2005  Sergio Oliveira Jr. (sergio.oliveira.jr@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.mentawai.core;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentawai.util.ActionUtils;

/**
 * A redirect consequence that has the following features:
 * 
 * <ol>
 * <li>Paths starting with "//" are relative to the webserver root.
 * <li>Paths containing "://" are absolute paths.
 * <li>All other paths (starting with "/" or not) are related to the context path.
 * <li>Supports dynamic URLs and dynamic parameters, with the dynamic values will come from action output.
 * <li>Dynamic parameters will be encoded. Static parameters (hardcoded in the URL) are assumed to be encoded.
 * </ol>
 * 
 * @author Sergio Oliveira Jr.
 * @author Alan Burlison Alan.Burlison@sun.com
 *
 */
public class Redirect implements Consequence {

   /** The parameter's name in the output containing the URL to redirect to. */
   public static final String REDIRURL_PARAM = "_mentawai_redirect_url";

   /** The target URL of the redirect. */
   private String url = null;

   /** True if the {@link Output} values are to be appended to the URL. */
   private boolean appendOutput = false;

   /** True if the URL is to be obtained from the {@link Output}. */
   private boolean dynamicUrl = false;

   /**
    * Creates a normal redirect for the given url. The supplied URL fully
    * specifies the target of the redirect.
    * 
    * @param url
    *           is the url for this redirect.
    */
   public Redirect(String url) {
      this.url = url;
   }
   
   public Redirect(ActionConfig ac) {
	   this(ActionUtils.getUrlFrom(ac));
   }

   /**
    * Creates a redirect for the given url with dynamic parameters. The URL
    * query parameters are obtained from the {@link Output} object for the
    * {@link Action}. All the parameters are appended to the URL query string.
    * 
    * @param url
    *           is the url for this redirect.
    * @param appendOutput
    *           is true if this redirect is to use dynamic parameters.
    */
   public Redirect(String url, boolean appendOutput) {
      this.url = url;
      this.appendOutput = appendOutput;
   }
   
   public Redirect(ActionConfig ac, boolean appendOutput) {
	   this(ActionUtils.getUrlFrom(ac), appendOutput);
   }

   /**
    * Creates a redirect with a dynamic url.
    * 
    * The redirect url is obtained from the action output through the key
    * REDIRURL_PARAM.
    * 
    */
   public Redirect() {
      this.dynamicUrl = true;
   }

   /**
    * Creates a redirect with a dynamic URL and dynamic parameters.
    * 
    * The redirect url is obtained from the action output.
    * 
    * The URL query parameters are obtained from the {@link Output} object for
    * the {@link Action}. All the parameters are appended to the URL query
    * string.
    * 
    * @param appendOutput
    *           is true if this redirect is to use dynamic parameters.
    */
   public Redirect(boolean appendOutput) {
      this.dynamicUrl = true;
      this.appendOutput = appendOutput;
   }

   /**
    * Execute the redirect consequence. The URL to redirect to is built up as
    * necessary, depending on the type of redirection that is required - see the
    * constructirs for this class for the various flavours of redirections that
    * are supported.
    * 
    * @param act
    *           action to be redirected.
    * @param req
    *           request object.
    * @param res
    *           response object
    * @throws ConsequenceException
    *            if an error is detected during construction of the redirection
    *            URL.
    */
   public void execute(Action act, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
      try {

         Output output = act != null ? act.getOutput() : null;

         String path;

         if (dynamicUrl && output != null) {
            
            // URL should be taken from action output (dynamic redirect)

            path = (String) output.getValue(REDIRURL_PARAM);

         } else {
            
            // URL was passed in the constructor (static redirect)

            path = this.url;
         }

         if (path == null || path.length() == 0) {

            throw new ConsequenceException("Missing url for redirect!");
         }

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

         // Check whether the redirect already have a query string (some parameters)
         URI uri = new URI(path);
         String urlQueryString = uri.getQuery();

         // If we have dynamic parameters, append them to the query string.
         if (appendOutput && output != null) {

            StringBuilder queryString = new StringBuilder(64);

            // Build the parameters into a query string.
            Iterator<String> iter = output.keys();

            while (iter.hasNext()) {

               String key = (String) iter.next();

               // Skip system params and also flash params...
               if (key.startsWith("_")) {
                  continue;
               }

               Object value = output.getValue(key);

               // Skip null values.
               if (value == null)
                  continue;

               if (queryString.length() > 0) {

                  queryString.append('&');
               }

               queryString.append(key);
               queryString.append("=");
               queryString.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            
            if (queryString.length() > 0) {
               
               if (urlQueryString == null) {
                  
                  // no query string...
                  
                  sb.append('?').append(queryString);
                  
               } else if (urlQueryString.equals("")) {
                  
                  // empty query string ('?' is already there!)
                  
                  sb.append(queryString);
                  
               } else {
                  
                  // query string is already there, so append with '&'
                  
                  sb.append('&').append(queryString);
               }
            }
         }
         
         res.sendRedirect(sb.toString());
         
      } catch (Exception e) {
         
         throw new ConsequenceException(e);
         
      }
   }

   /**
    * Return a string representation of the redirect.
    * 
    * @return a string representation of the redirect.
    */
   public String toString() {
      
      StringBuilder s = new StringBuilder();
      
      s.append("Redirect to ");
      
      if (url != null) {
         
         s.append(url);
         
      } else {
         
         s.append("(dynamic url)");
         
      }
      
      if (appendOutput) {
         
         s.append(" (dynamic parameters)");
      }
      
      return s.toString();
   }
}