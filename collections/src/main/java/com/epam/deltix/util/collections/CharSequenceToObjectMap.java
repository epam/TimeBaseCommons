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
 *  
 */
public class CharSequenceToObjectMap <T> extends HashMap <String, T> {
    private CharSubSequence     mBuffer = new CharSubSequence ();
    
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