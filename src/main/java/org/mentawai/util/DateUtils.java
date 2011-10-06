package org.mentawai.util;

import java.util.*;

import javax.management.timer.Timer;

public final class DateUtils {
   
   public static Date onlyDate(Date date) {
      
      Calendar cal = Calendar.getInstance();
      
      cal.setTime(date);
      
      cal.set(Calendar.MINUTE, 0);
      
      cal.set(Calendar.HOUR_OF_DAY, 0);
      
      cal.set(Calendar.SECOND, 0);
      
      cal.set(Calendar.MILLISECOND, 0);
      
      return cal.getTime();
   }
   
   public static boolean isToday(Date date) {
      
      Date now = onlyDate(new Date());
      
      Date d = onlyDate(date);
      
      return now.equals(d);
   }
   
   public static long daysDiff(Date d1, Date d2) {
	   
	   long t1 = d1.getTime();
	   long t2 = d2.getTime();
	   
	   return (t1 - t2) / (1000 * 60 * 60 * 24);
   }
   
   public static String getRelativeDate(Date date, Locale loc) { 
   	
	      long delta = new Date().getTime() - date.getTime();
	      
	      if (delta < 5L * Timer.ONE_SECOND) {
	    	  return "now";
	      }
	      if (delta < 1L * Timer.ONE_MINUTE) {
	        return toSeconds(delta) == 1 ? "one second ago" : toSeconds(delta) + " seconds ago";
	      }
	      if (delta < 2L * Timer.ONE_MINUTE) {
	        return "a minute ago";
	      }
	      if (delta < 45L * Timer.ONE_MINUTE) {
	        return toMinutes(delta) + " minutes ago";
	      }
	      if (delta < 90L * Timer.ONE_MINUTE) {
	        return "an hour ago";
	      }
	      if (delta < 24L * Timer.ONE_HOUR) {
	        return toHours(delta) + " hours ago";
	      }
	      if (delta < 48L * Timer.ONE_HOUR) {
	        return "yesterday";
	      }
	      if (delta < 30L * Timer.ONE_DAY) {
	        return toDays(delta) + " days ago";
	      }
	      if (delta < 12L * 4L * Timer.ONE_WEEK) { // month
	        long months = toMonths(delta); 
	        return months <= 1 ? "one month ago" : months + " months ago";
	      }
	      else {
	        long years = toYears(delta);
	        return years <= 1 ? "one year ago" : years + " years ago";
	      }
	    }

	    private static long toSeconds(long date) {
	      return date / 1000L;
	    }

	    private static long toMinutes(long date) {
	      return toSeconds(date) / 60L;
	    }

	    private static long toHours(long date) {
	      return toMinutes(date) / 60L;
	    }

	    private static long toDays(long date) {
	      return toHours(date) / 24L;
	    }

	    private static long toMonths(long date) {
	      return toDays(date) / 30L;
	    }

	    private static long toYears(long date) {
	      return toMonths(date) / 365L;
	    }
   
}