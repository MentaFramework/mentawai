package org.mentawai.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Form {
	
	private final String name;
	
	private final Set<String> fields = new LinkedHashSet<String>();
	private final Map<String, Type> fieldsToTypes = new HashMap<String, Type>();
	private String[] fieldsArray = { };
	private String[] fieldsBooleansArray = { };
	private String[] fieldsListsArray = { };
	private String[] fieldsDatesArray = { };
	
	private final Set<String> requiredFields = new LinkedHashSet<String>();
	private String[] requiredFieldsArray = { };
	private String[] requiredFieldsMinusBooleansArray = { };
	
	private final Set<String> optionalFields = new LinkedHashSet<String>();
	private String[] optionalFieldsArray = { };
	
	
	public Form(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
	public String toString() { return name; }
	
	public int hashCode() { return name.hashCode(); }
	
	public boolean equals(Object obj) {
		if (obj instanceof Form) {
			Form f = (Form) obj;
			return f.name.equals(name);
		}
		return false;
	}
	
	public void addFields(String ... fields) {
		for(int i=0;i<fields.length;i++) {
			
			String field = fields[i];
			
			String name = getName(field);
			Type type = getType(field);
			
			this.fields.add(name);
			this.fieldsToTypes.put(name, type);
		}
		
		fieldsArray = getFieldsArray();
		fieldsBooleansArray = getFieldsBooleansArray();
		fieldsDatesArray = getFieldsDatesArray();
		fieldsListsArray = getFieldsListsArray();
	}
	
	public void addField(String field) {
		
		String name = getName(field);
		Type type = getType(field);
		
		fields.add(name);
		fieldsToTypes.put(name, type);
		fieldsArray = getFieldsArray();
		fieldsBooleansArray = getFieldsBooleansArray();
		fieldsDatesArray = getFieldsDatesArray();
		fieldsListsArray = getFieldsListsArray();
	}
	
	public boolean hasField(String field) {
		return fields.contains(field);
	}
	
	public int getNumberOfFields() {
		return fields.size();
	}
	
	public String[] getFields() {
		return fieldsArray;
	}
	
	public String[] getBooleanFields() {
		return fieldsBooleansArray;
	}
	
	public String[] getListFields() {
		return fieldsListsArray;
	}
	
	public String[] getDateFields() {
		return fieldsDatesArray;
	}
	
	private String[] getFieldsArray() {
		String[] s = new String[fields.size()];
		return fields.toArray(s);
	}
	
	private String[] getFieldsBooleansArray() {
		
		List<String> list = new ArrayList<String>(fieldsArray.length);
		
		for(int i=0;i<fieldsArray.length;i++) {
			String f = fieldsArray[i];
			Type t = fieldsToTypes.get(f);
			if (t == Type.BOOLEAN) {
				list.add(f);
			}
		}
		
		String[] s = new String[list.size()];
		
		return list.toArray(s);
	}
	
	private String[] getFieldsListsArray() {
		
		List<String> list = new ArrayList<String>(fieldsArray.length);
		
		for(int i=0;i<fieldsArray.length;i++) {
			String f = fieldsArray[i];
			Type t = fieldsToTypes.get(f);
			if (t == Type.LIST) {
				list.add(f);
			}
		}
		
		String[] s = new String[list.size()];
		
		return list.toArray(s);
	}	
	
	private String[] getFieldsDatesArray() {
		
		List<String> list = new ArrayList<String>(fieldsArray.length);
		
		for(int i=0;i<fieldsArray.length;i++) {
			String f = fieldsArray[i];
			Type t = fieldsToTypes.get(f);
			if (t == Type.DATE) {
				list.add(f);
			}
		}
		
		String[] s = new String[list.size()];
		
		return list.toArray(s);
	}	
	
	private String[] diffArray(String[] a1, String[] a2) {
		Set<String> diff = new LinkedHashSet<String>();
		for(int i=0;i<a1.length;i++) {
			String s = a1[i];
			if (!contains(s, a2)) {
				diff.add(s);
			}
		}
		
		String[] array = new String[diff.size()];
		return diff.toArray(array);
	}
	
	private boolean contains(String s, String[] array) {
		for(int i=0;i<array.length;i++) {
			if (s.equals(array[i])) return true;
		}
		return false;
	}
	
	// required fields...
	
	public void addRequiredFields(String ... fields) {
		addRequiredFields(false, fields);
	}
	
	public void addRequiredFields(boolean ignoreUnknownField, String ... fields) {
		
		if (optionalFields.size() > 0) {
			throw new IllegalStateException("This form already has optional fields! (" + this + ")");
		}
		
		for(int i=0;i<fields.length;i++) {
			if (!ignoreUnknownField && !hasField(fields[i])) {
				throw new IllegalArgumentException("Unknown field: " + fields[i] + "(" + this + ")");
			}
			this.requiredFields.add(fields[i]);
		}
		requiredFieldsArray = getRequiredFieldsArray();
		requiredFieldsMinusBooleansArray = getRequiredFieldsMinusBooleansArray();
		optionalFieldsArray = diffArray(fieldsArray, requiredFieldsArray);
	}
	
	public void addRequiredField(String field) {
		addRequiredField(false, field);
	}
	
	public void addRequiredField(boolean ignoreUnkownField, String field) {
		
		if (optionalFields.size() > 0) {
			throw new IllegalStateException("This form already has optional fields! (" + this + ")");
		}
		
		if (!ignoreUnkownField && !hasField(field)) {
			throw new IllegalArgumentException("Unknown field: " + field + "(" + this + ")");
		}
		requiredFields.add(field);
		requiredFieldsArray = getRequiredFieldsArray();
		requiredFieldsMinusBooleansArray = getRequiredFieldsMinusBooleansArray();
		optionalFieldsArray = diffArray(fieldsArray, requiredFieldsArray);
	}
	
	public boolean isRequired(String field) {
		return requiredFields.contains(field) || !optionalFields.contains(field);
	}
	
	public String[] getRequiredFields() {
		return requiredFieldsArray;
	}
	
	public String[] getRequiredFieldsMinusBooleans() {
		return requiredFieldsMinusBooleansArray;
	}
	
	private String[] getRequiredFieldsMinusBooleansArray() {
		Set<String> list = new LinkedHashSet<String>();
		
		for(int i=0;i<requiredFieldsArray.length;i++) {
			String field = requiredFieldsArray[i];
			Type type = fieldsToTypes.get(field);
			if (type != Type.BOOLEAN) {
				list.add(field);
			}
		}
		
		String[] s = new String[list.size()];
		
		return list.toArray(s);
	}
	
	public int getNumberOfRequiredFields() {
		return requiredFieldsArray.length;
	}
	
	private String[] getRequiredFieldsArray() {
		String[] s = new String[requiredFields.size()];
		return requiredFields.toArray(s);
	}
	
	// optional fields...
	
	public void addOptionalFields(String ... fields) {
		addOptionalFields(false, fields);
	}
	
	public void addOptionalFields(boolean ignoreUnknownField, String ... fields) {
		
		if (requiredFields.size() > 0) {
			throw new IllegalStateException("This form already has required fields! (" + this + ")");
		}
		
		for(int i=0;i<fields.length;i++) {
			if (!ignoreUnknownField && !hasField(fields[i])) {
				throw new IllegalArgumentException("Unknown field: " + fields[i] + "(" + this + ")");
			}
			this.optionalFields.add(fields[i]);
		}
		optionalFieldsArray = getOptionalFieldsArray();
		requiredFieldsArray = diffArray(fieldsArray, optionalFieldsArray);
		requiredFieldsMinusBooleansArray = getRequiredFieldsMinusBooleansArray();
	}
	
	public void addOptionalField(String field) {
		addOptionalField(false, field);
	}
	
	public void addOptionalField(boolean ignoreUnknownField, String field) {
		
		if (requiredFields.size() > 0) {
			throw new IllegalStateException("This form already has required fields!");
		}

		if (!ignoreUnknownField && !hasField(field)) {
			throw new IllegalArgumentException("Unknown field: " + field);
		}
		optionalFields.add(field);
		optionalFieldsArray = getOptionalFieldsArray();
		requiredFieldsArray = diffArray(fieldsArray, optionalFieldsArray);
		requiredFieldsMinusBooleansArray = getRequiredFieldsMinusBooleansArray();
	}
	
	public boolean isOptional(String field) {
		return optionalFields.contains(field) || !requiredFields.contains(field);
	}
	
	public String[] getOptionalFields() {
		return optionalFieldsArray;
	}
	
	public int getNumberOfOptionalFields() {
		return optionalFieldsArray.length;
	}
	
	private String[] getOptionalFieldsArray() {
		String[] s = new String[optionalFields.size()];
		return optionalFields.toArray(s);
	}
	
	enum Type { TEXT, LIST, BOOLEAN, DATE }
	
	private Type getType(String s) {
		
		String[] array = s.split("\\:");
		
		if (array.length == 2) {
			
			String type = array[1].toUpperCase();
			
			try {
				
				return Type.valueOf(type);
				
			} catch(IllegalArgumentException e) {
				
				throw new IllegalArgumentException("Unkown type: " + type + " / " + name);
			}
			
		} else {
			
			throw new IllegalArgumentException("Invalid field name: " + s + " / " + name);
		}
	}
	
	private String getName(String field) {
		
		String[] array = field.split("\\:");
		
		return array[0];
		
	}
}