package deltix.util.io;

import deltix.util.io.FileSystemWatcher.EventType;
import deltix.util.repository.Repository;
import deltix.util.repository.RepositoryEvent;
import deltix.util.repository.RepositoryEventHandler;
import deltix.util.repository.SCMDRepositoryEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class FileBasedRepository<T> implements Repository<T> {
        
    protected final File                            root;
    @SuppressWarnings("NonConstantLogger")
    protected final Logger                          logger = Logger.getLogger(getClass().getName());
    
    private final List<RepositoryEventHandler<T>>   handlers = new ArrayList<>();
    private final Map<RepositoryEvent, List<RepositoryEventHandler<T>>> eventHandlers = new HashMap<>();    
    private final Map<Path, FileItem>               items = new HashMap<>();    
    private final FileSystemWatcher.EventHandler    fsEventHandler;
    
    protected FileBasedRepository(File root) {
        this.root = root;          
        
        fsEventHandler = new FileSystemWatcher.EventHandler() {
            @Override
            public void onEvent(File file, EventType event) {
                
                final Path path = file.toPath();
                
                if (event == EventType.DELETED) { // a file or folder is deleted                    

                    synchronized (items) {

                        final FileItem fItem = items.remove(path);
                        final List<RepositoryEventHandler<T>> hdrs = eventHandlers.get(SCMDRepositoryEvent.DELETED);

                        if (fItem != null) { // if a file
                            try {
                                if (hdrs != null) {
                                    for (RepositoryEventHandler<T> handler : hdrs) {
                                        handler.onEvent(fItem.item, SCMDRepositoryEvent.DELETED);
                                    }
                                }
                            } catch (Throwable t) {
                                logger.log(Level.WARNING, "An error while preparing item for " + path, t);
                            }
                        } else {
                            // try to check all sub-items and remove them
                            for (Map.Entry<Path, FileItem> iKv : new HashMap<>(items).entrySet()) {
                                if (iKv.getKey().startsWith(path)) {
                                    final FileItem deletedItem = items.remove(iKv.getKey());

                                    if (hdrs != null) {
                                        for (RepositoryEventHandler<T> handler : hdrs) {
                                            handler.onEvent(deletedItem.item, SCMDRepositoryEvent.DELETED);
                                        }
                                    }
                                }
                            }
                        }
                    }                    
                    
                } else if (file.isDirectory()) {
                    
                    switch (event) {
                        case SCANNED:
                        case CREATED:
                            if (isSubscribableFolder(file)) {
                                try {
                                    FileSystemWatcher.getInstance().subscribe(this, file, EventType.SCANNED, EventType.CREATED, EventType.MODIFIED, EventType.DELETED);
                                } catch (IOException e) {
                                    logger.log(Level.WARNING, "An error while subscription to " + file, e);
                                }
                            }
                            break;
                    }
                   
                } else if (isItemFile(file)) { // a file item
                    
                    switch (event) {
                        case SCANNED:
                        case CREATED:
                            if (file.length() == 0) {
                                break;
                            }
                            
                            final SCMDRepositoryEvent e = event == EventType.SCANNED ? SCMDRepositoryEvent.SCANNED : SCMDRepositoryEvent.CREATED;
                            
                            synchronized (items) {                                
                                if (items.containsKey(path)) { // already exists
                                    break;
                                }
                                
                                try {
                                    final FileItem fItem = new FileItem(file, prepareItem(file));
                                    items.put(path, fItem);

                                    final List<RepositoryEventHandler<T>> hdrs = eventHandlers.get(e);
                                    
                                    if (hdrs != null) {
                                        for (RepositoryEventHandler<T> handler : hdrs) {
                                            handler.onEvent(fItem.item, e);
                                        }
                                    }
                                } catch (Throwable t) {
                                    logger.log(Level.WARNING, "An error while preparing item for " + path, t);
                                }                                
                            }
                            break;
                        case MODIFIED:
                            synchronized (items) {                                
                                
                                FileItem fItem = items.get(path);

                                final boolean isNew = fItem == null;
                                final boolean isEmpty = file.length() == 0;
                                final long lastModified = file.lastModified();
                                    
                                if (isNew) {
                                    if (!isEmpty) {
                                        // created
                                        try {
                                            fItem = new FileItem(file, prepareItem(file));
                                            items.put(path, fItem);

                                            for (RepositoryEventHandler<T> handler : eventHandlers.get(SCMDRepositoryEvent.CREATED)) {
                                                handler.onEvent(fItem.item, SCMDRepositoryEvent.CREATED);
                                            }
                                        } catch (Throwable t) {
                                            logger.log(Level.WARNING, "An error while preparing item for " + path, t);
                                        }                                        
                                    }
                                } else {
                                    if (isEmpty) {
                                        // deleted
                                        try {
                                            items.remove(path);

                                            final List<RepositoryEventHandler<T>> hdrs = eventHandlers.get(SCMDRepositoryEvent.DELETED);
                                            
                                            if (hdrs != null) {
                                                for (RepositoryEventHandler<T> handler : hdrs) {
                                                    handler.onEvent(fItem.item, SCMDRepositoryEvent.DELETED);
                                                }
                                            }
                                        } catch (Throwable t) {
                                            logger.log(Level.WARNING, "An error while preparing item for " + path, t);
                                        }                                                                                
                                    } else if (lastModified != fItem.lastModified) {
                                        // modified
                                        try {
                                            fItem.lastModified = lastModified;

                                            fItem.item = prepareItem(file);
                                            
                                            final List<RepositoryEventHandler<T>> hdrs = eventHandlers.get(SCMDRepositoryEvent.MODIFIED);
                                            
                                            if (hdrs != null) {
                                                for (RepositoryEventHandler<T> handler : hdrs) {
                                                    handler.onEvent(fItem.item, SCMDRepositoryEvent.MODIFIED);
                                                }
                                            }
                                        } catch (Throwable t) {
                                            logger.log(Level.WARNING, "An error while preparing item for " + path, t);
                                        }                                                                                
                                    }                                    
                                }                                
                            }
                            break;
                    }
                }
            }
        };        
    }
        
    @Override
    public Collection<T> getItems() {
        final List<T> result = new ArrayList<>();
        synchronized (items) {
            for (FileItem fItem : items.values()) {
                result.add(fItem.item);
            }
        }
        return result;
    }    
    
    @Override
    public void subscribe(RepositoryEventHandler<T> handler, RepositoryEvent... events) {
        if (events == null || events.length == 0) {
            throw new IllegalArgumentException("You haven't specified any events.");
        }
        
        synchronized (items) {
            
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
                for (FileItem fItem : items.values()) {
                    handler.onEvent(fItem.item, SCMDRepositoryEvent.SCANNED);
                }
            }
        }
    }

    @Override
    public void unsubscribe(RepositoryEventHandler<T> handler) {
        synchronized (items) {
            
            if (!handlers.remove(handler)) {
                return;
            }
            
            for (List<RepositoryEventHandler<T>> evntHandlers : this.eventHandlers.values()) {
                evntHandlers.remove(handler);
            }                        
        }                           
    }
    
    protected final void start() {                 
        checkHoldsLock();
        
        try {
            FileSystemWatcher.getInstance().subscribe(fsEventHandler, root, EventType.SCANNED, EventType.CREATED, EventType.MODIFIED, EventType.DELETED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected final void stop() throws IOException {
        checkHoldsLock();
        
        handlers.clear();
        eventHandlers.clear();

        FileSystemWatcher.getInstance().unsubscribe(fsEventHandler);
    }
    
    protected final Object getLock() {
        return items;
    }
    
    protected final void checkHoldsLock() { 
        assert Thread.holdsLock(items) : "Thread should hold the lock.";
    }
    
    protected abstract boolean isSubscribableFolder(File folder);
    
    protected abstract boolean isItemFile(File file);
    
    protected abstract T prepareItem(File file) throws IOException;
    
    private class FileItem {
        private long        lastModified;
        private T           item;        

        private FileItem(File itemFile, T item) {
            lastModified = itemFile.lastModified();
            this.item = item;
        }         
    }
}
