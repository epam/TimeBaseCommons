package deltix.util.repository;

import java.util.Collection;

public interface Repository<T> {
    
    Collection<T> getItems();
    
    void subscribe(RepositoryEventHandler<T> handler, RepositoryEvent... events);

    void unsubscribe(RepositoryEventHandler<T> handler);
    
}
