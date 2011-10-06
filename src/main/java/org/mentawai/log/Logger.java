package org.mentawai.log;

import java.io.IOException;

public interface Logger {

	public void enable(boolean flag);

	public void roll() throws IOException;

	public void log(String... msg);

	public void log(Object... o1);

}