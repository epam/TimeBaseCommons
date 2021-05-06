package com.epam.deltix.timebase.messages;

/**
 * Generic Identity definition.
 */
public interface IdentityKey {

    /**
     * Identity name.
     * @return String representation of identity name.
     */
    public CharSequence         getSymbol ();
}
