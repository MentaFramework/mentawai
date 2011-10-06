package org.mentawai.i18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class I18NHolder {
	private class EmptyI18n extends I18N {

	    public EmptyI18n() {
	        super(null);
	    }
		
		public String get(int key) {
			return get(String.valueOf(key));
		}
	    
	    public boolean hasKey(String key) {
	        return false;
	    }

	    public String get(String key) {
	    	return '!' + key + '!';
	    }
		
		public Iterator<String> keys() {
			return new ArrayList<String>().iterator();
		}
	}

	private Map<Locale, I18N> map = new HashMap<Locale, I18N>();

	public void clear() {
		map.clear();
	}

	public boolean containsLocale(Locale loc) {
		return map.containsKey(loc);
	}

	public boolean containsI18n(I18N i18n) {
		return map.containsValue(i18n);
	}

	public I18N get(Locale loc) {
		I18N i18n = map.get(loc);
		if (i18n != null)
			return i18n;
		return new EmptyI18n();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public List<Locale> locales() {
		return new ArrayList<Locale>(map.keySet());
	}

	public I18N add(Locale loc, I18N i18n) {
		return map.put(loc, i18n);
	}

	public void putAll(Map<? extends Locale, ? extends I18N> map) {
		this.map.putAll(map);
	}

	public I18N remove(Locale loc) {
		return this.map.remove(loc);
	}

	public int size() {
		return this.map.size();
	}
}
