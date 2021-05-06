package com.epam.deltix.util.repository;

public interface RepositoryEvent {
    
    /**
     * Checks this event exists in the array of events.
     * @param events - array of events
     * @return true if this event exists in the array, otherwise false
     */
    boolean isInto(RepositoryEvent... events);
    
}
