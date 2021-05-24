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

import com.epam.deltix.util.collections.generated.*;
import com.epam.deltix.util.collections.hash.*;
import com.epam.deltix.util.lang.*;

public class CharSequenceToObjectMapQuick <T> extends ObjectToObjectHashMap <CharSequence, T> {
    private transient CharSubSequence     mBuffer = new CharSubSequence ();
    
    public CharSequenceToObjectMapQuick (int initialCapacity) {
        super (initialCapacity, StringHashCodeComputer.INSTANCE);
    }
    
    public CharSequenceToObjectMapQuick () {
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
}