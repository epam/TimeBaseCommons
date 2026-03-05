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
package com.epam.deltix.util.time;

/**
 *
 */
public final class FixedInterval extends Interval {
    private final long      mSize;
    
    /**
     *  Constructs a fixed interval from number of milliseconds. Guesses the
     *  underlying unit.
     */
    public FixedInterval (long size) {
        mSize = size;
    }
    
    /**
     *  Constructs a fixed interval from a number of units.
     */
    public FixedInterval (long numUnits, TimeUnit unit) {
        mSize = numUnits * unit.getSizeInMilliseconds ();
    }
    
    /**
     *  Returns the size of this interval in milliseconds.
     */
    public long                 getSizeInMilliseconds () {
        return (mSize);
    }
    
    @Override
    public TimeUnit             getUnit () {
        return (TimeUnit.getUnitForMilliseconds (mSize));
    }
    
    @Override
    public long                 getNumUnits () {
        return (mSize / getUnit ().getSizeInMilliseconds ());
    }

    @Override
    public long                 getNumUnits(TimeUnit unit) {
        return (mSize / unit.getSizeInMilliseconds ());
    }

    public FixedInterval        negate () {
        return (new FixedInterval (-mSize));
    }
    
    public FixedInterval        add (FixedInterval interval) {
        return (new FixedInterval (mSize + interval.mSize));
    }
    
    public FixedInterval        subtract (FixedInterval interval) {
        return (new FixedInterval (mSize - interval.mSize));
    }    
    
    @Override
    public int                  hashCode () {
        return ((int) (mSize ^ (mSize >>> 32)));
    }
    
    @Override
    public boolean              equals (Object o) {
        return (
            o instanceof FixedInterval &&
            ((FixedInterval) o).mSize == mSize
        );
    }
}