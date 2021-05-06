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
