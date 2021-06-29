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

/**
 *
 */
public class SingleElementIntegerSet implements ReadOnlyIntegerSet {
    private class EnumImpl implements IntegerEnumeration {
        private boolean     mHasMore = true;
        
        public int          nextIntElement () {
            mHasMore = false;
            return (mElement);
        }

        public Integer      nextElement () {
            return (nextIntElement ());
        }

        public boolean      hasMoreElements () {            
            return (mHasMore);
        }

        @Override
        public void reset() {
            mHasMore = true;
        }
    }
    
    private int         mElement;
    
    public SingleElementIntegerSet (int element) {
        mElement = element;
    }

    public boolean              contains (int value) {
        return (mElement == value);
    }

    public int []               toIntArray () {
        return (new int [] { mElement });
    }

    public int                  size () {
        return (1);
    }

    public boolean              isEmpty () {
        return (false);
    }

    public IntegerEnumeration   elements () {
        return (new EnumImpl ());
    }
    
    
}