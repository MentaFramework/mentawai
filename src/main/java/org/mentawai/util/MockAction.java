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
package org.mentawai.util;

import java.lang.reflect.Method;
import java.util.Locale;
import java.lang.Exception;

import org.mentawai.core.Action;
import org.mentawai.core.ActionException;
import org.mentawai.core.Context;
import org.mentawai.core.MapContext;
import org.mentawai.core.Input;
import org.mentawai.core.MapInput;
import org.mentawai.core.Output;
import org.mentawai.core.MapOutput;

/*
 * Testando a action sem o mock:
 * MyAction action = new MyAction();
 * Input input = new InputMap();
 * Output output = new OutputMap();
 * Context session = new ContextMap();
 * Context application = new ContextMap();
 * 
 * action.serInput(input);
 * action.setOutput(output);
 * action.setSession(session);
 * action.setApplication(application);
 * 
 * Coloca as coisas no input, application e session...
 *
 * String result = action.execute();
 * assertEqual(result, "balblablablal");
 * assertEqual(output.getValue("asdasd"), "asdasdasdasdas");
 * 
 * com o mock:
 * 
 * MyAction action = new MyAction();
 * MockAction mockAction = new MockAction(action);
 * mockAction.getInput().setValue("asdsaxa", adsdasdas);
 * String result = mockAction.execute();
 * assertEqual(result, "balblablablal");
 * assertEqual(mockAction.getOutput().getValue("asdasd"), "asdasdasdasdas");
 */
 
/**
 * <p>A mock for facilitating action testing. 
 * It internally creates mocks for the input, output, session and context.</p>
 * Example: <br/>
 * <br/>
 * Testing the action without this mock: <br/>
 * <pre>
 * MyAction action = new MyAction();
 * Input input = new InputMap();
 * Output output = new OutputMap();
 * Context session = new ContextMap();
 * Context application = new ContextMap();
 * action.setInput(input);
 * action.setOutput(output);
 * action.setSession(session);
 * action.setApplication(application);
 * input.setValue("asdasdas", someValue)/
 * String result = action.execute();
 * assertEqual(result, "balblablablal");
 * assertEqual(output.getValue("asdasd"), "asdasdasdasdas");
 * </pre>
 * with the mock:
 * <pre>
 * MyAction action = new MyAction();
 * MockAction mockAction = new MockAction(action);
 * mockAction.getInput().setValue("asdsaxa", adsdasdas);
 * String result = mockAction.execute();
 * assertEqual(result, "balblablablal");
 * assertEqual(mockAction.getOutput().getValue("asdasd"), "asdasdasdasdas");
 * </pre>
 *
 * @author Rubem Azenha
 */
public class MockAction implements Action {

	private Action action;
	
	public MockAction(Action action) {
		this.action = action;
        init(this);
	}
    
    public MockAction(Class<? extends Object> klass) {
        try {
            this.action = (Action) klass.newInstance();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        init(this);
    }
    
    public MockAction() {
    	this.action = new org.mentawai.action.SuccessAction();
    	init(this);
    }
    
    /**
     * Creates a mock action from the given class, returning
     * a new instance of the given class that can be used
     * as a regular action, but it is actually a mock action.
     * 
     * @param <E>
     * @param klass
     * @return action
     * @throws Exception
     */
    public static <E> E getMockAction(Class<? extends E> klass) {
    	
    	try {
    	
	    	E a = klass.newInstance();
	    	
	    	if (!(a instanceof Action)) {
	    		
	    		throw new InstantiationException("Class is not an Action: " + klass);
	    	}
	    	
	    	Action action = (Action) a;
	    	
	    	init(action);
	    	
	    	return a;
    	
    	} catch(Exception e) {
    		
    		throw new RuntimeException(e);
    	}
    }
    
    public static void init(Action action) {
        action.setInput(new MapInput());
        action.setOutput(new MapOutput());
        action.setSession(new MapContext());
        action.setApplication(new MapContext());
        action.setCookies(new MapContext());
        action.setLocale(Locale.getDefault());
    }
	
	public String execute() throws Exception {
      
      Method m = getMethod("execute");
      if (m != null) {
            try {
                return (String) m.invoke(action, new Object[0]);
            } catch(Exception e) {
                throw new ActionException(e);
            }
        } else {
            throw new ActionException("The method execute cannot be found!");
        }        
	}
    
    public String callInnerAction(String innerAction) throws Exception {
        
       Method m = getMethod(innerAction);
		if (m != null) {
            try {
                return (String) m.invoke(action, new Object[0]);
            } catch(Exception e) {
                throw new ActionException(e);
            }
        } else {
            throw new ActionException("The inner action does not exist: " + innerAction);
        }        
    }
    
	private Method getMethod(String innerAction) {
		try {
			Method m = action.getClass().getDeclaredMethod(innerAction, new Class[0]);
			if (m != null) {
				return m;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Action getAction() {
		return action;
	}

	public Context getApplication() {
		return action.getApplication();
	}

	public void setApplication(Context application) {
		action.setApplication(application);
	}

	public Input getInput() {
		return action.getInput();
	}

	public void setInput(Input input) {
		action.setInput(input);
	}

	public Locale getLocale() {
		return action.getLocale();
	}

	public void setLocale(Locale locale) {
		action.setLocale(locale);
	}

	public Output getOutput() {
		return action.getOutput();
	}

	public void setOutput(Output output) {
		action.setOutput(output);
	}

	public Context getSession() {
		return action.getSession();
	}

	public void setSession(Context session) {
		action.setSession(session);
	}
    
    public Context getCookies() {
        return action.getCookies();
    }
    
    public void setCookies(Context context) {
        action.setCookies(context);
    }
}