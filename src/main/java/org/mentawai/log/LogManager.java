package org.mentawai.log;

public class LogManager {
	
	public static void setLevel(Level level) {
		
		for(Level l : Level.getAll()) {
			
			if (l.getCode() >= level.getCode()) {
				
				l.enable(true);
			}
		}
	}
}