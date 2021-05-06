package com.epam.deltix.util.repository;

import java.util.Collection;

public interface Repository<T> {
    
    Collection<T> getItems();
    
    Collection<T> getItems(RepositoryItemFilter<T> filter);
    
    void subscribe(RepositoryEventHandler<T> handler, RepositoryEvent... events);

    void unsubscribe(RepositoryEventHandler<T> handler);
    
}
