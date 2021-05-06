package com.epam.deltix.util.time;

import com.epam.deltix.util.collections.generated.*;
import java.util.*;
import java.text.*;

/**
 *
 */
public class TimeDomain {
    private static final int        NOT_FOUND = 0x80000000;
    private static final int        CLEAR_NOT_FOUND = ~NOT_FOUND;

    public static final long        TIME_EXCLUDED = Long.MIN_VALUE;

    /**
     *  Inclusive starts of intervals.
     */
    private final LongArrayList     openTimes = new LongArrayList ();

    /**
     *  Exclusive ends of intervals.
     */
    private final LongArrayList     closeTimes = new LongArrayList ();

    /**
     *  cumTimes [n] = Sum (closeTimes [i] - openTimes [i]) for i <= n;
     */
    private final LongArrayList     cumTimes = new LongArrayList ();

    public static TimeDomain    createOpen () {
        TimeDomain     td = new TimeDomain ();

        td.add (Long.MIN_VALUE, Long.MAX_VALUE);
        td.buildIndex ();

        return (td);
    }
    
    /**
     *  Return interval index if t falls into an open interval.
     *  Otherwise return NOT_FOUND | index of interval following t.
     *  If domain is empty, return NOT_FOUND.
     */
    private int             search (long t) {
        int     n = openTimes.size ();

        if (n == 0)
            return (NOT_FOUND);

        int     pos = Arrays.binarySearch (openTimes.getInternalBuffer (), 0, n, t);

        if (pos >= 0)   // Hit open
            return (pos);

        pos = -pos - 1;

        if (pos == 0)
            return (NOT_FOUND);

        int     pos1 = pos - 1;

        if (t < closeTimes.getLong (pos1))
            return (pos1);

        return (NOT_FOUND | pos);
    }

    /**
     *  Return interval index to which cumulative open time t belongs.
     */
    private int             searchOpen (long t) {
        if (t < 0)
            return (-1);

        int     n = cumTimes.size ();

        int     pos = Arrays.binarySearch (cumTimes.getInternalBuffer (), 0, n, t);

        if (pos < 0)
            pos = -pos - 1;
       
        return (pos);
    }

    private void            insert (int idx, long open, long close) {
        openTimes.add (idx, open);
        closeTimes.add (idx, close);        
    }

    private void            widen (int idx, long open, long close) {
        openTimes.set (idx, Math.min (openTimes.getLong (idx), open));
        closeTimes.set (idx, Math.max (closeTimes.getLong (idx), close));
    }

    private void            remove (int fromIndex, int toIndex) {
        openTimes.removeRange (fromIndex, toIndex);
        closeTimes.removeRange (fromIndex, toIndex);
    }

    public void             buildIndex () {
        int     n = openTimes.size ();

        cumTimes.setSizeUnsafe (n);

        long    sum = 0;

        for (int ii = 0; ii < n; ii++) {
            long    open = openTimes.getLong (ii);
            long    close = closeTimes.getLong (ii);

            sum += (close - open);

            cumTimes.set (ii, sum);
        }
    }

    @Override
    public String           toString () {
        int     n = openTimes.size ();

        if (n == 0)
            return ("TimeDomain {}");

        StringBuffer    sb = new StringBuffer ("TimeDomain {\n");

        for (int ii = 0; ii < n; ii++) {
            sb.append ("[ (GMT) ");
            sb.append (GMT.formatDateTimeMillis (openTimes.getLong (ii)));
            sb.append (" .. ");
            sb.append (GMT.formatDateTimeMillis (closeTimes.getLong (ii)));
            sb.append (" ]\n");
        }

        sb.append ("}");

        return (sb.toString ());
    }

    public String           toString (TimeZone tz) {
        DateFormat  df = new SimpleDateFormat ("yyyy-MM-dd HH:mm");

        int     n = openTimes.size ();

        if (n == 0)
            return ("TimeDomain {}");

        StringBuffer    sb = new StringBuffer ("TimeDomain {\n");

        for (int ii = 0; ii < n; ii++) {
            sb.append ("[ ");
            sb.append (df.format (new Date (openTimes.getLong (ii))));
            sb.append (" .. ");
            sb.append (df.format (closeTimes.getLong (ii)));
            sb.append (" ]\n");
        }

        sb.append ("}");

        return (sb.toString ());
    }

    public int              getNumIntervals () {
        return (openTimes.size ());
    }

    public boolean          isEmpty () {
        return (openTimes.isEmpty ());
    }

    public void             clear () {
        openTimes.clear();
        closeTimes.clear();
        cumTimes.clear();
    }

    public void             add (long open, long close) {
        if (close <= open)
            return;

        int         openIndex = search (open) & CLEAR_NOT_FOUND;    // inclusive
        int         closeIndex = search (close);                    // inclusive

        if ((closeIndex & NOT_FOUND) != 0)
            closeIndex = (closeIndex & CLEAR_NOT_FOUND) - 1;

        int         width = closeIndex - openIndex;

        if (width < 0) {   // insert new interval at openIndex
            assert width == - 1;

            insert (openIndex, open, close);
        }
        else {              // replace [openIndex..closeIndex]
            if (width > 1)
                remove (openIndex + 1, closeIndex + 1);

            widen (openIndex, open, close);
        }

        cumTimes.clear ();
    }
/*
    public void             subtract (long open, long close) {
        if (close <= open)
            return;
    }
*/
    public boolean          includes (long t) {
        return ((search (t) & NOT_FOUND) == 0);
    }

    private long            getCumTimeUpTo (int upToIdx) {
        if (upToIdx == 0)
            return (0);
        else
            return (cumTimes.getLong (upToIdx - 1));
    }

    public long             g2c (long t, boolean snap) {
        int         idx = search (t);

        if ((idx & NOT_FOUND) == 0)
            return (getCumTimeUpTo (idx) + t - openTimes.getLong (idx));

        if (snap)
            return (getCumTimeUpTo (idx & CLEAR_NOT_FOUND));

        return (TIME_EXCLUDED);
    }

    public long             c2g (long t) {
        int     n = cumTimes.size ();

        if (n == 0)
            throw new IllegalStateException ("empty");

        if (t < 0)
            return (openTimes.getLong (0) + t);

        int     pos = Arrays.binarySearch (cumTimes.getInternalBuffer (), 0, n, t + 1);

        if (pos < 0) {
            pos = -pos - 1;
            
            if (pos == n)
                pos--;
        }

        return (openTimes.getLong (pos) + t - getCumTimeUpTo (pos));
    }

    /**
     *
     * @param from      From time in open space
     * @param to        To time in open space
     * @return Array of boundary times in cumulative open space (corresponding to
     *          the open time of each boundary interval).
     */
    public long []          getBoundaries (long from, long to) {
        int         a = searchOpen (from);
        int         b = searchOpen (to);
        int         n = b - a;

        if (n == 0)
            return (null);

        long []     boundaries = new long [n];

        for (int ii = 0; ii < n; ii++)
            boundaries [ii] = cumTimes.getLong (a + ii);
                
        return (boundaries);
    }
}
