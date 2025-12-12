package com.epam.deltix.util.concurrent;

/**
 *  Thrown when a resource is unavailable. This class is used in conjunction with
 *  the {@link IntermittentlyAvailableResource} interface.
 */
public class UnavailableResourceException extends RuntimeException {
    private UnavailableResourceException () {
    }

    @Override
    public synchronized Throwable   fillInStackTrace () {
        return null;
    }

    public static final UnavailableResourceException    INSTANCE =
        new UnavailableResourceException ();
}
