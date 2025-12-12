package com.epam.deltix.util.lang;

import java.io.*;

/**
 *  Prints stack trace to specified stream
 */
public class ConsoleExceptionHandler implements ExceptionHandler {
    private PrintStream     mOut;
    
    public ConsoleExceptionHandler (PrintStream out) {
        mOut = out;
    }
    
    public void handle (Throwable x) {
        x.printStackTrace (System.err);
    }
    
    /**
     *  Prints to System.err
     */
    public static final ConsoleExceptionHandler     STDERR_HANDLER =
        new ConsoleExceptionHandler (System.err);
}
