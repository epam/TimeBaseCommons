package deltix.qsrv.hf.tickdb.lang.pub;

/**
 *
 */
public abstract class Location {
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
}
