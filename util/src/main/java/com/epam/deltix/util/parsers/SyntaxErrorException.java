package com.epam.deltix.util.parsers;

/**
 *
 */
public class SyntaxErrorException extends CompilationException {
    public SyntaxErrorException (long location) {
        this ("Syntax error", location);
    }

    public SyntaxErrorException (String msg, long location) {
        super (msg, location);
    }
}
