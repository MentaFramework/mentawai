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
package org.mentawai.filter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Deque;
import java.util.LinkedList;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;

/**
 * <p>
 * A filter for handling exceptions that may occour during the action's
 * execution. The filter catchs all the exceptions throwed by the action and
 * call the method handleException. The default implementation of the
 * handleException method put in the action's output the following attributes:
 * </p>
 *
 * <p>
 * "message" - The message of the Exception (e.getMessage()) <br/>
 * "stacktrace" - The Exception's stacktrace (e.getStackTrace()) <br/>
 * "stackheader" - The Exception's first stacktrace (e.getStackTrace()[0]) <br/>
 * </p>
 * <p>
 * Also, the method e.printStackTrace() is called if the attribute "trace"
 * is setted to true. </p>
 * <p>
 * After that, the filter return "exception" as the action's result. A
 * Consequence must be mapped for this result.<p>
 * <p>
 * The behavior of this filter can be changed by extending it's class and
 * overriding the handleException method.
 * </p>
 *
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class ExceptionFilter implements Filter {

    /** Attribute MESSAGE_KEY of ExceptionFilter. */
    public static String MESSAGE_KEY = "message";

    /** Attribute EXCEPTION_KEY of ExceptionFilter. */
    public static String EXCEPTION_KEY = "exception";

    /** Attribute STACK_TRACE_KEY of ExceptionFilter. */
    public static String STACK_TRACE_KEY = "stacktrace";

    /** Attribute STACK_HEADER_KEY of ExceptionFilter. */
    public static String STACK_HEADER_KEY = "stackheader";

    /** Attribute EXCEPTION of ExceptionFilter. */
    public static String EXCEPTION = "exception";

    /** Attribute trace of ExceptionFilter. */
    private boolean trace = true;

    /**
     * Default constructor.
     */
    public ExceptionFilter() {

    }

    /**
     * Parametric constructor.
     * @param trace boolean
     */
    public ExceptionFilter(boolean trace) {
        this.trace = trace;
    }

    /**
     * Execute the chain and cacth any exception that occours, delegating to the
     * handleException() method the resposability to handle the exception
     * throwed by the chain's execution.
     *
     * @return the result of the action's execution.
     */
    public String filter(InvocationChain chain) throws Exception {
        try {
            return chain.invoke();
        } catch (Throwable throwable) {
            return handleException(chain.getAction(), throwable);
        }
    }

    /**
     * Gets the root cause of exception.
     * @param throwable Throwable
     * @return Throwable
     */
    protected static Throwable getRootCause(Throwable throwable) {
        Deque < Throwable > list = new LinkedList < Throwable >();
        while (throwable != null && list.contains(throwable) == false) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        return list.getLast();
    }

    /**
     * Handle the exception, putting in the action's output the message of the
     * exception, all the StackTrace elements and the first StackTrace element.
     * <br/> This method can be overrided to change the behavior of the
     * ExceptionFilter.
     *
     * @param a the action that has throwed the exception.
     * @param throwable the exception throwed by the action's execution.
     * @return the result of the action's execution.
     */
    protected String handleException(Action a, Throwable throwable) {

        Throwable t = getRootCause(throwable);

        String exp = t != null ? t.getClass().getName() : throwable.getClass().getName();

        Output output = a.getOutput();
        output.setValue(EXCEPTION_KEY, exp);
        output.setValue(MESSAGE_KEY, t.getMessage());

        StringWriter sw = new StringWriter();

        PrintWriter pw = new PrintWriter(sw, true);

        t.printStackTrace(pw);

        if (t != null) {

            pw.println(sw.getBuffer().toString());

            pw.flush();

        }

        String full_trace = sw.getBuffer().toString();

        String[] lines = full_trace.split("\\n");

        output.setValue(STACK_HEADER_KEY, lines[0]);

        output.setValue(STACK_TRACE_KEY, prepareStackTrace(lines));

        if (trace) {

            if (t != null) {

                System.err.println(sw.getBuffer().toString());

                System.err.flush();

            }

        }

        return EXCEPTION;
    }

    protected String prepareStackTrace(String[] stacktrace) {
        StringBuffer sb = new StringBuffer(stacktrace.length * 75);
        for (int i = 0; i < stacktrace.length; i++) {
            String s = stacktrace[i];
            if (i != 0)
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            sb.append(prepareForHtml(s)).append("<br/>");
        }
        return sb.toString();
    }

    protected String prepareStackTrace(StackTraceElement[] stacktrace) {
        StringBuffer sb = new StringBuffer(stacktrace.length * 75);
        for (int i = 0; i < stacktrace.length; i++) {
            String s = stacktrace[i].toString();
            if (i != 0)
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            sb.append(prepareForHtml(s)).append("<br/>");
        }
        return sb.toString();
    }

    protected String prepareForHtml(String s) {
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        return s;
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {

    }

}
