package org.mentawai.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

public class IOUtils {
	public static void writeFile(final URL fromURL, final File toFile)
	throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(fromURL.openStream());
			out = new BufferedOutputStream(new FileOutputStream(toFile));
			int len;
			byte[] buffer = new byte[4096];
			while ((len = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} finally {
			in.close();
			out.close();
		}
	}
	public static void writeFile(final URL fromURL, final String toFile)
			throws IOException {
		writeFile(fromURL, new File(toFile));
	}
	
	public static String readFile(String file) throws IOException {
		return readFile(file, null);
	}
	
	public static String readFile(String file, String charset) throws IOException {
		
		File f = new File(file);
		
		if (!f.exists()) return null;
		
		FileInputStream fis = null;
		BufferedReader br = null;
		
		try {
			
			fis = new FileInputStream(f);
			
			if (charset != null) {
			
				br = new BufferedReader(new InputStreamReader(fis, charset));
				
			} else {
				
				br = new BufferedReader(new InputStreamReader(fis));
			}
			
			StringBuilder sb = new StringBuilder(1024);
			
			String line;
			
			while((line = br.readLine()) != null) {
				
				sb.append(line).append('\n');
			}
			
			return sb.toString();

		} finally {
			
			br.close();
			
		}
	}

	public static void createDir(String dirName) throws IOException {
		File f = new File(dirName);
		if (f.exists()) {
			if (!f.isDirectory())
				throw new IOException(
						"The directory already exists as an file.");
		} else {
			f.mkdirs();
		}
	}
	
	public static String readURL(String url, Map<String, Object> params) throws Throwable {
		
		return readURL(url, params, null);
	}
	
	public static String readURL(String url, Map<String, Object> params, String charset) throws Throwable {
		
		String fullUrl = url;
		
		if (params != null) {
		
			fullUrl = HttpUtils.convertToQueryString(params, url, "?");
		}
			
		
		return readURL(fullUrl, charset);
	}
	
	public static String readURL(String url) throws Throwable {
		return readURL(url, (String) null);
	}
	
	public static String readURL(String url, String charset) throws Throwable {
		
		URL u = new URL(url);
		
		BufferedReader in;
		
		if (charset != null) {
		
			in = new BufferedReader(new InputStreamReader(u.openStream(), charset));
			
		} else {
			
			in = new BufferedReader(new InputStreamReader(u.openStream()));
		}
		
		StringBuilder sb = new StringBuilder(1024);
		
		String line;
		
		while((line = in.readLine()) != null) {
			sb.append(line);
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
