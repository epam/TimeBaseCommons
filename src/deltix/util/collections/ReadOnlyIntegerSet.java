package deltix.util.collections;

import deltix.util.collections.generated.IntegerEnumeration;

/**
 *
 */
public interface ReadOnlyIntegerSet {
    public boolean              isEmpty ();
    
    public IntegerEnumeration   elements ();
    
    /**
     *  Determines if the specified value is in the set
     */
    public boolean              get (int value);
}
