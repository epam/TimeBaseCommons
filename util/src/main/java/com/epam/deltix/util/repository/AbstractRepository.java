/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.repository;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;

import java.io.IOException;
import java.util.*;

public abstract class AbstractRepository<T> implements Repository<T> {
        
    @SuppressWarnings("NonConstantLogger")
    protected final Log logger = LogFactory.getLog(getClass().getName());
    protected final Object                          lock;
    
    private final List<RepositoryEventHandler<T>>   handlers = new ArrayList<>();
    private final Map<RepositoryEvent, List<RepositoryEventHandler<T>>> eventHandlers = new HashMap<>();    
    
    protected AbstractRepository(Object lock) {
        this.lock = lock;
    }
            
    @Override
    public final void subscribe(RepositoryEventHandler<T> handler, RepositoryEvent... events) {
        if (events == null || events.length == 0) {
            throw new IllegalArgumentException("You haven't specified any events.");
        }
        
        synchronized (lock) {
            
            if (!handlers.contains(handler)) {
                handlers.add(handler);

                for (RepositoryEvent event : events) {
                    List<RepositoryEventHandler<T>> evntHandlers = this.eventHandlers.get(event);
                    if (evntHandlers == null) {
                        evntHandlers = new ArrayList<>();
                        eventHandlers.put(event, evntHandlers);
                    }

                    evntHandlers.add(handler);
                }                
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