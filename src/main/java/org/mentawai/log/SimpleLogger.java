package org.mentawai.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger implements Logger {

	private PrintWriter out;

	private final Format header = getHeaderFormat();

	private final Format rollFile = getRollFileFormat();

	private final MutableLong myLong = new MutableLong();

	private boolean enable = false;

	private File outputFile = null;
	
	private boolean alsoSystemOut = false;
	
	private String levelString = null;

	public SimpleLogger(PrintWriter out) {

		this.out = out;

		printTopHeader();

	}

	public SimpleLogger(String filename) throws IOException {

		this.outputFile = new File(filename);
		
		FileOutputStream fos = new FileOutputStream(outputFile, true);

		this.out = new PrintWriter(new OutputStreamWriter(fos), false);

		printTopHeader();

	}
	
	public SimpleLogger(String dir, String filename) throws IOException {
		
		if (!dir.endsWith("/") && !filename.startsWith("/")) {
			
			dir = dir + "/";
		}

		this.outputFile = new File(dir + filename);
		
		FileOutputStream fos = new FileOutputStream(outputFile, true);

		this.out = new PrintWriter(new OutputStreamWriter(fos), false);

		printTopHeader();

	}
	

	public SimpleLogger(File file) throws IOException {

		this.outputFile = file;
		
		FileOutputStream fos = new FileOutputStream(outputFile, true);

		this.out = new PrintWriter(new OutputStreamWriter(fos), false);

		printTopHeader();

	}

	public SimpleLogger(PrintStream stream) {
		
		this.out = new PrintWriter(new OutputStreamWriter(stream), true);
	}
	
	public void setAlsoSystemOut(boolean flag, String levelString) {
		
		this.alsoSystemOut = flag;
		
		this.levelString = levelString;
	}

	public synchronized void roll() throws IOException {

		if (outputFile == null || !outputFile.exists())
			return;

		out.close();

		String file = outputFile.getAbsoluteFile()
				+ rollFile.format(new Long(System.currentTimeMillis()));

		outputFile.renameTo(new File(file));

		out = new PrintWriter(new FileWriter(outputFile), false);

		printTopHeader();

	}

	public void enable(boolean flag) {

		this.enable = flag;
	}

	protected void printTopHeader() {

		out.println("LOG OPENED - " + (new Date()).toString());
	}

	protected Format getRollFileFormat() {

		return new SimpleDateFormat("-dd/MM/yyyy-HH:mm:ss");
	}

	protected Format getHeaderFormat() {

		return new SimpleDateFormat("dd.HH:mm:ss");
	}

	protected synchronized void header() {

		// here just for the heck of it and to show up that we care about
		// performance,
		// we are not creating one Long object for every log message because
		// that would post an overhead to the GC

		myLong.setValue(System.currentTimeMillis());
		
		String h = header.format(myLong);

		out.print(h);

		out.print(' ');
		
		if (alsoSystemOut) {
			
			System.out.print(levelString);
			
			System.out.print(" - ");
			
			System.out.print(h);
			
			System.out.print(' ');
		}

	}

	private static class MutableLong extends Number {

		private long value;

		public void setValue(long value) {

			this.value = value;
		}

		public int intValue() {

			if (value <= Integer.MAX_VALUE) {

				return (int) value;
			}

			throw new IllegalStateException(
					"Number is too big to be returned as Integer: " + value);
		}

		public long longValue() {

			return value;

		}

		public float floatValue() {

			return value;

		}

		public double doubleValue() {

			return value;
		}
	}

	public synchronized void log(String... msgs) {
		if (!enable)
			return;

		header();
		
		int count = 0;

		for (Object msg : msgs) {
			
			if (count++ > 0) {
				
				out.print(' ');
				
				if (alsoSystemOut) System.out.print(' ');
			}
			
			out.print(msg);
			
			if (alsoSystemOut) System.out.print(msg);
		}
		
		out.println();
		
		out.flush();
		
		if (alsoSystemOut) {
			
			System.out.println();
			
			System.out.flush();
		}
	}

	public synchronized void log(Object... objects) {
		if (!enable)
			return;

		header();
		
		int count = 0;

		for (Object object : objects) {
			
			if (count++ > 0) {
				
				out.print(' ');
				
				if (alsoSystemOut) System.out.print(' ');
			}
			
			out.print(object);
			
			if (alsoSystemOut) System.out.print(object);
		}

		out.println();

		out.flush();
		
		if (alsoSystemOut) {
			
			System.out.println();
			
			System.out.flush();
		}

	}

}