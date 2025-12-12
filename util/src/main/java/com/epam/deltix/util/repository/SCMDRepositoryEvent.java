package com.epam.deltix.util.repository;

public enum SCMDRepositoryEvent implements RepositoryEvent {    
    /**
     * An item exists in the repository by the moment of subscription.
     */
    SCANNED,
    /**
     * A new item is created.
     */
    CREATED,
    /**
     * An item is modified.
     */
    MODIFIED,
    /**
     * An item is deleted.
     */
    DELETED;        
    
    @Override
    public boolean isInto(RepositoryEvent... events) {
        if (events != null) {
            for (int i = 0; i < events.length; i++) {
                if (this == events[i]) {
                    return true;
                }
            }
        }
        return false;
    }
}
