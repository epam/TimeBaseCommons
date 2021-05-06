package com.epam.deltix.util.io;

import com.epam.deltix.util.io.FileSystemWatcher.EventType;
import com.epam.deltix.util.repository.AbstractRepository;
import com.epam.deltix.util.repository.RepositoryEventHandler;
import com.epam.deltix.util.repository.RepositoryItemFilter;
import com.epam.deltix.util.repository.SCMDRepositoryEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FileBasedRepository<T> extends AbstractRepository<T> {
    private static final int                        TEMP_FILE_LIFE_TIME = 10 * 60 * 1000;
    private static final String                     WATCH_FS_PROPERTY = FileBasedRepository.class.getName() + ".watchFs";
        
    protected final File                            root;
    protected final boolean                         watchFs;
    
    private final Map<Path, FileItem>               items = new HashMap<>();    
    private final FileSystemWatcher.EventHandler    fsEventHandler;

    protected FileBasedRepository(final File root) {
        this(root, true);
    }
    
    protected FileBasedRepository(final File root, final boolean watchFs) {
        super(FileSystemWatcher.getInstance().getLock());
        
        this.root = root;
        
        final String watchFsProperty = System.getProperty(WATCH_FS_PROPERTY);        
        if (watchFsProperty != null && watchFsProperty.equalsIgnoreCase("false")) {
            this.watchFs = false;
            logger.info("File system monitoring has beed disabled for %s by the system property %s.").with(getClass().getSimpleName()).with(WATCH_FS_PROPERTY);
        } else {        
            this.watchFs = watchFs;
        }

        fsEventHandler = new FileSystemWatcher.EventHandler() {
            @Override
            public void onEvent(File file, EventType event) {
                synchronized (lock) {
                    update(file, event);
                }
            }
        };
        
        try {
            // clean-up the old temp files/folders if exist
            Files.walkFileTree(root.toPath(), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    final File fDir = dir.toFile();
                    
                    if (root.equals(fDir.getParentFile()) &&
                            dir.getFileName().startsWith(IOUtil.TEMP_FILE_PREFIX)
                            && System.currentTimeMillis() - attrs.creationTime().toMillis() > TEMP_FILE_LIFE_TIME) { // older than 10 mins                
                        
                        IOUtil.deleteFileOrDir(fDir);
                        
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }                
                
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    final File fFile = file.toFile();
                    
                    if (root.equals(fFile.getParentFile()) &&
                            file.getFileName().startsWith(IOUtil.TEMP_FILE_PREFIX)
                            && System.currentTimeMillis() - attrs.creationTime().toMillis() > TEMP_FILE_LIFE_TIME) { // older than 10 mins                
                        
                        IOUtil.deleteFileOrDir(fFile);
                    }
                    return FileVisitResult.CONTINUE;
                }
                
            });
        } catch (IOException e) {
            logger.warn().append("Cannot clean-up the folder ").append(root).append(" from the temporary files.").append(e).commit();
        }
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

        if (logger.isDebugEnabled())
            logger.debug("[%s] %s > %s").with(getClass().getSimpleName()).with(event).with(path);

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
                        
                        if (watchFs) {
                            try {
                                FileSystemWatcher.getInstance().subscribe(fsEventHandler, file, EventType.SCANNED, EventType.CREATED, EventType.MODIFIED, EventType.DELETED);
                            } catch (IOException e) {
                                logger.warn().append("An error while subscription to ").append(file).append(e).commit();
                            }
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

                    if (IOUtil.copiedCompletely(file)) {
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
                            logger.warn().append("An error while preparing item for ").append(path).append(t).commit();
                        }
                    }
                    break;
                case MODIFIED:

                    FileItem fItem = items.get(path);

                    final boolean isNew = fItem == null;
                    final boolean isEmpty = file.length() == 0;
                    final long lastModified = file.lastModified();

                    if (isNew) {
                        if (!isEmpty) {
                            if (IOUtil.copiedCompletely(file)) {
                                // created
                                try {
                                    fItem = new FileItem(file, prepareItem(file));
                                    items.put(path, fItem);

                                    for (RepositoryEventHandler<T> handler : getHandlers(SCMDRepositoryEvent.CREATED)) {
                                        handler.onEvent(fItem.item, SCMDRepositoryEvent.CREATED);
                                    }
                                } catch (Throwable t) {
                                    logger.warn().append("An error while preparing item for ").append(path).append(t).commit();
                                }
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
                                logger.warn().append("An error while preparing item for ").append(path).append(t).commit();
                            }
                        } else if (lastModified != fItem.lastModified) {
                            // modified
                            if (IOUtil.copiedCompletely(file)) {
                                try {
                                    fItem.lastModified = lastModified;

                                    fItem.item = prepareItem(file);

                                    for (RepositoryEventHandler<T> handler : getHandlers(SCMDRepositoryEvent.MODIFIED)) {
                                        handler.onEvent(fItem.item, SCMDRepositoryEvent.MODIFIED);
                                    }
                                } catch (Throwable t) {
                                    logger.warn().append("An error while preparing item for ").append(path).append(t).commit();
                                }
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
        
        if (watchFs) {
            try {
                FileSystemWatcher.getInstance().subscribe(fsEventHandler, root, EventType.SCANNED, EventType.CREATED, EventType.MODIFIED, EventType.DELETED);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void stop() throws IOException {
        super.stop();

        if (watchFs) {
            FileSystemWatcher.getInstance().unsubscribe(fsEventHandler);
        }
    }
    
    protected boolean isSubscribableFolder(File folder) {
        return false;
    }
    
    protected abstract boolean isItemFile(File file);
    
    protected abstract T prepareItem(File file) throws IOException;
    
    protected class FileItem {
        private long        lastModified;
        private T           item;        

        private FileItem(File itemFile, T item) {
            lastModified = itemFile.lastModified();
            this.item = item;
        }         
    }
}
