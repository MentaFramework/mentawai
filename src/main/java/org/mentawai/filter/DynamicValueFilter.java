package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.util.InjectionUtils;

/**
 * This abstract filter is useful for placing dynamic values
 * in the action output so that we can grab them in the view
 * with menta tags.
 * 
 * @author Sergio Oliveira Jr.
 *
 */
public abstract class DynamicValueFilter implements Filter {
	
	public void destroy() { }
	
	protected abstract String getParamName();
	
	protected abstract String getValueName();
	
	protected String getBeanName() {
		return null;
	}
	
	protected String getBeanProperty() {
		if (getBeanName() == null) return null;
		return getParamName();
	}
	
	protected Object getValue(Object value) {
		return value;
	}
	
	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		Input input = action.getInput();
		
		Output output = action.getOutput();
		
		Object val = input.getValue(getParamName());
		
		if (val != null) { // estah no input...
			
			// Erro de validacao...
			
			output.setValue(getValueName(), getValue(val));
			
			return chain.invoke();
			
		} else { // checa no restante da action...
			
			// Editando bean...
			
			String res = chain.invoke();
			
			String beanName = getBeanName();

			boolean found = false;
			
			if (beanName == null) {
				
				Object obj = BaseAction.findValue(getParamName(), action);
				
				if (obj != null) {
				
					output.setValue(getValueName(), getValue(obj));
					
					found = true;
				}
				
			} else {
			
				Object obj = BaseAction.findValue(beanName, action);
				
				if (obj != null) {
				
					// achei bean, estou editando mesmo...
					
					String value = InjectionUtils.getProperty(obj, getBeanProperty());
					
					if (value != null) {
						
						output.setValue(getValueName(), getValue(value));
							
						found = true;
					}
				}
			}
			
			if (!found) {
				
				// nao achei... coloca uma lista vazia
				// estou adicionando bean...
				
				output.setValue(getValueName(), getValue(null));
			}
			
			return res;
		}
	}
	
}
