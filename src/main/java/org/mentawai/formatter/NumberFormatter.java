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
package org.mentawai.formatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Marvin Herman Froeder
 *
 */
public class NumberFormatter implements Formatter {

    private int style = -1;

    private String pattern = null;

    private DecimalFormatSymbols symbols;

    public static final int INTEGER = 0;

    public static final int REAL = 1;

    public static final int PERCENTAGE = 2;

    public static final int CURRENCY = 3;

    /**
     * @param style
     */
    public NumberFormatter(int style) {
        this.style = style;
    }

    public NumberFormatter(String pattern) {
        this(pattern, new DecimalFormatSymbols(Locale.getDefault()));
    }

    public NumberFormatter(String pattern, DecimalFormatSymbols symbols) {
        this.pattern = pattern;
        this.symbols = symbols;
    }

    public String format(Object value, Locale loc) {
    	
        if (!(value instanceof Number)) return value.toString();

        NumberFormat nf;
        Number n = (Number) value;

        if (this.pattern != null) {
        	
            nf = new DecimalFormat(this.pattern, this.symbols);
            
        } else if (this.style != -1) {
        	
            switch (this.style) {
            
		        case INTEGER:
		            nf = NumberFormat.getIntegerInstance(loc);
		            break;
		        case REAL:
		            nf = NumberFormat.getNumberInstance(loc);
		            break;
		        case PERCENTAGE:
		            nf = NumberFormat.getPercentInstance(loc);
		            break;
		        case CURRENCY:
		            nf = NumberFormat.getCurrencyInstance(loc);
		            break;
		        default:
		            throw new IllegalArgumentException("Invalid Style number: " + this.style);
            }
            
        } else {

            throw new IllegalStateException("Should never be here!");
        }

        return nf.format(n);
    }
}