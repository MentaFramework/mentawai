package org.mentawai.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.message.Message;
import org.mentawai.message.MessageManager;

/**
 * 
 * @author Robert Willian Gil
 *
 */
public class AjaxValidationFilter implements Filter {

	public void destroy() {
	}

	@SuppressWarnings("unchecked")
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		Locale loc = action.getLocale();
		String result = chain.invoke();
		Output output = action.getOutput();

		if (result.equals(Action.ERROR)) {
			Map<String, Message> fieldErrors = MessageManager.getFieldErrors(
					action, true);
			if (!fieldErrors.isEmpty()) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				Entry entry = null;

				for (Iterator iter = fieldErrors.entrySet().iterator(); iter
						.hasNext();) {
					entry = (Entry) iter.next();
					Message msg = fieldErrors.get(entry.getKey());
					map.put(entry.getKey().toString(), msg.getText(loc));
				}
				output.setValue(AjaxConsequence.OBJECT, map);
			} else { // if not error of validation, send error createds with addError() 
				List<Message> errors = MessageManager.getErrors(action, true);
				
				// Send addErrors if exist
				if(!errors.isEmpty()){
					output.setValue(AjaxConsequence.OBJECT, proccessList(errors, loc));
					
				} else {
					// output cant be null
					// put empty map soo.
					if(output.getValue(AjaxConsequence.OBJECT) == null)
						output.setValue(AjaxConsequence.OBJECT, new HashMap<String, String>());
					
				}
				
			}
		} else { // if not be ERROR

			List<Message> messages = MessageManager.getMessages(action, true);
			
			// Send addMessages if exist
			if(!messages.isEmpty()){
				output.setValue(AjaxConsequence.OBJECT, proccessList(messages, loc));
				
			} else {
				// output cant be null
				// put empty map soo.
				if(output.getValue(AjaxConsequence.OBJECT) == null)
					output.setValue(AjaxConsequence.OBJECT, new HashMap<String, String>());
				
			}
			
		}
		return result;
	}

	protected Map<String, String> proccessList(List<Message> list, Locale loc) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		int msgNumber = 0;
		for (Message message : list) {
			msgNumber++;
			map.put("msg" + msgNumber, message.getText(loc));
		}

		return map;
	}

}
