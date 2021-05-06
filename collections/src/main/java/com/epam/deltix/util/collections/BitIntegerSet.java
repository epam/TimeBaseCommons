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
