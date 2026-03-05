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

import com.epam.deltix.util.collections.generated.ObjectToLongHashMap;
import com.epam.deltix.util.collections.hash.*;
import com.epam.deltix.util.lang.*;

/**
 *  
 */
public class CharSequenceToLongMap extends ObjectToLongHashMap<CharSequence> {
    private final CharSubSequence     mBuffer = new CharSubSequence ();
    
    public CharSequenceToLongMap (int initialCapacity) {
        super (initialCapacity, StringHashCodeComputer.INSTANCE);
    }
    
    public CharSequenceToLongMap () {
        super (StringHashCodeComputer.INSTANCE);
    }

    //  The following 3 overrides make all other methods work:
    @Override
    protected void          putKey (int pos, CharSequence key) {
        super.putKey (pos, key.toString ());
    }

    @Override
    protected int           find (CharSequence key) {
        if (mBuffer != key)     // This check is critical for preserving range!
            mBuffer.set (key);
        
        return (super.find (mBuffer));
    }

    @Override
    protected boolean       keyEquals (CharSequence a, CharSequence b) {
        return (Util.equals (a, b));
    }
    
    public final long       get (CharSequence key, int start, int end, long notFoundValue) {
        mBuffer.set (key, start, end);
        return (super.get (mBuffer, notFoundValue));
    }

    public boolean          put (CharSequence key, int start, int end, long value) {
        mBuffer.set (key, start, end);
        return (super.put (mBuffer, value));
    }
    
    public boolean          containsKey (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return (super.containsKey (mBuffer));
    }

    public long              remove (CharSequence key, int start, int end, long notFoundValue) {
        mBuffer.set (key, start, end);
        return super.remove (mBuffer, notFoundValue);
    }
}