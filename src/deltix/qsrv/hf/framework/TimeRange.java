package deltix.qsrv.hf.framework;

import deltix.util.time.GMT;

/**
 * @author Andy
 *         Date: Jan 14, 2010 4:20:52 PM
 */
public final class TimeRange {

    public static final long UNDEFINED = Long.MIN_VALUE;

    public final long from;

    public final long to;


    public TimeRange(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public TimeRange(long from) {
        this.from = from;
        this.to = UNDEFINED;
    }

    @Override
    public String toString() {
        String f = (from == UNDEFINED) ? "*" : GMT.formatDateTime(from);
        String t = (to == UNDEFINED) ? "*" : GMT.formatDateTime(to);

        return f + '-' + t;
    }
}
