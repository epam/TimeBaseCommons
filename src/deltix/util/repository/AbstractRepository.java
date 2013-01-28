package deltix.util.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractRepository<T> implements Repository<T> {
        
    @SuppressWarnings("NonConstantLogger")
    protected final Logger                          logger = Logger.getLogger(getClass().getName());
    protected final Object                          lock = new Object();
    
    private final List<RepositoryEventHandler<T>>   handlers = new ArrayList<>();
    private final Map<RepositoryEvent, List<RepositoryEventHandler<T>>> eventHandlers = new HashMap<>();    
    
    protected AbstractRepository() {
    }
            
    @Override
    public final void subscribe(RepositoryEventHandler<T> handler, RepositoryEvent... events) {
        if (events == null || events.length == 0) {
            throw new IllegalArgumentException("You haven't specified any events.");
        }
        
        synchronized (lock) {
            
            if (handlers.contains(handler)) {
                throw new IllegalArgumentException("Handler " + handler + " has been subscribed already.");
            }
            
            handlers.add(handler);
            
            for (RepositoryEvent event : events) {
                List<RepositoryEventHandler<T>> evntHandlers = this.eventHandlers.get(event);
                if (evntHandlers == null) {
                    evntHandlers = new ArrayList<>();
                    eventHandlers.put(event, evntHandlers);
                }
                
                evntHandlers.add(handler);
            }
            
            if (SCMDRepositoryEvent.SCANNED.isInto(events)) {
                for (T item : getItems()) {
                    handler.onEvent(item, SCMDRepositoryEvent.SCANNED);
                }
            }
        }
    }

    @Override
    public final void unsubscribe(RepositoryEventHandler<T> handler) {
        synchronized (lock) {
            
            if (!handlers.remove(handler)) {
                return;
            }
            
            for (List<RepositoryEventHandler<T>> evntHandlers : eventHandlers.values()) {
                evntHandlers.remove(handler);
            }                        
        }                           
    }
    
    protected final void checkHoldsLock() { 
        assert Thread.holdsLock(lock) : "Thread should hold the lock.";
    }    
        
    @SuppressWarnings("unchecked")
    protected final Collection<RepositoryEventHandler<T>> getHandlers(RepositoryEvent e) {
        checkHoldsLock();
        
        final Collection<RepositoryEventHandler<T>> result = eventHandlers.get(e);
        return result == null ? Collections.<RepositoryEventHandler<T>>emptyList() : result;
    }
    
    protected void start() {
        checkHoldsLock();
    }

    protected void stop() throws IOException {
        checkHoldsLock();
        
        handlers.clear();
        eventHandlers.clear();
    }        
    
}

