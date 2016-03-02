package org.mentawai.rmi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentawai.core.Action;
import org.mentawai.core.Consequence;
import org.mentawai.core.ConsequenceException;
import org.mentawai.core.Output;

public class RmiConsequence implements Consequence {
	
	public static final String RMI_RESULT_OUTPUT_KEY = "rmiResult";

	@Override
    public void execute(Action a, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		
		Output output = a.getOutput();
		
		if (!output.has(RMI_RESULT_OUTPUT_KEY)) {
			throw new ConsequenceException("Cannot find RMI result in the action output!");
		}
		
		Object o = output.getValue(RMI_RESULT_OUTPUT_KEY);
		
		res.setContentType("text/plain");
		
		try {
			
			PrintWriter out = res.getWriter();
			
			if (o == null) {
				out.println("null");
				out.close();
				return;
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			
			String hex = RmiClient.fromBytesToHex(baos.toByteArray());
			
			out.println(hex);
			out.close();
			
		} catch(IOException e) {
			throw new ConsequenceException("Cannot write response!", e);
		}
    }
	
}