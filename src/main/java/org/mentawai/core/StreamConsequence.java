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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A web-based consequence that gets a ready-to-use input stream
 * from the output of the action, get the contents from that input
 * stream and flush them to the servlet output stream.
 * Very useful for loading images from a database, downloads and 
 * other resources that the programmer may want to protect from
 * direct access from the client.
 * This class is <i>thread-safe</i>.
 *
 * @author Rubem Azenha
 * @author Sergio Oliveira Jr.
 */
public class StreamConsequence implements Consequence {
    
    /** The default key ("<i>stream</i>") to look for in the action output. */
    public static final String STREAM = "stream";
    
    /** The default key ("<i>contentDisposition</i>") to look for in the action output. */
    public static final String CONTENT_DISPOSITION = "contentDisposition";
    
    /** The default key ("<i>contentLength</i>") to look for in the action output. */
    public static final String CONTENT_LENGTH = "contentLength";
    
    /** The default key ("<i>contentType</i>") to look for in the action output. */
    public static final String CONTENT_TYPE = "contentType";
    
    /** Ogg, a multimedia bitstream container format. Defined in RFC 5334. */
    public static final String APPLICATION_OGG = "application/ogg";
    
    /** Portable Document Format. Defined in RFC 3778 5334. */
    public static final String APPLICATION_PDF = "application/pdf";
    
    /** SOAP. Defined by RFC 3902. */
    public static final String APPLICATION_SOAP = "application/soap+xml";
    
    /** MP3 or other MPEG audio. Defined in RFC 3003. */
    public static final String AUDIO_MPEG = "audio/mpeg";
    
    /** Ogg Vorbis, Speex, Flac and other audio. Defined in RFC 5334. */
    public static final String AUDIO_OGG = "audio/ogg";
    
    /** GIF image. Defined in RFC 2045 and RFC 2046. */
    public static final String IMAGE_GIF = "image/gif";
    
    /** JPEG JFIF image. Defined in RFC 2045 and RFC 2046. */
    public static final String IMAGE_JPEG = "image/jpeg";
    
    /** Portable Network Graphics. Defined in RFC 2083. */
    public static final String IMAGE_PNG = "image/png";
    
    
    
    public static final String FILENAME = "filename";

	// The content-type of the message
	private String contentType;
    
    // The key with which to get the stream from the output...
    private String keyForStream = STREAM;
    
    // The key with which to get the content disposition from the output...
    private String keyForContentDisposition = CONTENT_DISPOSITION;
    
    // The key with which to get the content length from the output...
    private String keyForContentLength = CONTENT_LENGTH;
    
    private String keyForContentType = CONTENT_TYPE;
    
    private String keyForFilename = FILENAME;
    
    /**
     * Creates a new instance of the StreamConsequence class.
     * The content type and everything else will be dynamic, in
     * other words, defined by the action at runtime.
     */
    public StreamConsequence() {
    	
    }
    
	/**
	 * Creates a new instance of the StreamConsequence class,
	 * with the given content-type
     *
     * @param contentType The content type to set in the servlet response.
	 */
	public StreamConsequence(String contentType) {
	    this.contentType = contentType;
    }
    
	public StreamConsequence keyForFilename(String key) {
		
		this.keyForFilename = key;
		
		return this;
	}
	
	public StreamConsequence keyForContentType(String key) {
		this.keyForContentType = key;
		return this;
	}

	public StreamConsequence keyForContentDisposition(String key) {
		
		this.keyForContentDisposition = key;
		
		return this;
	}
	
	public StreamConsequence keyForContentLength(String key) {
		
		this.keyForContentLength = key;
		
		return this;
	}
	
	public StreamConsequence keyForStream(String key) {
		
		this.keyForStream = key;
		
		return this;
	}
	
	/**
	 * Executes the action, reading from the input stream the data that
	 * has to be flushed into the servlet output stream.
	 */
	public void execute(Action a, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		
		Output output = a.getOutput();

		Object obj = null;
		
		if (keyForStream != null && output.has(keyForStream)) {
			
			obj = output.getValue(keyForStream);
			
		} else {
			
			Iterator<String> iter = output.keys();
			
			while(iter.hasNext()) {
				
				String key = iter.next();
				
				Object value = output.getValue(key);
				
				if (value instanceof InputStream || value instanceof byte[]) {
					
					obj = value;
					
					break;
				}
			}
		}
		
		// be smart and set content length if not provided by action...
		
		if (obj instanceof byte[]) {
			
			if (!output.has(keyForContentLength)) {
				
				byte[] array = (byte[]) obj;
				
				output.setValue(keyForContentLength, array.length);
			}
			
		}
		
		defineHeader(res, output);
		
		OutputStream outputStream = null;
		
		try {
			outputStream = res.getOutputStream();
		} catch (IOException e) {
			throw new ConsequenceException(e);
		}
        
        try {
            if (obj instanceof InputStream) {
            	
            	InputStream in = (InputStream) obj;
            	 byte[] buffer = new byte[4092];
            	 int size = 0;
            	 while((size = in.read(buffer, 0, buffer.length)) != -1) {
            		 
            	     outputStream.write(buffer, 0, size);
            	 }
            	 
                outputStream.flush();
                
            } else if (obj instanceof byte[]) {
                byte [] b = (byte []) obj;
                outputStream.write(b);
                outputStream.flush();
            } else {
                throw new ConsequenceException("Cannot find stream: " + keyForStream);
            }
        } catch(Exception e) {
            throw new ConsequenceException(e);
        }
    }
    
	protected void defineHeader(HttpServletResponse res, Output output) throws ConsequenceException {
		
		if (contentType == null) {
			
			Object o = output.getValue(keyForContentType);
			
			if (o == null || !(o instanceof String)) {
				throw new ConsequenceException("Content-type not defined!");
			}
			
			res.setContentType(o.toString());
			
		} else {
		
			res.setContentType(contentType);
			
		}
        
        Object contentLength = output.getValue(keyForContentLength);
        
        if (contentLength != null && contentLength instanceof Number) {
            res.setContentLength(((Number) contentLength).intValue());
        }
        
		Object contentDisposition = output.getValue(keyForContentDisposition);
		
		Object filename = output.getValue(keyForFilename);
		
		if (filename != null) {
			
			res.setHeader("Content-Disposition", "inline;filename=" + filename);
			
		} else if (contentDisposition == null) {
			
            res.setHeader("Content-Disposition", "inline");
            
        } else {
        	
            res.setHeader("Content-Disposition", contentDisposition.toString());
        }
	}    
}