package org.mentawai.coc;

import java.util.HashMap;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Consequence;
import org.mentawai.core.Forward;

public class InnerActionConsequenceProvider implements ConsequenceProvider {
   
   private final Map<String, String> dirCache = new HashMap<String, String>();
   
    /**
     * This method will imply a directory name from the action name.
     * 
     * Ex: 
     * Action = HelloWorldAction
     * Directory = /helloworld
     * 
     * @param actionName The action class name
     * @return a directory where to look for JSPs
     */
    protected String getDir(String actionName) {
      
      String dir = null;
      
      synchronized(dirCache) {
         
         dir = dirCache.get(actionName);
      }
      
      if (dir != null) return dir;
        
        String dirName = actionName.toLowerCase();
        
        if (dirName.endsWith("action")) {
        
           int index = dirName.lastIndexOf("action");
           
           if (index > 0) {
               
               dirName = dirName.substring(0, index);
           }
        }
        
        synchronized(dirCache) {
         
         dirCache.put(actionName, dirName);
         
        }
        
        return dirName;
    }

   public Consequence getConsequence(String action, Class<? extends Object> actionClass, String result, String innerAction) {
   
      if (result == null) return null;
      
      if (innerAction == null) {
         
         // This CoC is designed solely for innerActions, so that it can use
         // the name of the innerAction as the page name...
         
         return null;
      }
      
      if (!result.equals(Action.SUCCESS) && !result.equals(Action.ERROR)) {
         
         // This CoC is designed solely for SUCCESS results...
         
         return null;
      }
        
        StringBuffer sb = new StringBuffer(128);
        
        String dirName = getDir(action);
        
        sb.append("/").append(dirName).append("/");
        
        sb.append(innerAction).append(".jsp");
        
        return new Forward(sb.toString());
   }
}
