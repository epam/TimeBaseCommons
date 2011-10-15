package deltix.qsrv.hf.tickdb.lang.pub;

/**
 *
 */
public abstract class Location {
    public static final int     NONE = 0xFFFF;
    
    public static int           getStartLine (long location) {
        return ((int) (location >>> 48));
    }

    public static int           getEndLine (long location) {
        return ((int) ((location >>> 16) & 0xFFFF));
    }

    public static int           getStartPosition (long location) {
        return ((int) ((location >>> 32) & 0xFFFF));
    }

    public static int           getEndPosition (long location) {
        return ((int) (location & 0xFFFF));
    }

    public static long          getStart (long location) {
        return (location >>> 32);
    }

    public static long          getEnd (long location) {
        return (location & 0xFFFFFFFFL);
    }

    public static long          combine (long fromPos, long toPos) {
        return (fromPos << 32 | toPos);
    }
    
    public static long          fromTo (long fromLocation, long toLocation) {
        return ((fromLocation & 0xFFFFFFFF00000000L) | (toLocation & 0xFFFFFFFF));
    }
}
