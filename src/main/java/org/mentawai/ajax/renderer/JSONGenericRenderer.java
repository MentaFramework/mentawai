package org.mentawai.ajax.renderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;
import org.mentawai.core.MapOutput;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.list.ListData;
import org.mentawai.list.ListItem;
import org.mentawai.util.InjectionUtils;
import org.mentawai.util.MentaJson;


/**
 * @author Robert Willian Gil
 * 
 * This renderer build a JSON text: <br>
 * <br>
 * Example: <br>
 * Map<String, String> map = new LinkedHashMap<String, String>();<br>
 * map.put("key1", "value1");<br>
 * map.put("key2", "value2");<br>
 * 
 * Return this:<br>
 * With default constructor JSONGenericRenderer();<br>
 * {"obj":[{"key":"key1","value":"value1"},{"key":"key2","value":"value2"}]}
 */
public class JSONGenericRenderer implements AjaxRenderer {

	String dateFormat = null;
	private int levels = 0;
	private int currentLevel = 0;
	
	/**
	 * List de propriedades excluidas.
	 * Ex: hibernateLazyInitializer
	 */
	private static List<String> excludedProperties = new ArrayList<String>();
	
	static {
		excludedProperties.add("hibernateLazyInitializer");
	}
	
	public JSONGenericRenderer() {
	}

	public JSONGenericRenderer(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public JSONGenericRenderer(int levels) {
		this.levels = levels;
	}
	
	public JSONGenericRenderer(String dateFormat, int levels) {
		this(dateFormat);
		this.levels = levels;
	}

	public String encode(Object object, Locale loc, boolean pretty) throws Exception {
		if(object instanceof String){
			return object.toString();
		}
		return buildJSON(object, loc).toString();
	}

	public String getContentType() {
		
		// return APP_JSON;
		
		return APP_URLENCODED; // better from binary objects!	
	}
   
   public String getCharset() {
      return AjaxConsequence.DEFAULT_CHARSET;
   }

	@SuppressWarnings("unchecked")
	protected JSONObject buildJSON(Object obj, Locale loc) {
		
		

		try {
			
			if (obj instanceof Map) {
			    
			    JSONObject jsonObj = MentaJson.getJSONObject();
			    jsonObj.put("obj", convertMap((Map) obj, loc));
			    
				return jsonObj;
				
			} else if (obj instanceof Collection) {
			    
		        JSONObject jsonObj = MentaJson.getJSONObject();
		        jsonObj.put("obj", convertListBean((Collection) obj, loc));
		    
				return jsonObj;
				
			} else if(obj instanceof ListData) {
			    
			    JSONObject jsonObj = MentaJson.getJSONObject();
			    jsonObj.put("obj", convertListData((ListData) obj, loc));
			    
				return jsonObj;
				
			} else {  // Every Bean Must implement Serializable
			    
			    JSONArray jsonArray = MentaJson.getJSONArray();
			    jsonArray.add(convertBean(obj, loc));
			    
			    JSONObject jsonObj = MentaJson.getJSONObject();
			    jsonObj.put("obj", jsonArray);
			    
				return jsonObj;
				
			}
			
			//throw new IllegalStateException("Object must be a (Map || List || ListData || Bean implemented Serializable)");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private JSONArray convertListData(ListData listData, Locale loc) {
		List<ListItem> itens =  listData.getValues(loc);
		JSONArray jsa = MentaJson.getJSONArray();
		JSONObject jso;
		for (ListItem listItem : itens) {
			try {

				jso = MentaJson.getJSONObject();
				jso.put("key", listItem.getKey());
				jso.put("value", listItem.getValue());
				jsa.add(jso);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return jsa;
	}
	
	private Object parseDate(Object value, Locale loc){
		Object result = null;
		if(value != null) {
			if(dateFormat == null){
				SimpleDateFormat sdf = LocaleManager.getSimpleDateFormat(loc);
				if (sdf != null) { result = sdf.format(value); }
			} else {
				result = new SimpleDateFormat(dateFormat).format(value);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject convertBean(Object bean, Locale loc){
		MapOutput om = new MapOutput();
		InjectionUtils.setObject(bean, om, null, false);
		Iterator<String> interator = om.keys();
		JSONObject jsonObj = MentaJson.getJSONObject();
		String propertyName;
		Object value;
		
		while(interator.hasNext()){
			propertyName = interator.next();
			
			if(excludedProperties.contains(propertyName))
				continue;
			
			value = om.getValue(propertyName);

			if(value instanceof Date) {
				value = parseDate(value, loc);
			}
			else
			if( value instanceof Calendar){
				value = parseDate( ((Calendar)value).getTime(), loc);
			}
			
			if (value == null) value = "";
			
			try {
			
				if(isValidBean(value) && !value.equals("") && (levels > currentLevel)){   // Probably is JavaBean
					
					currentLevel++;

					jsonObj.put(propertyName, convertBean(value, loc));		// Recursive call
					
				} else {
					
					if(isWrapper(value) && !isValidBean(value)) {

						jsonObj.put(propertyName, value.toString());
						
					} else {
						
						if (value instanceof Collection && (levels == 1) && (levels > currentLevel)) {
							
							Collection list = (Collection) value;
							jsonObj.put(propertyName, convertListBean(new ArrayList(list), loc));
							
						} else {
							
							jsonObj.put(propertyName, "");
							
						}

					}
					
					
				}
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		currentLevel = 0;
		
		return jsonObj;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray convertCollection(Collection col){
		JSONArray array = MentaJson.getJSONArray();
		for (Object object : col) {
			array.add(object.toString());
		}
		
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONArray convertListBean(Collection list, Locale loc) {
		
		JSONArray jsonArray = MentaJson.getJSONArray();
		for (Object object : list) {
			
			if(isValidBean(object)) {		// if is bean
				jsonArray.add(convertBean(object, loc));
				
			} else {
				jsonArray = convertCollection(list);
				break;
			}
			
		}
		
		return jsonArray;
	}
	 
 	@SuppressWarnings("unchecked")
	private JSONArray convertMap(Map obj, Locale loc) {
		Map map = (Map) obj;
		Entry entry = null;
		Object value;
		JSONArray jsonArray = MentaJson.getJSONArray();
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			entry = (Entry) iter.next();
			try {
				
				value = entry.getValue();
				
				if(value instanceof Map){			// Recursive call
				    
				    JSONObject jsonObj = MentaJson.getJSONObject();
				    jsonObj.put("key", entry.getKey().toString());
				    jsonObj.put("value", convertMap( (Map) value, loc));
				    
					jsonArray.add( jsonObj  );
				
				} else if (value instanceof Collection){
				    
				    JSONObject jsonObj = MentaJson.getJSONObject();
				    jsonObj.put("key", entry.getKey().toString());
				    jsonObj.put("value", convertCollection( (Collection) value)  );
				    
					jsonArray.add( jsonObj );
					
				} else if(value instanceof ListData) {
				    
				    JSONObject jsonObj = MentaJson.getJSONObject();
				    jsonObj.put("key", entry.getKey().toString());
				    jsonObj.put("value", convertListData( (ListData) value, loc));
				    
					jsonArray.add( jsonObj );
					
				} else {
				    
				    JSONObject jsonObj = MentaJson.getJSONObject();
				    jsonObj.put("key", entry.getKey().toString());
				    jsonObj.put("value", entry.getValue());
				    
					jsonArray.add( jsonObj );
					
				}
				
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return jsonArray;
	}
	
	
	protected static boolean isWrapper(Object o){
		
		if(o instanceof Number) return true;
		if(o instanceof String) return true;
		if(o instanceof Character) return true;
		if(o instanceof Boolean) return true;
		
		return false;
	}
	
	protected static boolean isValidBean(Object o){
		
		if(o.getClass().getName().startsWith("java.util.") || o.getClass().getName().startsWith("java.lang."))
			return false;
		
		// Hibernate Maldito!
		if(o instanceof Collection)
			return false;
		
		// by Sergio: This should not be necessary,
		// but let's make some tests to be sure.
		// Robert is aware of that. (08/19/2008)
		/*
		try {		// Looking for default constructor
			o.getClass().getDeclaredConstructor();
		} catch (Exception e) {
			return false;
		}
		
		if(o instanceof Serializable) return true;
		*/
		
		return true;
	}
	
	public static void main(String[] args) throws Exception {
	    
	    Map<String, Object> map = new HashMap<String, Object>();
	    
	    map.put("success", true);
	    
	    Map<String, String> data = new HashMap<String, String>();
	    
	    data.put("key1", "data1");
	    data.put("key2", "data2");
	    data.put("key3", "data3");
	    
	    map.put("data", data);
	    
	    AjaxRenderer renderer = new JSONGenericRenderer();
	    
	    System.out.println(renderer.encode(map, null, true));
	}

}
