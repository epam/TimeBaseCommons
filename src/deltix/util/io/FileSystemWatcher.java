package deltix.util.io;

import deltix.util.lang.Util;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSystemWatcher {
    
    public interface EventHandler {
        void onEvent(Path path, Path file, WatchEvent.Kind event);
    }

    private final static FileSystemWatcher      INSTANCE = new FileSystemWatcher();
    private final static Logger                 LOGGER = Logger.getLogger(FileSystemWatcher.class.getName());
    
    public static FileSystemWatcher getInstance() {
        return INSTANCE;
    }
    
    private WatchingThread                      watcher;
    
    private FileSystemWatcher() {                 
    }
    
    public void subscribe(EventHandler handler, Path path, WatchEvent.Kind... events) throws IOException {
        subscribe(handler, path, false, events);
    }
    
    public synchronized void subscribe(EventHandler handler, Path path, boolean recursively, WatchEvent.Kind... events) throws IOException {
        if (!path.isAbsolute() ||
                !path.toFile().exists() ||
                !path.toFile().isDirectory()) {
            throw new IOException(path + " isn't a folder.");
        }
        
        if (watcher == null) {
            watcher = new WatchingThread();
            watcher.start();
        }
                
        watcher.subscribe(handler, path, recursively, events);
    }

    public synchronized void unsubscribe(EventHandler handler) throws IOException {        
        if (watcher == null) {
            return;
        }
        
        watcher.unsubscribe(handler);
        
        if (watcher.isEmpty()) {
            watcher.interrupt();
            try {
                watcher.join();
            } catch (InterruptedException e) {                
            }
            watcher = null;
        }        
    }   
    
    private class WatchingThread extends Thread {
        
        private final WatchService                          watcher;
        private final Map<String, List<EventHandler>>       pathMap = new HashMap<>();
        private final Map<WatchKey, Path>                   keyMap = new HashMap<>();
                
        private WatchingThread() {
            super(FileSystemWatcher.class.getSimpleName());
            
            setDaemon(true);
            
            try {
                watcher = FileSystems.getDefault().newWatchService();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }                        
        }

        private boolean isEmpty() {
            synchronized (pathMap) {
                return pathMap.isEmpty();
            }
        }
        
        private void subscribe(EventHandler handler, final Path startPath, boolean recursively, WatchEvent.Kind... events) throws IOException {
                        
            synchronized (pathMap) {
            
                final List<Path> paths = new ArrayList<>();              
                paths.add(startPath);
                
                if (recursively) {
                    Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {

                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                                throws IOException {
                            if (!dir.equals(startPath) && attrs.isDirectory()) {
                                paths.add(dir);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });                    
                }

                for (Path path : paths) {
                    
                    final String pathStr = path.toAbsolutePath().toString();

                    final boolean newPath = !pathMap.containsKey(pathStr);

                    final List<EventHandler> pathLtns;
                    if (newPath) {
                        pathLtns = new ArrayList<>();
                        pathMap.put(pathStr, pathLtns);
                    } else {
                        pathLtns = pathMap.get(pathStr);

                        if (pathLtns.contains(handler)) {
                            throw new IOException(handler + " already subscribed to " + pathStr);
                        }
                    }

                    pathLtns.add(handler);

                    if (newPath) {
                        keyMap.put(path.register(watcher, events), path);
                    }
                }
            }                        
        }

        private void unsubscribe(EventHandler handler) {                        
            synchronized (pathMap) {                
                
                final List<Path> paths = new ArrayList<>();

                for (Map.Entry<String, List<EventHandler>> kv : pathMap.entrySet()) {
                    if (!kv.getValue().contains(handler)) {
                        continue;
                    }
                    for (Map.Entry<WatchKey, Path> kv2 : keyMap.entrySet()) {
                        if (!kv2.getValue().toAbsolutePath().toString().equals(kv.getKey())) {
                            continue;
                        }
                        paths.add(kv2.getValue());
                    }
                }

                for (Path path : paths) {

                    final String pathStr = path.toAbsolutePath().toString();

                    final List<EventHandler> pathLtns = pathMap.get(pathStr);
                    if (pathLtns == null) {
                        return;
                    }

                    pathLtns.remove(handler);

                    if (pathLtns.isEmpty()) {
                        for (Map.Entry<WatchKey, Path> kv : new HashMap<>(keyMap).entrySet()) {
                            if (!kv.getValue().equals(path)) {
                                continue;
                            }

                            final WatchKey key = kv.getKey();
                            key.cancel();
                            keyMap.remove(key);
                        }

                        pathMap.remove(pathStr);
                    }
                }
            }
        }        
        
        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            while (!isInterrupted()) {
                // wait for key to be signaled
                final WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    break;
                }
                                
                for (WatchEvent<?> event : key.pollEvents()) {
                    final WatchEvent.Kind<?> kind = event.kind();

                    // This key is registered only
                    // for ENTRY_CREATE events,
                    // but an OVERFLOW event can
                    // occur regardless if events
                    // are lost or discarded.
                    if (kind ==  StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    // The filename is the
                    // context of the event.
                    final WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    final Path file = ev.context();

                    synchronized (pathMap) {
                        
                        final Path path = keyMap.get(key);
                        if (path == null) {
                            continue;
                        }
                        
                        for (EventHandler handler : pathMap.get(path.toAbsolutePath().toString())) {
                            try {
                                handler.onEvent(path, file, kind);
                            } catch (Throwable t) {
                                LOGGER.log(Level.WARNING, "An error while event processing.", t);
                            }
                        }
                    }
                }
                
                key.reset();
            }

            Util.close(watcher);
        }
    }        
}
