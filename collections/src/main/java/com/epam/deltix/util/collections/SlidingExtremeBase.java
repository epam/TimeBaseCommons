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
 * Base class for finding online extreme of a sliding window of arbitrary
 * comparable objects. See also 
 * <a href="http://www.tiac.net/~cri/2001/slidingmin.html">http://www.tiac.net/~cri/2001/slidingmin.html</a>
 */
abstract class SlidingExtremeBase {
    private final int       windowWidth;
    private final long []   ageOfDeath;
    private long            globalCount = 0;
    private int             extreme = -1;
    private int             last = -1;
    
    protected SlidingExtremeBase (int windowWidth) {  
        this.windowWidth = windowWidth;
                
        ageOfDeath = new long [windowWidth];        
    }

    private int             next (int n) {
        n++;

        if (n == windowWidth)
            n = 0;

        return (n);
    }

    private int             prev (int n) {
        return (n == 0 ? windowWidth - 1 : n - 1);
    }

    public final boolean    isEmpty () {
        return (globalCount == 0);
    }
    
    public void             clear () {
        //minima [0] = 0;
        globalCount = 0;
        extreme = -1;
        last = -1;
    }

    protected final int     getIndexOfExtreme () {
        assert globalCount > 0;
        
        return (extreme);
    }
    
    protected abstract boolean  isCurrentValueNoLessExtremeThanAt (int idx);
    
    protected String        printValueAt (int idx) {
        return ("<?>");
    }
    
    /**
     *  @return  The index at which the value should be written into the
     *              queue storage.
     */
    protected int           onOffer () {
        if (globalCount == 0) {
            extreme = 0;
            last = 0;
            globalCount = 1;
            ageOfDeath [0] = windowWidth + 1;
            return (0);
        }
        
        assert globalCount < ageOfDeath [extreme];
        
        globalCount++;
        
        //  If time for the tail to die, remove it.
        if (globalCount == ageOfDeath [extreme]) 
            extreme = next (extreme);

        if (isCurrentValueNoLessExtremeThanAt (extreme)) {
            //  
            //  New extreme is found. Collapse the queue
            //
            extreme = 0;
            last = 0;            
        }
        else {
            while (isCurrentValueNoLessExtremeThanAt (last))
                last = prev (last);

            last = next (last);
        }
        
        ageOfDeath [last] = globalCount + windowWidth;
        
        return (last);
    }
    
    public final long           getCount () {
        return (globalCount);
    }
    
    public final void         dump () {
        if (globalCount == 0) {
            System.out.printf (
                "Width: %,d; EMPTY\n",
                windowWidth
            );
            return;            
        }
        
        System.out.printf (
            "Width: %,d; Count: %,d; Extreme (tail): [%,d]; Last (head): [%,d]\n",
            windowWidth,
            globalCount,
            extreme,
            last
        );
                
        for (int ii = extreme, n = 0; ; ii = next (ii), n++) {
            System.out.printf (
                "[%,d] #%,d: %s; death at %,d\n",
                ii, n, printValueAt (ii), ageOfDeath [ii]
            );
            
            if (ii == last)
                break;
        }
    }
        
}