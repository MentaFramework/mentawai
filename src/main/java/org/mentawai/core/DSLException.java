package org.mentawai.core;

/**
 * A exception to be throw related to the execution of a script application manager.
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 *
 */
public class DSLException extends RuntimeException {

    public DSLException(String message, Throwable cause) {
        super(message, cause);
    }

    public DSLException(String message) {
        super(message);
    }

    public DSLException(Throwable cause) {
        super(cause);
    }

    
}
