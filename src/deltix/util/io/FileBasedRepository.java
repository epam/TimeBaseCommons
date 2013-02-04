package deltix.util.io;

import deltix.util.io.FileSystemWatcher.EventType;
import deltix.util.repository.AbstractRepository;
import deltix.util.repository.RepositoryEventHandler;
import deltix.util.repository.RepositoryItemFilter;
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

public abstract class FileBasedRepository<T> extends AbstractRepository<T> {
        
    protected final File                            root;
    
    private final Map<Path, FileItem>               items = new HashMap<>();    
    private final FileSystemWatcher.EventHandler    fsEventHandler;
    
    protected FileBasedRepository(File root) {
        super(FileSystemWatcher.getInstance().getLock());
        
        this.root = root;

        fsEventHandler = new FileSystemWatcher.EventHandler() {
            @Override
            public void onEvent(File file, EventType event) {
                synchronized (lock) {
                    update(file, event);
                }
            }
        };
    }
    
    public final File getRoot() {
        return root;
    }
         
    @Override
    public final Collection<T> getItems() {
        return getItems(null);
    }
        
    @Override
    public final Collection<T> getItems(RepositoryItemFilter<T> filter) {
        final List<T> result = new ArrayList<>();
        
        synchronized (lock) {
            for (FileItem fItem : items.values()) {
                final T item = fItem.item;
                if (filter == null || filter.accepted(item)) {
                    result.add(item);
                }
            }
        }
        return result;
    }        

    protected void update(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {                
                update(file, EventType.SCANNED);
            } else {
                update(file, !items.containsKey(file.toPath())
                        ? EventType.SCANNED : EventType.MODIFIED);
            }
        } else {
            update(file, EventType.DELETED);
        }
    }

    protected void update(File file, EventType event) {

        checkHoldsLock();

        final Path path = file.toPath();

        if (event == EventType.DELETED) { // a file or folder is deleted                    

            final FileItem fItem = items.remove(path);

            if (fItem != null) { // if a file
                for (RepositoryEventHandler<T> handler : getHandlers(SCMDRepositoryEvent.DELETED)) {
                    handler.onEvent(fItem.item, SCMDRepositoryEvent.DELETED);
                }
            } else {
                // try to check all sub-items and remove them
                for (Map.Entry<Path, FileItem> iKv : new HashMap<>(items).entrySet()) {
                    if (iKv.getKey().startsWith(path)) {
                        final FileItem deletedItem = items.remove(iKv.getKey());

                        for (RepositoryEventHandler<T> handler : getHandlers(SCMDRepositoryEvent.DELETED)) {
                            handler.onEvent(deletedItem.item, SCMDRepositoryEvent.DELETED);
                        }
                    }
                }
            }

        } else if (file.isDirectory()) {

            switch (event) {
                case SCANNED:
                case CREATED:

                    if (isSubscribableFolder(file)) {
                        
                        for (Map.Entry<Path, FileItem> iKv : new HashMap<>(items).entrySet()) {
                            if (iKv.getKey().startsWith(path)) {
                                break; // already under watching
                            }
                        }                        
                        
                        try {
                            FileSystemWatcher.getInstance().subscribe(fsEventHandler, file, EventType.SCANNED, EventType.CREATED, EventType.MODIFIED, EventType.DELETED);
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

                    if (items.containsKey(path)) { // already exists
                        break;
                    }

                    try {
                        final FileItem fItem = new FileItem(file, prepareItem(file));
                        items.put(path, fItem);

                        for (RepositoryEventHandler<T> handler : getHandlers(e)) {
                            handler.onEvent(fItem.item, e);
                        }
                    } catch (Throwable t) {
                        logger.log(Level.WARNING, "An error while preparing item for " + path, t);
                    }
                    break;
                case MODIFIED:

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

                                for (RepositoryEventHandler<T> handler : getHandlers(SCMDRepositoryEvent.CREATED)) {
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

                                for (RepositoryEventHandler<T> handler : getHandlers(SCMDRepositoryEvent.DELETED)) {
                                    handler.onEvent(fItem.item, SCMDRepositoryEvent.DELETED);
                                }
                            } catch (Throwable t) {
                                logger.log(Level.WARNING, "An error while preparing item for " + path, t);
                            }
                        } else if (lastModified != fItem.lastModified) {
                            // modified
                            try {
                                fItem.lastModified = lastModified;

                                fItem.item = prepareItem(file);

                                for (RepositoryEventHandler<T> handler : getHandlers(SCMDRepositoryEvent.MODIFIED)) {
                                    handler.onEvent(fItem.item, SCMDRepositoryEvent.MODIFIED);
                                }
                            } catch (Throwable t) {
                                logger.log(Level.WARNING, "An error while preparing item for " + path, t);
                            }
                        }
                    }
                    break;
            }
        }

    }

    @Override
    protected void start() {                 
        super.start();
        
        try {
            FileSystemWatcher.getInstance().subscribe(fsEventHandler, root, EventType.SCANNED, EventType.CREATED, EventType.MODIFIED, EventType.DELETED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void stop() throws IOException {
        super.stop();

        FileSystemWatcher.getInstance().unsubscribe(fsEventHandler);
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
