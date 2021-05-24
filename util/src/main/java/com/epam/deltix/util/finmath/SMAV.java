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
package com.epam.deltix.util.finmath;

import com.epam.deltix.util.collections.DoubleQueue;

/**
 *  Simple Moving Average with Variance
 */
public class SMAV {
    private final DoubleQueue   q;
    private double              mSum = 0;
    private double              mSumSquares = 0;
    
    public SMAV (int numPeriods) {
        q = new DoubleQueue (numPeriods);
    }

    public boolean      isFull () {
        return (q.isFull ());
    }
    
    public double       getSum () {
        return (mSum);
    }
    
    public double       getSumSquares () {
        return (mSumSquares);
    }
    
    public double       getLastRegisteredValue () {
        return (q.youngest ());
    }
    
    public double       getAverage () {
        return (mSum / q.size ());
    }
    
    public double       getVariance () {
        final double        n = q.size ();
        final double        mean = mSum / n;
        
        return (mSumSquares / n - mean * mean);
    }
    
    public double       getStdDev () {
        return (Math.sqrt (getVariance ()));
    }

    public double       update (double value) {
        register (value);
        return (getAverage ());
    }
        
    public void         register (double value) {
        if (q.isFull ()) {
            double  old = q.poll ();
            mSum -= old;
            mSumSquares -= old * old;
        }
        
        q.offer (value);
        mSum += value;
        mSumSquares += value * value;
    }

    public void         clear () {
        q.clear ();
        mSum = 0;
    }
}