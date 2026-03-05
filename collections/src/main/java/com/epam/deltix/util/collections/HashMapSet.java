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
package com.epam.deltix.util.collections;

import java.util.*;

/**
 *  A set of objects indexed by a key contained in each object.
 */
public abstract class HashMapSet <K, V>
    extends HashSet <V>
    implements MapSet <K, V> 
{
    private HashMap <K, V>          keyMap;
    private boolean                 keyMapUpToDate = true;
    private boolean                 delayMapUpdate = true;
    
    public HashMapSet () {
        this (16);
    }
    
    public HashMapSet (int capacity) {
        super (capacity);
        keyMap = new HashMap <K, V> (capacity);
    }
    
    private HashMap <K, V>      getKeyMap () {
        if (!keyMapUpToDate) {
            keyMap.clear ();
            
            for (V v : this)
                keyMap.put (getKey (v), v);
        }
        
        return (keyMap);
    }
    
    private void                invalidateKeyMap () {
        keyMapUpToDate = false;
        keyMap.clear ();
    }
    
    public HashMapSet (Collection <V> copy) {
        this (copy.size ());        
        addAll (copy);
    }
    
    public abstract K           getKey (V value);
    
    //  MapSet own methods
    public boolean              containsKey (K key) {
        return (getKeyMap ().containsKey (key));
    }

    public boolean              containsKeyOf (V value) {
        return (containsKey (getKey (value)));
    }
    
    public V                    get (K key) {
        return (getKeyMap ().get (key));
    }

    public Set <K>              keySet () {
        return (getKeyMap ().keySet ());
    }

    public V                    removeKey (K key) {
        final V             v = getKeyMap ().remove (key);
        
        if (v != null)
            super.remove (v);
        
        return (v);
    }

    public void                 keyChanged (V value) {
        if (delayMapUpdate) 
            invalidateKeyMap ();
        else {
            final V             v = keyMap.put (getKey (value), value);

            if (v != value)
                throw new RuntimeException (value + " was not in the map");
        }
    }
    
    public <T extends K> T []   keysToArray (T [] a) {
        return (getKeyMap ().keySet ().toArray (a));
    }
    
    //  Hooks into Set methods
    @Override
    public boolean              add (V v) {
        if (delayMapUpdate) {
            invalidateKeyMap ();
            return (super.add (v));
        }
        else {
            final K             key = getKey (v);

            if (containsKey (key)) 
                if (contains (v))
                    return (false);
                else
                    throw new IllegalStateException (
                        "Key was in, but object was out: " + key
                    );
            else if (super.add (v)) {
                keyMap.put (key, v);
                return (true);
            }
            else
                throw new IllegalStateException ("Key was out, but object was in: " + key);
        }
    }

    @Override @SuppressWarnings ("unchecked")
    public boolean              remove (Object o) {
        final V             v = (V) o;
        
        if (delayMapUpdate) {            
            invalidateKeyMap ();
            return (super.remove (v));
        }
        else {
            final K             key = getKey (v);
            final V             removed = keyMap.remove (key);

            if (removed != null) {            
                if (!super.remove (o))
                    throw new IllegalStateException ("Key was in, but object was out: " + key);

                if (!removed.equals (o))
                    throw new IllegalStateException ("Key was in, but object was different: " + key);

                return (true);
            }
            else if (contains (v))
                throw new IllegalStateException ("Key was out, but object was in: " + key);
            else
                return (false);
        }
    }

    @Override
    public void                 clear () {
        super.clear ();
        keyMap.clear ();
        keyMapUpToDate = true;
    }

    @Override @SuppressWarnings ("unchecked")
    public Object               clone () {        
        HashMapSet <K, V> newSet = (HashMapSet <K, V>) super.clone ();
        newSet.keyMap = new HashMap <K, V> (size ());
        newSet.keyMapUpToDate = false;
        return newSet;
    }   
}