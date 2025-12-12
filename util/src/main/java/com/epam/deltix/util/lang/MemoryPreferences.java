package com.epam.deltix.util.lang;

import java.util.*;
import java.util.prefs.*;

/**
 * Transient in-memory Preferences implementation.
 * All of the preferences will be lost on application restart.
 *
 * @see java.util.prefs.Preferences
 */
public final class MemoryPreferences extends AbstractPreferences {

    public static class Factory implements PreferencesFactory {
        private MemoryPreferences   sys = new MemoryPreferences (false);
        private MemoryPreferences   user = new MemoryPreferences (true);

        public Preferences      systemRoot () {
            return (sys);
        }

        public Preferences      userRoot () {
            return (user);
        }
    }

    private static final String []      EMPTY_ARRAY = {};

    private final boolean               isUser;
    private final Map <String, String>  map = new HashMap <String, String> ();

    MemoryPreferences (boolean isUser) {
        super (null, "");
        this.isUser = isUser;
    }

    private MemoryPreferences (MemoryPreferences parent, String name) {
        super (parent, name);
        this.isUser = parent.isUser;
    }

    @Override
    public boolean          isUserNode () {
        return isUser;
    }

    @Override
    protected String []     keysSpi () throws BackingStoreException {
        return map.keySet ().toArray (new String [map.size()]);
    }

    @Override
    protected String        getSpi (String key) {
        return map.get(key);
    }

    @Override
    protected void          removeSpi (String key) {
        map.remove(key);
    }

    @Override
    protected void          putSpi (String key, String value) {
        map.put(key, value);
    }

    @Override
    protected String []     childrenNamesSpi () throws BackingStoreException {
        return EMPTY_ARRAY;
    }

    @Override
    protected AbstractPreferences childSpi (String name) {
        return new MemoryPreferences (this, name);
    }

    @Override
    protected void          removeNodeSpi () throws BackingStoreException {
    }

    @Override
    protected void          flushSpi () throws BackingStoreException {
    }

    @Override
    protected void          syncSpi () throws BackingStoreException {
    }
}
