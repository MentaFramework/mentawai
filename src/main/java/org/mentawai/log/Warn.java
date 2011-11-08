package org.mentawai.log;

import java.io.File;
import java.io.IOException;

import org.mentawai.core.ApplicationManager;

public class Warn {
	
	private static boolean ENABLED = false;
    
    public static String FILENAME = "WARN.LOG";
    
    private static Logger logger;
    
    static {
        
        try {
            
        	 boolean isConsole = Boolean.getBoolean("logConsole");
             boolean isFile = Boolean.getBoolean("logFile");
             
             if (!isFile && !isConsole) {
             	isConsole = true; // default is console!
             } else if (isFile && isConsole) {
             	isConsole = false; // give priority to logFile!
             }
            
            if (isConsole) {
                
                logger = new SimpleLogger(System.out);
                
            } else {
            
                String filename = System.getProperty("logWarnFile", FILENAME);
            
                String dir = System.getProperty("logDir");
                
                if (dir == null) {
                	
                	dir = ApplicationManager.getRealPath();
                	
                	if (!dir.endsWith("/")) dir = dir + "/";
                	
                	dir = dir + "WEB-INF/logs";
                	
                	File theDir = new File(dir);
                	
                	if (!theDir.exists()) theDir.mkdir();
                }
            
                SimpleLogger sl = new SimpleLogger(dir, filename);
                
                boolean noSystemOut = Boolean.getBoolean("noSystemOut");
                
                if (!noSystemOut) {
                	
                	sl.setAlsoSystemOut(true, "WARN");
                }
                
                logger = sl;
            }
            
            enable(true); // by deafult enabled...
            
        } catch(IOException e) {
            
            System.err.println("Cannot open " + FILENAME + "!!!");
            
            e.printStackTrace();
            
            logger = null;
        }
    }
    
    public static void enable(boolean flag) {
        
        if (logger != null) {
        	
        	logger.enable(flag);
        	
        	ENABLED = flag;
        }
    }
    
    public static boolean isEnabled() {
    	
    	return ENABLED;
    }
    
    public static void roll() throws IOException {
        
        if (logger != null) logger.roll();
    }

    public static void log(String... msgs) {
        
        if (logger != null) logger.log(msgs);
    }
    
    public static void log(Object... objects) {
        
        if (logger != null) logger.log(objects);

    }
    
    /**
     * Set custom logger
     */
    public static void setLogger(Logger logger) {
		Warn.logger = logger;
	}
}