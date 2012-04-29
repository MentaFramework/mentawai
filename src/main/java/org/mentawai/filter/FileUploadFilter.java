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
package org.mentawai.filter;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.mentawai.core.Action;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Context;
import org.mentawai.core.FileUpload;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.SessionContext;

/**
 * A filter for handling File Upload that uses the Jakarta Commons FileUpload library.
 *
 * An <i>org.apache.commons.fileupload.FileItem</i> will be placed in the action input.
 *
 * @author Sergio Oliveira Jr.
 */
public class FileUploadFilter implements Filter {

	private final DiskFileItemFactory factory;
	private int maxSizeToThrowError = 0;

	public FileUploadFilter() {
		this.factory = new DiskFileItemFactory();
	}

	public FileUploadFilter(int maxInMemorySize) {
		this(maxInMemorySize, 0, null);
	}


	public FileUploadFilter(int maxInMemorySize, int maxSizeToThrowError) {
		this(maxInMemorySize, maxSizeToThrowError, null);
	}
	
	public FileUploadFilter(int maxInMemorySize, String tempDirInsideWebInf) {
		this(maxInMemorySize, 0, tempDirInsideWebInf);
	}

	public FileUploadFilter(int maxInMemorySize, int maxSizeToThrowError, String tempDirInsideWebInf) {

		this.factory = new DiskFileItemFactory();

		factory.setSizeThreshold(maxInMemorySize);

		if (tempDirInsideWebInf != null) {

			StringBuilder sb = new StringBuilder(ApplicationManager.getRealPath());
			sb.append(File.separator).append("WEB-INF").append(File.separator);
			sb.append(tempDirInsideWebInf);

			File file = new File(sb.toString());

			if (!file.exists()) {

				if (!file.mkdir()) {

					throw new IllegalStateException("Cannot create directory: " + file);
				}
			}

			factory.setRepository(file);
		}

		this.maxSizeToThrowError = maxSizeToThrowError;
	}

	/**
	 * Because we are using the FileUpload from Jakarta Commons, and because it
	 * requires the HttpServletRequest, there is nothing we can do about it
	 * besides fetching the HttpServletRequest and giving it to the FileUpload
	 * class.
	 *
	 * @return The HttpServletRequest of the current request!
	 */
	protected HttpServletRequest getRequest(Action action) {

		Context session = action.getSession();

		if (!(session instanceof SessionContext))
			return null;

		return ((SessionContext) action.getSession()).getRequest();

	}

	public String filter(InvocationChain chain) throws Exception {

		Action action = chain.getAction();

		Input input = action.getInput();

		HttpServletRequest req = getRequest(action);

		if (req == null) {

			throw new FilterException("I was not possible to fetch HttpServletRequest inside FileUploadFilter!!!");
		}

		try {

			if (ServletFileUpload.isMultipartContent(req)) {

				ServletFileUpload upload = new ServletFileUpload(factory);

				if (maxSizeToThrowError > 0) upload.setSizeMax(maxSizeToThrowError);

				List<FileItem> items = upload.parseRequest(req);

				Iterator<FileItem> iter = items.iterator();

				while(iter.hasNext()) {

					FileItem item = iter.next();

					String name = item.getFieldName();

					if (item.isFormField()) {

						String value = item.getString();

						Object currValue = input.getValue(name);

						if (currValue == null) {

							// adding for the first time... just add...

							input.setValue(name, value);

						} else if (currValue instanceof String) {

							// we already have a String, so add as an array...

							String s = (String) currValue;

							String[] array = new String[2];

							array[0] = s;
							array[1] = value;

							input.setValue(name, array);

						} else if (currValue instanceof String[]) {

							String[] s = (String[]) currValue;

							String[] array = new String[s.length + 1];

							System.arraycopy(s, 0, array, 0, s.length);

							array[array.length - 1] = value;

							input.setValue(name, array);

						} else {
							throw new FilterException("Error trying to add a field value: " + name);
						}

					} else {

						if (item.getSize() > 0) {

							input.setValue(name, new FileUpload(item));

						} else {

							input.removeValue(name); // probably not necessary...
						}
					}
				}
			}
		} catch (FileUploadException e) {
			throw new FilterException(e);
		}

		return chain.invoke();
	}

	public void destroy() {

	}


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("FileUploadFilter: Class=").append(this.getClass().getName());
        return sb.toString();
    }
}
