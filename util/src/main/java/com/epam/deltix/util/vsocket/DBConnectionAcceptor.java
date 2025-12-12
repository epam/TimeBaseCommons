package com.epam.deltix.util.vsocket;

/**
 * The interface tests if a connection can be accepted for the given client ID or not.
 */
public interface DBConnectionAcceptor {
    boolean accept(String clientId);
}
