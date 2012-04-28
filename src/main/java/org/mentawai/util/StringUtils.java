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
package org.mentawai.util;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mentawai.i18n.LocaleManager;

public class StringUtils {
	
    private static final String[] CHARS = split("abcdefghijklmnopqrstuvxwyz");
	
	private static final String[] NUMBERS = split("0123456789");
	
	private static final Random RAND = new Random();
    
    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }
    
    /**
     * Checks if String is null or empty.
     * @param str String
     * @return boolean
     */
    public static boolean isEmpty(String str) {
    	return str == null || str.trim().length() == 0;
    }
    
    
    /**
     * Turns a line with strings separated by one
     * or more spaces into a String array.
     * 
     * @param line The line to be sliced
     * @return A String array with the slices
     */
    public static String[] slice(String line) {
    	return line.trim().split("\\s+");
    }
    
    public static String[] split(String line) {
    	String[] s = new String[line.length()];
    	
    	for(int i=0;i<line.length();i++) {
    		s[i] = String.valueOf(line.charAt(i));
    	}
    	return s;
    }
    
    public static String randomString(int size) {
    	
    	return randomString(size, false);
    }
    
    public static String randomString(int size, boolean onlyChars) {
    	
    	return randomString(size, onlyChars, false);
    }
    
    public static String randomNumber(int size) {
    	
    	int count = 0;
    	
    	StringBuilder sb = new StringBuilder(size);
    	
    	while(count < size) {
    		
    		int index = RAND.nextInt(NUMBERS.length);
    		
    		sb.append(NUMBERS[index]);
    		
    		count++;
    	}
    	
    	return sb.toString();
    }
    
    public static String randomString(int size, boolean onlyChars, boolean upcase) {
    	
    	int count = 0;
    	
    	StringBuilder sb = new StringBuilder(size);
    	
    	while(count < size) {
    		
    		int index = RAND.nextInt(CHARS.length);
    		
    		sb.append(CHARS[index]);
    		
    		count++;
    		
    		if (!onlyChars) {
    			
    			if (count >= size) break;
    		
	    		index = RAND.nextInt(NUMBERS.length);
	    		
	    		sb.append(NUMBERS[index]);
	    		
	    		count++;
    		}
    	}
    	
    	return upcase ? sb.toString().toUpperCase() : sb.toString();
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
    
    public static String invert(String s) {
    	StringBuilder sb = new StringBuilder(s.length());
    	
    	for(int i=s.length()-1;i>=0;i--) {
    		sb.append(s.charAt(i));
    	}
    	
    	return sb.toString();
    }
    
    /**
	 * Convert Money value as String to a Number
	 * @param value
	 * @param loc (Optional) Default is LocaleManager.DEFAULT_LOCALE
	 */
	public static Number parseCurrencyNumber(String value, Locale loc) throws ParseException {
		if(loc == null) loc = LocaleManager.DEFAULT_LOCALE;
		String symbol = DecimalFormatSymbols.getInstance(loc).getCurrencySymbol();
		if("R$".equals(symbol)) symbol += " "; // Caso seja a moeda Real.
		Number number = NumberFormat.getCurrencyInstance(loc).parse(symbol + value);
		return number;		
	}
	
	public static String[] REPLACES = { "a", "e", "i", "o", "u", "c", "A", "E", "I", "O", "U", "C" };

	public static Pattern[] PATTERNS = null;

	static {
		PATTERNS = new Pattern[REPLACES.length];
		PATTERNS[0] = Pattern.compile("[âãáàä]");
		PATTERNS[1] = Pattern.compile("[éèêë]");
		PATTERNS[2] = Pattern.compile("[íìîï]");
		PATTERNS[3] = Pattern.compile("[óòôõö]");
		PATTERNS[4] = Pattern.compile("[úùûü]");
		PATTERNS[5] = Pattern.compile("[ç]");
		PATTERNS[6] = Pattern.compile("[ÂÃÁÀÄ]");
		PATTERNS[7] = Pattern.compile("[ÉÈÊË]");
		PATTERNS[8] = Pattern.compile("[ÍÌÎÏ]");
		PATTERNS[9] = Pattern.compile("[ÓÒÔÕÖ]");
		PATTERNS[10] = Pattern.compile("[ÚÙÛÜ]");
		PATTERNS[11] = Pattern.compile("[Ç]");
	}

	public static String removeAccents(String text) {
		String result = text;
		for (int i = 0; i < PATTERNS.length; i++) {
			Matcher matcher = PATTERNS[i].matcher(result);
			result = matcher.replaceAll(REPLACES[i]);
		}
		return result;
	}
}


