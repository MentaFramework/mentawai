package org.mentawai.rmi;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RmiClient {
	
	private final String host;
	
	public RmiClient(String host) {
		if (host.endsWith("/")) host = host.substring(0, host.length() - 1);
		this.host = host;
	}
	
	protected static Object decode(String hex) throws IOException {
		
		if (hex.equals("null")) return null;
		
		byte[] data = fromHexToBytes(hex);
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String encode(Object o) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		
		byte[] array = baos.toByteArray();
		
		return fromBytesToHex(array);
	}
	
	public <E> E call(String url, Object ... params) throws IOException {
		
		StringBuilder queryString = new StringBuilder(1024);

		for(int i = 0; i < params.length; i++) {
			
			queryString.append("p").append(i).append("=");
			
			queryString.append(encode(params[i])).append("&");
		}
		
		String qs = queryString.toString();
		
		if (qs.length() > 0) qs = qs.substring(0, qs.length() - 1);
		
		String urlParameters  = qs;
		byte[] postData       = urlParameters.getBytes();
		int    postDataLength = postData.length;
		
		if (!url.startsWith("/")) url = "/" + url;
		
		URL u = new URL(host + url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();           
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty("charset", "utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		conn.setUseCaches(false);
		conn.getOutputStream().write(postData);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));		
		String result = reader.readLine();
		reader.close();
		
		if (result == null) throw new IOException("Could not read result from server!");
		
		if (result.equals("null")) return null;
		
		byte[] data = fromHexToBytes(result);
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		try {
			return (E) ois.readObject();
		} catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
    private static byte toByte(char hex) {
    	
    	switch(hex) {
    	
	    	case '0':
	    		return 0;
	    	case '1':
	    		return 1;
	    	case '2':
	    		return 2;
	    	case '3':
	    		return 3;
	    	case '4':
	    		return 4;
	    	case '5':
	    		return 5;
	    	case '6':
	    		return 6;
	    	case '7':
	    		return 7;
	    	case '8':
	    		return 8;
	    	case '9':
	    		return 9;
	    	case 'A':
	    	case 'a':
	    		return 10;
	    	case 'B':
	    	case 'b':
	    		return 11;
	    	case 'C':
	    	case 'c':
	    		return 12;
	    	case 'D':
	    	case 'd':
	    		return 13;
	    	case 'E':
	    	case 'e':
	    		return 14;
	    	case 'F':
	    	case 'f':
	    		return 15;
	    		
			default:
				throw new IllegalArgumentException("Not a valid hex digit: " + hex);
    	}
    }
    
    public static String fromBytesToHex(byte[] b) {
    	
    	StringBuilder sb = new StringBuilder(b.length * 2);
    	
    	for (int i=0; i < b.length; i++) {
    		sb.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ) );
    	}
    	
    	return sb.toString();
    }
    
    public static byte[] fromHexToBytes(String hex) {
    	
    	int x = hex.length() / 2;
    	
    	byte[] b = new byte[x];
    	
    	int index = 0;
    	
    	for(int i=0;i<hex.length();i+=2) {
    		
    		byte b1 = toByte(hex.charAt(i));
    		
    		byte b2 = toByte(hex.charAt(i + 1));
    		
    		b[index++] = (byte) ((b1 << 4) + b2);
    	}
    	
    	return b;
    }
    
    public static String fromHexToString(String hex) {
    	return new String(fromHexToBytes(hex));
    }
    
    public static String fromStringToHex(String s) {
    	return fromBytesToHex(s.getBytes());
    }
}