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

import com.epam.deltix.util.collections.generated.IntegerEnumeration;

import java.util.*;

/**
 *  Adapts the BitSet class to the {@link MutableIntegerSet} interface.
 */
public class BitIntegerSet extends BitSet implements MutableIntegerSet {
    public BitIntegerSet () {
        super ();
    }
    
    public BitIntegerSet (int capacity) {
        super (capacity);
    }
    
    private class BitEnumeration implements IntegerEnumeration {
        private int             mCurrentBit;
        
        BitEnumeration () {
            mCurrentBit = nextSetBit (0);
        }
        
        public int              nextIntElement () {
            assert hasMoreElements () : "!hasMoreElements ()";
            
            int         ret = mCurrentBit;
            
            mCurrentBit = nextSetBit (mCurrentBit + 1);
            
            return (ret);
        }

        public Integer          nextElement () {
            return (nextIntElement ());
        }

        public boolean          hasMoreElements () {
            return (mCurrentBit >= 0);
        }

        public void             reset() {
            mCurrentBit = nextSetBit (0);
        }
    }
    
    public int []                   toIntArray () {
        final int       count = size ();
        final int []    ret = new int [count];
        int             pos = 0;
        
        for (int ii = nextSetBit (0); ii >= 0; ii = nextSetBit (ii + 1))
            ret [pos++] = ii;
        
        assert pos == count : "pos: " + pos + "; count: " + count;
        return (ret);
    }
    
    public boolean                  remove (int value) {
        boolean     found = get (value);
        
        if (found)
            clear (value);
        
        return (found);
    }

    public boolean                  contains (int value) {
        return (get (value));
    }

    public boolean                  add (int value) {
        boolean     notFound = !get (value);
        
        if (notFound)
            set (value);
            
        return (notFound);
    }

    public void                     addAll (IntegerEnumeration e) {
        while (e.hasMoreElements ()) 
            add (e.nextIntElement ()); 
    }
    
    public IntegerEnumeration       elements () {
        return (new BitEnumeration ());
    }

    public int                      size () {
        return (super.cardinality ());
    }    
}