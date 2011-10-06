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
package org.mentawai.bean;

import org.mentawai.bean.type.AutoIncrementType;
import org.mentawai.bean.type.AutoTimestampType;
import org.mentawai.bean.type.BigDecimalType;
import org.mentawai.bean.type.BooleanEnumType;
import org.mentawai.bean.type.BooleanIntType;
import org.mentawai.bean.type.BooleanStringType;
import org.mentawai.bean.type.DateType;
import org.mentawai.bean.type.DoubleType;
import org.mentawai.bean.type.FloatType;
import org.mentawai.bean.type.IntegerType;
import org.mentawai.bean.type.LongType;
import org.mentawai.bean.type.SequenceType;
import org.mentawai.bean.type.StringType;
import org.mentawai.bean.type.TimeType;
import org.mentawai.bean.type.TimestampType;

public class DBTypes {
	
	
	
	public static StringType STRING = new StringType();
	public static IntegerType INTEGER = new IntegerType();
	public static DateType DATE = new DateType();
	public static SequenceType SEQUENCE = new SequenceType();
	public static AutoIncrementType AUTOINCREMENT = new AutoIncrementType();
	public static AutoTimestampType AUTOTIMESTAMP = new AutoTimestampType();
	public static LongType LONG = new LongType();
	public static DoubleType DOUBLE = new DoubleType();
	public static TimeType TIME = new TimeType();
	public static TimestampType TIMESTAMP = new TimestampType();
	public static FloatType FLOAT = new FloatType();
	public static BooleanStringType BOOLEANSTRING = new BooleanStringType();
	public static BooleanEnumType BOOLEANENUM = new BooleanEnumType();
	public static BooleanIntType BOOLEANINT = new BooleanIntType();
	public static BigDecimalType BIGDECIMAL = new BigDecimalType();

	
}