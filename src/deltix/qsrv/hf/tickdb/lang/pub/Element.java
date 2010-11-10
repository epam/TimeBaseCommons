package deltix.qsrv.hf.tickdb.lang.pub;

/**
 *
 */
public abstract class Element {
    public static final long    NO_LOCATION = 0;
    
    public final long           location;

    protected Element (long location) {
        this.location = location;
    }

    /**
     *  Convert the element to correct QQL. This method is used for
     *  serialization.
     */
    public abstract void        print (StringBuilder s);

    public final int            getStartLine () {
        return (Location.getStartLine (location));
    }

    public final int            getEndLine () {
        return (Location.getEndLine (location));
    }

    public final int            getStartPosition () {
        return (Location.getStartPosition (location));
    }

    public final int            getEndPosition () {
        return (Location.getEndPosition (location));
    }

    /**
     *  Convert the element to correct QQL. This method is used for
     *  serialization. This method calls {@link #print}.
     */
    @Override
    public final String         toString () {
        StringBuilder               sb = new StringBuilder ();

        print (sb);

        return (sb.toString ());
    }
}
