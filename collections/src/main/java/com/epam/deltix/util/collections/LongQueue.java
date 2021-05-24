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


/**
 * Fixed size circular buffer of long values
 */
public class LongQueue {
    private final int           mCapacity;
    private int                 mSize = 0;
    private int                 mHead = 0;
    private int                 mTail = 0;
    private final long []       mBuffer;
    
    public LongQueue (int capacity) {
        mCapacity = capacity;
        mBuffer = new long [capacity];
    }
    
    public void                 offer (long value) {
        assert mSize < mCapacity;
        
        mBuffer [mTail] = value;
        
        mSize++;
        mTail++;
        
        if (mTail == mCapacity)
            mTail = 0;                
    }
    
    public long               youngest () {
        assert mSize > 0;
        
        return (mBuffer [mTail]);
    }
    
    public long               oldest () {
        assert mSize > 0;
        
        return (mBuffer [mHead]);
    }
    
    public long               poll () {
        assert mSize > 0;
        
        long  value = mBuffer [mHead];
        
        mSize--;
        mHead++;
        
        if (mHead == mCapacity)
            mHead = 0;
        
        return (value);
    }
    
    public void                 clear () {
        mSize = 0;
        mHead = 0;
        mTail = 0;
    }
    
    public final boolean        isEmpty () {
        return (mSize == 0);
    }
    
    public final boolean        isFull () {
        return (mSize == mCapacity);
    }
    
    public final int            size () {
        return (mSize);
    }  
    
    public final int            capacity () {
        return (mCapacity);
    }  
}