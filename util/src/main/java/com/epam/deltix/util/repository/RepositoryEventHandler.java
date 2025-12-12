package com.epam.deltix.util.repository;

public interface RepositoryEventHandler<T> {
    void onEvent(T item, RepositoryEvent event);
}
