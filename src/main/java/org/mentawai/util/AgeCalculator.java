package org.mentawai.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AgeCalculator {
	
	public static int calculate(int year, int month, int day) {
		
		if (month < 1 || month > 12) throw new IllegalArgumentException("Invalid month: " + month);
		
		if (day < 1 || day > 31) throw new IllegalArgumentException("Invalid day: " + day);
		
		month = month - 1; // 0 = Jan; 11 = Dec
		
		int ageYears;
		
		Calendar cd = Calendar.getInstance();
		
	    Calendar bd = new GregorianCalendar(year, month, day);
	    
	    bd.setLenient(false);
	    
	    ageYears = cd.get(Calendar.YEAR) - bd.get(Calendar.YEAR);
	    
	    if(cd.before(new GregorianCalendar(cd.get(Calendar.YEAR), month, day))) {
	    	
	      ageYears--;
	      
	    } else {
	    	
	      ageYears = cd.get(Calendar.YEAR) - bd.get(Calendar.YEAR);
	    }
	    
	    return ageYears;
	}
	
	public static int calculate(Calendar cd) {
		
		return calculate(cd.get(Calendar.YEAR), cd.get(Calendar.MONTH) + 1, cd.get(Calendar.DAY_OF_MONTH));
	}
	
	public static int calculate(Date birth) {
		
		Calendar cd = GregorianCalendar.getInstance();
		cd.setTime(birth);
		
		return calculate(cd);
	}
	
  public static void main(String[] args) {
	  
	  System.out.println("AGE: " + calculate(1976, 4, 30));
	  
  }
  
}