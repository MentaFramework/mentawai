package org.mentawai.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExecUtil {
	
	public static String WIN_CMD = "cmd.exe";
	
	public static String execWin(String cmd) throws IOException, InterruptedException {
		
		Runtime rt = Runtime.getRuntime();
		
		String[] s = cmd.split("\\s+");
		
		String[] c = new String[2 + s.length];
		
		c[0] = WIN_CMD;
		c[1] = "/C";
		
		System.arraycopy(s, 0, c, 2, s.length);
		
		Process proc = null;
		
		StringGrabber grabber = null;
		
		try {
		
	        proc = rt.exec(c);
	
	        grabber = new StringGrabber(proc.getInputStream());
	        
	        grabber.start();
	        
	        int exitVal = proc.waitFor();
	        
	        if (exitVal == 0) {
	        	
	        	grabber.join();
	        	
	        	Thread.sleep(50);
	        	
	        	return grabber.getString();
	        }
        
		} finally {
			
			if (grabber != null) { grabber.kill(); }
			
			if (proc != null) { proc.destroy(); }
		}
        
		return null;
	}
	
    public static String execLinux(String cmd) throws IOException, InterruptedException {
		
		Runtime rt = Runtime.getRuntime();
		
		String[] s = cmd.split("\\s+");
		
		String[] c = new String[s.length];
		
		System.arraycopy(s, 0, c, 0, s.length);
		
		Process proc = null;
		
		StringGrabber grabber = null;
		
		try {
		
	        proc = rt.exec(c);
	
	        grabber = new StringGrabber(proc.getInputStream());
	        
	        grabber.start();
	        
	        int exitVal = proc.waitFor();
	        
	        if (exitVal == 0) {
	        	
	        	grabber.join();
	        	
	        	Thread.sleep(50);
	        	
	        	return grabber.getString();
	        }
        
		} finally {
			
			if (grabber != null) { grabber.kill(); }
			
			if (proc != null) { proc.destroy(); }
		}
        
		return null;
	}
	
	static class StringGrabber extends Thread {
		
	    private InputStream is;
	    private StringBuilder sb = new StringBuilder(1024);
	    private volatile boolean running = false;
	    private volatile boolean killed = false;
	    
	    public StringGrabber(InputStream is) {
	        this.is = is;
	        setDaemon(true);
	    }

	    @Override
	    public void start() {
	    	running = true;
	    	super.start();
	    }
	    
	    public String getString() {
	    	
	    	//if (running) throw new IllegalStateException("StringGrabber is still running!");
	    	
	    	return sb.toString();
	    }

	    public void kill() {
	    	if (running) {
	    		killed = true;
	    		interrupt();
	    	}
	    }
	    
	    public boolean isRunning(){ return running; }
	    
	    public void run() {
	    	
	    	try {
	    		
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            
	            String line = null;
	            
	            while((line = br.readLine()) != null) {
	            	
	            	sb.append(line).append('\n');
	            }
	            
	    	} catch(java.lang.Exception e) {
	    		if (!killed) e.printStackTrace();
	    	}
	    	
	    	running = false;
	    }
	}
}