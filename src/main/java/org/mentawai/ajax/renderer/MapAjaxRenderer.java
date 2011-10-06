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
package org.mentawai.ajax.renderer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;
import org.mentawai.ajax.DOMUtils;
/**
 * <p>A AjaxRender that gets map from the action`s output
 * and creates a XML structure with the contents of this
 * map.</p>
 * <p>For example, a Map with this content: <br/>
 * <pre>
 * |      key      |      value      |
 * |     akey      |      avalue     |
 * | yetAnotherKey | yetAnotherValue |
 * </pre>
 * <br/>
 * Would create the folowing XML structure:
 * <pre>
 * &ltmap&gt
 *     &ltentry key="akey"&gtavalue&lt/entry&gt
 *     &ltentry key="yetAnotherKey"&gtyetAnotherValue&lt/entry&gt
 *  &lt/map&gt   
 * </pre>
 * </p>
 * <p>The tag names ("map" & "entry") may be changed.</p>
 * <p>Then you can use your favorite JavaScript parser (or use your own)
 * to parse the generated XML strucure and interact it in your code.</p>
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * @author Fernando Boaglio - update to jdom 1.0 
 */
public class MapAjaxRenderer implements AjaxRenderer {
	
	public static final String FATHER = "map";
	public static final String CHILD = "entry";
	public static final String KEY = "key"; 
	
	private String root;
	private String child;
	private String key;
	
	public MapAjaxRenderer(String root, String child, String key) {
		this.root = root;
		this.child = child;
		this.key = key;
	}

	public MapAjaxRenderer() {
		this.root = FATHER;
		this.child = CHILD;
		this.key = KEY;
	}
	
	public String encode(Object obj, Locale loc, boolean pretty) throws Exception {
		
		if (!(obj instanceof Map)) throw new IllegalArgumentException("Value is not a Map: " + obj);
		
		Map<?, ?> map = (Map<?, ?>) obj;
		
		Document document = new Document();
		
		Element mapElement  = new Element(root);
		
		for (Object element: map.entrySet()) {
			
			Content child = new Element(this.child);
			
			Entry<?, ?> entry = (Entry<?, ?>) element;
			
			String key = entry.getKey().toString();
			
			String value = entry.getValue().toString();
			
			((Element) child).setAttribute(this.key, key);
			
			((Element) child).setText(value);
			
			mapElement.addContent(child);
		}
		
		document.setRootElement(mapElement);
		
		return DOMUtils.getDocumentAsString(document, pretty);
	}

    public String getContentType() {
        return TEXT_XML;
    }
    
    public String getCharset() {
       return AjaxConsequence.DEFAULT_CHARSET;
    }

    
    public static void main(String[] args) throws Exception {
    	
    	Map<String, String> map = new HashMap<String, String>();
    	
    	map.put("sergio","sergio@sergio.com.br");
    	map.put("patty","patty@patty.com.br");
    	
    	AjaxRenderer render = new MapAjaxRenderer("usuarios", "usuario", "username");
    	
    	System.out.println(render.encode(map, new Locale("en"), true));
    	
    }
}
