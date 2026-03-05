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
public class MonthlyInterval extends Interval {
    private final int           mNumMonths;
    
    /**
     * Constructs a MonthlyInterval from number of months.
     */
    public MonthlyInterval (int numMonths) {
        mNumMonths = numMonths;
    }
    
    /**
     *  Constructs a MonthlyInterval interval from a number of units.
     */
    public MonthlyInterval (int numUnits, TimeUnit unit) {
        mNumMonths = numUnits * unit.getSizeInMonths ();
    }
    
    /**
     *  Returns the number of months in this interval.
     */
    public int                  getNumberOfMonths () {
        return (mNumMonths);
    }
    
    @Override
    public TimeUnit             getUnit () {
        return (TimeUnit.getUnitForMonths (mNumMonths));
    }
    
    @Override
    public long                  getNumUnits () {
        return (mNumMonths / getUnit ().getSizeInMonths ());
    }

    @Override
    public long                 getNumUnits(TimeUnit unit) {
        return (mNumMonths / unit.getSizeInMonths ());
    }

    public MonthlyInterval       negate () {
        return (new MonthlyInterval (-mNumMonths));
    }
    
    public MonthlyInterval       add (MonthlyInterval interval) {
        return (new MonthlyInterval (mNumMonths + interval.mNumMonths));
    }
    
    public MonthlyInterval       subtract (MonthlyInterval interval) {
        return (new MonthlyInterval (mNumMonths - interval.mNumMonths));
    }
    
    @Override
    public int                  hashCode () {
        return (mNumMonths);
    }
    
    @Override
    public boolean              equals (Object o) {
        return (
            o instanceof MonthlyInterval &&
            ((MonthlyInterval) o).mNumMonths == mNumMonths
        );
    }    
}