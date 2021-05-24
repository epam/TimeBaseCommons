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
package com.epam.deltix.util.lang;

/**
 *
 */
public class MixedGrowthPolicy implements GrowthPolicy {
    public double               incFactor;
    public long                 minIncrement;
    public long                 maxIncrement;
    
    public MixedGrowthPolicy (
        double               factor,
        long                 minIncrement,
        long                 maxIncrement
    )
    {
        if (factor <= 1)
            throw new IllegalArgumentException ("Illegal factor: " + factor);
        
        if (minIncrement < 0)
            throw new IllegalArgumentException ("Illegal minIncrement: " + minIncrement);
        
        if (maxIncrement < 2)
            throw new IllegalArgumentException ("Illegal maxIncrement: " + maxIncrement);
        
        this.incFactor = factor - 1;
        this.minIncrement = minIncrement;
        this.maxIncrement = maxIncrement;
    }
        
    public long     computeLength (long curLength, long minLength) {
        long                mixedIncrement =
            Math.max (
                minIncrement, 
                Math.min (
                    maxIncrement,
                    (long) (incFactor * curLength)
                )
            );
        
        return (Math.max (minLength, curLength + mixedIncrement));
    }

}