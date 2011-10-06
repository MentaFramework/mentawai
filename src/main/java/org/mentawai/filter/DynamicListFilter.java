package org.mentawai.filter;

import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.util.InjectionUtils;

/**
 * This abstract filter is useful for placing dynamic lists
 * in the action output so that a mtw:select cand display it.
 * 
 * This is useful for AJAX combos that vary according to another
 * combo.
 * 
 * @author Sergio Oliveira Jr.
 *
 */
public abstract class DynamicListFilter implements Filter {
	
	public void destroy() { }
	
	protected abstract Map<String, String> getMap(int id);
	
	protected abstract String getParamName();
	
	protected abstract String getListName();
	
	protected String getBeanName() {
		return null;
	}
	
	protected String getBeanProperty() {
		if (getBeanName() == null) return null;
		return getParamName();
	}
	
	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		Input input = action.getInput();
		
		Output output = action.getOutput();
		
		int id = input.getInt(getParamName());
		
		if (id > 0) { // estah no input...
			
			// Erro de validacao...
			
			output.setValue(getListName(), getMap(id));
			
			return chain.invoke();
			
		} else { // checa no restante da action...
			
			// Editando bean...
			
			String res = chain.invoke();
			
			String beanName = getBeanName();
			
			boolean found = false;
			
			if (beanName == null) {
				
				Object obj = BaseAction.findValue(getParamName(), action);
				
				if (obj != null) {
					
					try {
						
						id = Integer.parseInt(obj.toString());
						
						output.setValue(getListName(), getMap(id));
						
						found = true;
						
					} catch(Exception e) { }
				}
				
			} else {
			
				Object obj = BaseAction.findValue(beanName, action);
				
				if (obj != null) {
				
					// achei bean, estou editando mesmo...
					
					String value = InjectionUtils.getProperty(obj, getBeanProperty());
					
					if (value != null) {
						
						try {
						
							id = Integer.parseInt(value);
							
							output.setValue(getListName(), getMap(id));
							
							found = true;
							
						} catch(Exception e) { }
						
					}
				}
			}
			
			if (!found) {
				
				// nao achei... coloca uma lista vazia
				// estou adicionando bean...
				
				output.setValue(getListName(), getMap(-1));
			}
			
			return res;
		}
	}
	
}
