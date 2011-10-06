package org.mentawai.util;

public class EnumUtils {

	public static <T extends Enum<T>> T getFromString(Class<T> c, String string) {
		
		if (c != null && string != null) {
		
			try {
			
				return Enum.valueOf(c, string.trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				
			}
		}
		
		return null;
	}
}