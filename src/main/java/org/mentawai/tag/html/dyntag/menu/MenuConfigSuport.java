package org.mentawai.tag.html.dyntag.menu;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Robert Willian
 */

public class MenuConfigSuport {

	
	
	private static Map<String, String> PARS = new HashMap<String, String>();

	public static StringBuffer doConfig(Map<String, String> parameters) {
		
		StringBuffer result = new StringBuffer();
		PARS = parameters;
		
		result.append("<script type=\"text/javascript\">\n");
		result.append("c_styles={};c_menus={};\n");
		result.append("c_menus['mtwMenu']=[");
		result.append("[");
		result.append("'" + valid("horizontalOrVertical") + "',"); // ItemsArrangement 'vertical','horizontal')
		result.append("'relative',"); // Position ('relative','absolute','fixed')
		result.append("'0',"); // X Position (values in CSS valid units- px,em,ex)
		result.append("'0',"); // Y Position (values in CSS valid units- px,em,ex)
		result.append("false,"); // RightToLeft display of the sub menus
		result.append(" false,"); // BottomToTop display of the sub menus
		result.append("0,"); // X SubMenuOffset (pixels)
		result.append("0,"); // Y SubMenuOffset
		result.append("'" + valid("onVerticalWidth") + "',"); // Width (values in CSS valid units - px,em,ex) (matters for main menu with 'vertical' ItemsArrangement only)
		result.append("'MM',"); // CSS Class (one of the defined in section 2)
		result.append(valid("openMenuOnClick")); // Open sub-menus onclick (default is onmouseover)
		result.append("],["); // SUB-MENUS FEATURES
		result.append("0,"); // X SubMenuOffset (pixels)
		result.append("0,"); // Y SubMenuOffset 
		result.append(" 'auto',"); // Width ('auto',values in CSS valid units - px,em,ex)
		result.append("'" + valid("subMenuMinWidth") + "',"); // MinWidth ('pixels') (matters/useful if Width is set 'auto')
		result.append("'" + valid("subMenuMaxWidth") + "',"); // MaxWidth ('pixels') (matters/useful if Width is set 'auto')
		result.append("'SM',"); // CSS Class (one of the defined in section 2)
		result.append(valid("openSubMenuOnClick")); // Open sub-menus onclick (default is onmouseover)
		result.append("]];\n");
		result.append("</script>\n\n");

		return result;
	}
	
	private static String valid(String attribute){
		
		if(attribute == "horizontalOrVertical") {
			if(PARS.get(attribute) == null){
				return "horizontal";				// Default Value
			} else {
				return PARS.get(attribute);
			}
		}
		
		if(attribute == "subMenuMinWidth") {
			if(PARS.get(attribute) == null){
				return "100";				// Default Value
			} else {
				return PARS.get(attribute);
			}
		}
		
		if(attribute == "subMenuMaxWidth") {
			if(PARS.get(attribute) == null){
				return "300";				// Default Value
			} else {
				return PARS.get(attribute);
			}
		}
		
		if(attribute == "openMenuOnClick") {
			if(PARS.get(attribute) == null){
				return "false";				// Default Value
			} else {
				return PARS.get(attribute);
			}
		}
		
		if(attribute == "openSubMenuOnClick") {
			if(PARS.get(attribute) == null){
				return "false";				// Default Value
			} else {
				return PARS.get(attribute);
			}
		}
		
		if(attribute == "onVerticalWidth") {
			if(PARS.get(attribute) == null){
				return "12em";				// Default Value
			} else {
				return PARS.get(attribute);
			}
		}
		
		return null;
	}
}
