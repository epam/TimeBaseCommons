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
 * Warning: It's recommenced to use {@link CharSeqToObjMap} or {@link CharSequenceToObjectMapQuick}
 * if you do "get()" operations using {@link StringBuilder} or {@link String}.
 * <p>
 * If you always use this map with {@link String} keys only, use {@link HashMap} directly to avoid performance penalty on {@link #get} calls.
 * <p>
 * Uses {@link HashMap} as backend.
 * All keys are converted to {@link String} on insertion.
 * <p>
 * Works correctly because on each lookup key is wrapped intp {@link CharSubSequence}
 * which computes hash code and equality based on content of the provided {@link CharSequence}
 * and not on implementation of {@link String#equals(Object)}.
 * <p>
 * Pros:
 * <ul>
 *     <li>No need to care about immutability of keys</li>
 *     <li>Has most of the methods of {@link HashMap} (however it's not guarantied for all of them to work,
 *     as only few methods are overridden)</li>
 * </ul>
 * <p>
 * Cons:
 * <ul>
 *     <li>Each insertion of non-String key allocates a new {@link String} object.
 *     So if you expect to remove and insert same key later, consider using {@link CharSeqToObjMap} instead.</li>
 *
 *     <li>Implementation have to use {@link CharSubSequence#equals(Object)} for comparison of keys
 *     with {@link CharSubSequence} as one argument and {@link String} as another.
 *     This way it's impossible to use fast comparison between stored {@link String} and lookup key of {@link String}
 *     or {@link StringBuilder} type.</li>
 *
 *     <li>Some base methods of {@link HashMap} are not overridden,
 *     so if you use them with non-String keys, they will not work correctly.</li>
 * </ul>
 */
public class CharSequenceToObjectMap <T> extends HashMap <String, T> {
    protected final CharSubSequence mBuffer = new CharSubSequence();
    
    public CharSequenceToObjectMap (int initialCapacity, float loadFactor) {
        super (initialCapacity, loadFactor);
    }
    
    public CharSequenceToObjectMap (int initialCapacity) {
        super (initialCapacity);
    }
    
    public CharSequenceToObjectMap () {
        super ();
    }
    
    @Override
    public final T              get (Object key) {
        return (get ((CharSequence) key));
    }

    public final T              get (CharSequence key) {
        mBuffer.set (key);        
        return (super.get (mBuffer));
    }
    
    public final T              get (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);        
        return (super.get (mBuffer));
    }
    
    public T                    put (CharSequence key, T value) {
        return super.put (key.toString (), value);
    }        
    
    public T                    put (CharSequence key, int start, int end, T value) {
        return super.put (key.subSequence (start, end).toString (), value);
    }

    @Override
    public boolean              containsKey (Object key) {
        mBuffer.set ((CharSequence) key);
        return super.containsKey (mBuffer);
    }

    public boolean              containsKey (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return super.containsKey (mBuffer);
    }

    public boolean              containsKey (CharSequence key) {
        mBuffer.set (key);
        return super.containsKey (mBuffer);
    }

    @Override
    public T                    remove (Object key) {
        mBuffer.set ((CharSequence) key);
        return super.remove (mBuffer);
    }

    public T                    remove (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return super.remove (mBuffer);
    }

    public T                    remove (CharSequence key) {
        mBuffer.set (key);
        return super.remove (mBuffer);
    }
}