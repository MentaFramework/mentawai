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
package org.mentawai.ajax;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
/**
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * A utility class for manipulating DOM documents. This class provide services and is implemented to 
 * prevent code repetition. 
 *
 */
public final class DOMUtils {
	/**
	 * A XMLOutputter for printing the documents into an output. See <a href="http://www.jdom.org/docs/apidocs/org/jdom/output/XMLOutputter.html">JDOM Api docs</a>
	 * for more details on this object functions.
	 */
	private static final XMLOutputter outputter = new XMLOutputter();
	
	private static final XMLOutputter outputterPretty = new XMLOutputter(Format.getPrettyFormat());
	
    /**
     * Get a String representation of a DOM document.
     * @param document The document to represent
     * @return The String representation of the document
     */
	public static final String getDocumentAsString(Document document, boolean pretty) {
		
		StringWriter stringWriter = new StringWriter();
		
		try {
			
			writeDocument(document, stringWriter, pretty);
			
		} catch (IOException e) {
			
			throw new RuntimeException(e);
			
		}
		return stringWriter.toString();
	}
    /**
     * Write an given document to a output stream. 
     * @param document The document to be writed out.
     * @param outputStream The output stream that will be used to write the document.
     * @throws IOException If an I\O error ocours while using the output stream.
     */
	public static final void writeDocument(Document document, OutputStream outputStream, boolean pretty) throws IOException {
		
		if (pretty) {
		
			outputterPretty.output(document, outputStream);
			
		} else {
			
			outputter.output(document, outputStream);
		}
	}
    /**
     * Write an given document to a writer. 
     * @param document The document to be writed out.
     * @param writer The writer that will be used to write the document.
     * @throws IOException If an I\O error ocours while using the writer.
     */
	public static final void writeDocument(Document document, Writer writer, boolean pretty) throws IOException {
		
		if (pretty) {
		
			outputterPretty.output(document, writer);
			
		} else {
		
			outputter.output(document, writer);
			
		}
	}
}
