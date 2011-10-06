package org.mentawai.log;

import java.util.EnumSet;

public enum Level {

	DEBUG(1), INFO(2), EVENT(3), ERROR(4);
	
	private int code;

    private Level(int code) {
    	this.code = code;
    }

    public int getCode() {
    	return code;
    }
    
    public static EnumSet<Level> getAll() {
    	
    	return EnumSet.allOf(Level.class);
    }
    
    public void enable(boolean flag) {
    	
    	if (code == 1) Debug.enable(flag);
    	else if (code == 2) Info.enable(flag);
    	else if (code == 3) Event.enable(flag);
    	else if (code == 4) Error.enable(flag);
    }
    
    public static Level from(String s) {
    	
    	return Level.valueOf(s.toUpperCase());
    }
}
