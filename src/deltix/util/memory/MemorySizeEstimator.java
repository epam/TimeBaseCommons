package deltix.util.memory;

/**
 *  Implemented by objects that can estimate the size they occupy in memory.
 */
public interface MemorySizeEstimator {
    public static final int     OBJECT_OVERHEAD = 8;
    public static final int     ARRAY_OVERHEAD = 12;
    public static final int     SIZE_OF_BYTE = 1;
    public static final int     SIZE_OF_BOOLEAN = 1;
    public static final int     SIZE_OF_CHAR = 2;
    public static final int     SIZE_OF_SHORT = 2;
    public static final int     SIZE_OF_INT = 4;
    public static final int     SIZE_OF_LONG = 8;
    public static final int     SIZE_OF_FLOAT = 4;
    public static final int     SIZE_OF_DOUBLE = 8;
    public static final int     SIZE_OF_POINTER = 8;
    
    public int          getSizeInMemory ();
}
