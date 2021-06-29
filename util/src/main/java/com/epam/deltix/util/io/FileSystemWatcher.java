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
package com.epam.deltix.util.io;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.lang.Util;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Allows to scan and watch the file system folders.</p>
 * An example of using:
 * <pre>
 *    final FileSystemWatcher.EventHandler h = new FileSystemWatcher.EventHandler() {
 *           {@literal @}Override
 *           public void onEvent(File file, FileSystemWatcher.EventType event) {
 * 
 *               System.out.println(file + " " + event);
 *               
 *               // subscribe again for recursive monitoring
 *               if (file.isDirectory() &amp;&amp;
 *                      (event == FileSystemWatcher.EventType.SCANNED ||
 *                       event == FileSystemWatcher.EventType.CREATED)) {
 *                     try {
 *                         FileSystemWatcher.getInstance().subscribe(this, file, FileSystemWatcher.EventType.SCANNED, FileSystemWatcher.EventType.CREATED, FileSystemWatcher.EventType.MODIFIED, FileSystemWatcher.EventType.DELETED);
 *                     } catch (IOException e) {                            
 *                         e.printStackTrace();
 *                     }
 *               }
 *           }
 *       };
 * 
 *       FileSystemWatcher.getInstance().subscribe(h, new java.io.File("/home/user/projects/qs/main/custom"), FileSystemWatcher.EventType.SCANNED, FileSystemWatcher.EventType.CREATED, FileSystemWatcher.EventType.MODIFIED, FileSystemWatcher.EventType.DELETED);
 *       
 *       // ...do something...
 *       
 *       FileSystemWatcher.getInstance().unsubscribe(h); 
 * </pre>
 */
public class FileSystemWatcher {
    public enum EventType {
        /**
         * A file/folder exists in the target folder by the moment of subscription.
         */
        SCANNED,
        
        /**
         * A new file/folder is created in the target folder.
         */
        CREATED,
        
        /**
         * A file/folder is modified in the target folder.
         */
        MODIFIED,
        
        /**
         * A file/folder is deleted from the target folder.
         */        
        DELETED;
        
        public boolean isInto(EventType... events) {
            for (EventType event : events) {
                if (this == event) {
                    return true;
                }
            }
            return false;
        }
        
        private static WatchEvent.Kind[] toWatchEventKind(EventType... events) {
            final List<WatchEvent.Kind> result = new ArrayList<>();
            for (EventType event : events) {
                switch (event) {
                    case CREATED:
                        result.add(StandardWatchEventKinds.ENTRY_CREATE);
                        break;
                    case MODIFIED:
                        result.add(StandardWatchEventKinds.ENTRY_MODIFY);
                        break;
                    case DELETED:
                        result.add(StandardWatchEventKinds.ENTRY_DELETE);
                        break;
                }
            }
            return result.toArray(new WatchEvent.Kind[result.size()]);
        }

        private static EventType toEventType(WatchEvent.Kind event) {
            switch (event.name().charAt(6)) {
                case 'C': //StandardWatchEventKinds.ENTRY_CREATE:
                    return CREATED;
                case 'M': //StandardWatchEventKinds.ENTRY_MODIFY:
                    return MODIFIED;
                case 'D': //StandardWatchEventKinds.ENTRY_DELETE:
                    return DELETED;
            }
            return null;
        }        
    }
    
    public interface EventHandler {
        void onEvent(File file, EventType event);
    }

    private final static FileSystemWatcher      INSTANCE = new FileSystemWatcher();
    private final static Log LOGGER = LogFactory.getLog(FileSystemWatcher.class.getName());
    
    public static FileSystemWatcher getInstance() {
        return INSTANCE;
    }
    
    private final Object                        lock = new Object();
    private WatchingThread                      watcher;
    
    private FileSystemWatcher() {                 
    }
    
    public synchronized void subscribe(EventHandler handler, File folder, EventType... events) throws IOException {
        if (!folder.exists()) {
            throw new IOException(folder + " doesn't exist.");
        }

        if (!folder.isDirectory()) {
            throw new IOException(folder + " isn't a folder.");
        }
        
        if (watcher == null) {
            watcher = new WatchingThread();
            watcher.start();
        }
                
        watcher.subscribe(handler, folder, events);
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
                Thread.currentThread().interrupt();
            }
            watcher = null;
        }        
    }   
    
    public Object getLock() {
        return lock;
    }
    
    private class WatchingThread extends Thread {
        
        private final WatchService                          watcher;
        private final Map<Path, List<EventHandler>>         pathMap = new HashMap<>();
        private final Map<WatchKey, Path>                   keyMap = new HashMap<>();
        
        private WatchingThread() {
            super(FileSystemWatcher.class.getSimpleName());
            
            setDaemon(true);
            
            try {
                //System.out.println("Preparing WatchingThread");
                watcher = FileSystems.getDefault().newWatchService();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }                        
        }

        private boolean isEmpty() {
            synchronized (lock) {
                return pathMap.isEmpty();
            }
        }
        
        private void subscribe(final EventHandler handler, final File folder, EventType... events) throws IOException {
                        
            final Path path = folder.toPath();
            final WatchEvent.Kind[] stdEvents = EventType.toWatchEventKind(events);
            
            synchronized (lock) {            
                
                if (stdEvents.length > 0) {

                    final boolean newPath = !pathMap.containsKey(path);

                    final List<EventHandler> pathLtns;
                    if (newPath) {
                        pathLtns = new ArrayList<>();
                        pathMap.put(path, pathLtns);
                    } else {
                        pathLtns = pathMap.get(path);
                    }

                    if (!pathLtns.contains(handler)) {
                        pathLtns.add(handler);
                    }

                    if (newPath) {
                        keyMap.put(path.register(watcher, stdEvents), path);
                    }
                }
                
                if (EventType.SCANNED.isInto(events)) {
                    for (File file : folder.listFiles()) {
                        handler.onEvent(file, EventType.SCANNED);
                    }
                }
            }                        
        }

        private void unsubscribe(EventHandler handler) {                        
            synchronized (lock) {                
                
                final List<Path> paths = new ArrayList<>();

                for (Map.Entry<Path, List<EventHandler>> kv : pathMap.entrySet()) {
                    if (!kv.getValue().contains(handler)) {
                        continue;
                    }
                    for (Map.Entry<WatchKey, Path> kv2 : keyMap.entrySet()) {
                        if (!kv2.getValue().toAbsolutePath().equals(kv.getKey())) {
                            continue;
                        }
                        paths.add(kv2.getValue());
                    }
                }

                for (Path path : paths) {

                    final List<EventHandler> pathLtns = pathMap.get(path);
                    if (pathLtns == null) {
                        continue;
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

                        pathMap.remove(path);
                    }
                }
            }
        }        
                
        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            while (!isInterrupted()) {

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
                    
                    final EventType eventType = EventType.toEventType(kind);
                    if (eventType == null) {
                        continue;
                    }

                    final WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    final Path file = ev.context();

                    synchronized (lock) {
                        
                        final Path path = keyMap.get(key);
                        if (path == null) {
                            continue;
                        }
                        
                        for (EventHandler handler : pathMap.get(path.toAbsolutePath())) {
                            try {
                                handler.onEvent(path.resolve(file).toFile(), eventType);
                            } catch (Throwable t) {
                                LOGGER.warn().append("An error while event processing.").append(t).commit();
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