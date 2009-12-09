package deltix.qsrv.hf.tickdb.sql.lang.pub;

/**
 *
 */
public abstract class Element {
    public static final long    NO_LOCATION = 0;
    
    private final long          location;

    protected Element (long location) {
        this.location = location;
    }

    @Override
    public int                  hashCode () {
        return (getClass ().hashCode ());
    }

    @Override
    public boolean              equals (Object o) {
        return (this == o || getClass () == o.getClass ());
    }

    public abstract void        print (StringBuilder s);

    @Override
    public final String         toString () {
        StringBuilder               sb = new StringBuilder ();

        print (sb);

        return (sb.toString ());
    }
}
