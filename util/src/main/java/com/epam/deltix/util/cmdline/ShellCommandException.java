package com.epam.deltix.util.cmdline;

/**
 * Thrown to indicate that shell command was recognized but was not properly processed.
 *
 * @author Alexei Osipov
 */
public class ShellCommandException extends RuntimeException {
    private final int errorLevel;

    public ShellCommandException(String message) {
        this(message, 1);
    }

    public ShellCommandException(String message, int errorLevel) {
        super(message);
        this.errorLevel = errorLevel;
    }

    public int getErrorLevel() {
        return errorLevel;
    }
}
