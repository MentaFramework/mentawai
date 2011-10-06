package org.mentawai.filter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;

public class DateFilter implements Filter {
	
	
	private final String dayField, monthField, yearField;
	
	private final String dateField;
	
	public DateFilter(String dayField, String monthField, String yearField, String dateField) {
		this.dayField = dayField;
		this.monthField = monthField;
		this.yearField = yearField;
		this.dateField = dateField;
	}
	
	public String filter(InvocationChain chain) throws Exception {
		
		Input input = chain.getAction().getInput();
		Locale loc = chain.getAction().getLocale();
		String lang = loc.getLanguage();
		
		String dd = input.getString(dayField);
		String mm = input.getString(monthField);
		String yyyy = input.getString(yearField);
		
		if (dd != null && mm != null && yyyy != null) {
			if (!dd.trim().equals("") && !mm.trim().equals("") && !yyyy.trim().equals("")) {
				
				if (lang.equals("pt")) {
				
					input.setValue(dateField, dd + "/" + mm + "/" + yyyy);
					
				} else {
					
					input.setValue(dateField, mm + "/" + dd + "/" + yyyy);
				}
			}
		}
		
		String res = chain.invoke();
		
		Output output = chain.getAction().getOutput();
		
		Object obj = output.getValue(dateField);
		
		if (obj instanceof Date) {
			
			Date d = (Date) obj;
			
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(d);
			
			prepareOutput(output, c);
			
		} else if (obj instanceof Calendar) {
			
			Calendar c = (Calendar) obj;
			
			prepareOutput(output, c);
		}
		
		return res;
	}
	
	private void prepareOutput(Output output, Calendar c) {
		
		output.setValue(dayField, c.get(Calendar.DAY_OF_MONTH));
		output.setValue(monthField, c.get(Calendar.MONTH) + 1);
		output.setValue(yearField, c.get(Calendar.YEAR));
	}
	
	public void destroy() { }
	
	
}