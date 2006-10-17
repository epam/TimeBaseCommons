package deltix.util.memory;

/**
 *
 */
public abstract class EstimatorUtils implements MemorySizeEstimator {
    public static int       getSizeInMemory (byte [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length) / 8 * 8);
    }
    
    public static int       getSizeInMemory (boolean [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_BOOLEAN) / 8 * 8);
    }
    
    public static int       getSizeInMemory (short [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_SHORT) / 8 * 8);
    }
    
    public static int       getSizeInMemory (char [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_CHAR) / 8 * 8);
    }
    
    public static int       getSizeInMemory (int [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_INT) / 8 * 8);
    }
    
    public static int       getSizeInMemory (long [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_LONG) / 8 * 8);
    }
    
    public static int       getSizeInMemory (float [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_FLOAT) / 8 * 8);
    }
    
    public static int       getSizeInMemory (double [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_DOUBLE) / 8 * 8);
    }
    
    public static int       getSizeInMemory (MemorySizeEstimator obj) {
        return (obj == null ? 0 : obj.getSizeInMemory ());
    }
    
    public static int       getSizeInMemory (String s) {
        return (
            s == null ? 
                0 : 
                ((45 + s.length () * SIZE_OF_CHAR) / 8) * 8
        );
    }
    
    public static int       getSizeInMemory (Object obj) {
        if (obj == null)
            return (0);
        
        if (obj instanceof MemorySizeEstimator)
            return (getSizeInMemory ((MemorySizeEstimator) obj));
        
        if (obj instanceof String)
            return (getSizeInMemory ((String) obj));
        
        return (OBJECT_OVERHEAD);
     }
    
     public static int       getSizeInMemory (Object [] a) {
        if (a == null)
            return (0);
        
        int             used = (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_POINTER) / 8 * 8;
        
        for (int ii = 0; ii < a.length; ii++) 
            used += getSizeInMemory (a [ii]);
            
        return (used);
    }
    
}
