package deltix.util.collections;

import deltix.util.collections.generated.IntegerEnumeration;

/**
 *  A read-only integer set.
 */
public interface ReadOnlyIntegerSet {
    public int                  size ();
    
    public boolean              isEmpty ();
    
    public IntegerEnumeration   elements ();
    
    /**
     *  Determines if the specified value is in the set
     */
    public boolean              contains (int value);
    
    public int []               toIntArray ();
}
