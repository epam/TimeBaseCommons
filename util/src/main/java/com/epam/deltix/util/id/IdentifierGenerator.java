package com.epam.deltix.util.id;

/** Thread-safe generator of unique integer numbers */
public interface IdentifierGenerator {
	/** @return next valid order id. This method is thread safe */
    long next();

    /**
     * Special method to support UHF Failover. Method re-establishes next unique order ID from original media.
     * @throws UnsupportedOperationException if reset/failover is not supported
     * */
    void reset()
        throws UnsupportedOperationException;
}
