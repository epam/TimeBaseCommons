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
 * Fixed size circular buffer of double values
 */
public class DoubleQueue {
    private final int           mCapacity;
    private int                 mSize = 0;
    private int                 mHead = 0;
    private int                 mTail = 0;
    private final double []     mBuffer;
    
    public DoubleQueue (int capacity) {
        mCapacity = capacity;
        mBuffer = new double [capacity];
    }
    
    public void                 offer (double value) {
        assert mSize < mCapacity;
        
        mBuffer [mTail] = value;
        
        mSize++;
        mTail++;
        
        if (mTail == mCapacity)
            mTail = 0;                
    }
    
    public double               youngest () {
        assert mSize > 0;
        
        return (mBuffer [mTail]);
    }
    
    public double               oldest () {
        assert mSize > 0;
        
        return (mBuffer [mHead]);
    }
    
    public double               poll () {
        assert mSize > 0;
        
        double  value = mBuffer [mHead];
        
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