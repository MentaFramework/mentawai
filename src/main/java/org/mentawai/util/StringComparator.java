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

import java.util.Comparator;

/**
 * Compare two strings, not taking into account accents!
 * 
 * Based on the code found here: http://www.rgagnon.com/javadetails/java-0456.html
 * 
 * @author Sergio Oliveira
 */
public class StringComparator implements Comparator {
	
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		
		if (s1 == null && s2 == null) return 0;
		if (s1 == null) return -1;
		if (s2 == null) return 1;
		
		s1 = StringUtils.removeAccents(s1).toLowerCase();
		s2 = StringUtils.removeAccents(s2).toLowerCase();
		
		return s1.compareTo(s2);
	}
}