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

import java.io.IOException;
import java.util.function.BiConsumer;

import com.epam.deltix.util.collections.generated.ObjectToObjectHashMap;
import com.epam.deltix.util.collections.hash.StringHashCodeComputer;

/**
 * Similar to {@link CharSeqToObjMap} but takes care about immutability of stored keys
 * by always storing them as {@link String} (with allocation).
 * <p>
 * This map is good for cases when you never or very rarely delete keys from the map or do not care about allocations.
 * <p>
 * It's recommended to use {@link CharSeqToObjMap} if you often remove and add keys and want to avoid
 * allocation overhead.
 * <p>
 * Pros:
 * <ul>
 *     <li>No need to care about immutability of inserted keys</li>
 *     <li>Performs fast key comparison for lookup keys of {@link String} and {@link StringBuilder} type
 *     (using {@link String#contentEquals(CharSequence)}).</li>
 * </ul>
 * <p>
 * Cons:
 * <ul>
 *     <li>Each insertion of non-String key allocates a new {@link String} object.
 *     So if you expect to remove and insert same key later, consider using {@link CharSeqToObjMap} instead.</li>
 * </ul>
 */
public class CharSequenceToObjectMapQuick <T> extends ObjectToObjectHashMap <CharSequence, T> {
    private transient CharSubSequence     mBuffer = new CharSubSequence ();
    
    public CharSequenceToObjectMapQuick (int initialCapacity) {
        super (initialCapacity, StringHashCodeComputer.INSTANCE);
    }
    
    public CharSequenceToObjectMapQuick () {
        super (StringHashCodeComputer.INSTANCE);
    }
    
    //  The following 2 overrides make all other methods work:
    @Override
    protected void          putKey (int pos, CharSequence key) {
        // Key is always stored as String
        super.putKey (pos, key.toString ());
    }

    @Override
    protected boolean       keyEquals (CharSequence searchValue, CharSequence storedKey) {
        // Second argument is always String.
        // This call utilizes String's optimized content comparison method that takes
        //  advantage of access to internal coder and value byte array.
        return ((String) storedKey).contentEquals(searchValue);
    }
    
    public final T              get (CharSequence key, int start, int end, T notFoundValue) {
        mBuffer.set (key, start, end);        
        return (super.get (mBuffer, notFoundValue));
    }
    
    public T                    putAndGet (CharSequence key, int start, int end, T value, T notFoundValue) {
        mBuffer.set (key, start, end);  
        return super.putAndGet (mBuffer, value, notFoundValue);
    }

    public boolean              containsKey (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return super.containsKey (mBuffer);
    }

    public T                    remove (CharSequence key, int start, int end, T notFoundValue) {
        mBuffer.set (key, start, end);
        return super.remove (mBuffer, notFoundValue);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        mBuffer = new CharSubSequence();
    }

    @SuppressWarnings("unchecked")
    @Override
    public KVEnumeration<String, T> elements () {
        return (KVEnumeration<String, T>) super.elements();
    }

    /** Iterate over key-value pairs */
    @SuppressWarnings("unchecked")
    public void forEach(BiConsumer<String, ? super T> consumer) {
        for (int i = 0; i < values.length; i++) {
            if (isFilled (i)) {
                // Stored keys are always String
                consumer.accept((String) keys[i], (T) values[i]);
            }
        }
    }
}