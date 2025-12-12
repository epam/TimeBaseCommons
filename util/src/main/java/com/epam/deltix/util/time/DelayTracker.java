package com.epam.deltix.util.time;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.gflog.api.LogLevel;

/**
 *  Logs a message if an activity takes longer than specified time.
 *  This class is very efficient and imposes completely negligible overhead.
 *  It uses {@link TimeKeeper} for time tracking, and does not even call
 *  System.currentTimeMillis. Instances of this class are obviously
 *  not thread-aware. Usage:
 *<pre>
//One-time preparation:
DelayTracker   dr = new DelayTracker (1000, "Some activity", MyLogger, Level.WARNING);
...
//Now, to report delays in performing an activity of some sort:
dr.in ();
... perform activity ...
dr.out ();
</pre>
*/
public final class DelayTracker {
    private final long          delayThreshold;
    private final String        activityName;
    private final Log logger;
    private final LogLevel level;
    private long                timeIn = 0;

    public DelayTracker (long delayThreshold, String activityName) {
        this (delayThreshold, activityName, LogFactory.getLog(DelayTracker.class), LogLevel.INFO);
    }

    public DelayTracker (long delayThreshold, String activityName, Log logger, LogLevel level) {
        this.delayThreshold = delayThreshold;
        this.activityName = activityName;
        this.logger = logger;
        this.level = level;
    }

    public void                 in () {
        timeIn = System.nanoTime ();
    }

    public void                 out () {
        if (timeIn == 0) {
            logger.warn().append("Out of sequence call: out () not called after in ()").commit();
            return;
        }

        long    delay = System.nanoTime () - timeIn;

        if (delay >= delayThreshold && logger.isEnabled (level))
            logger.log (level).append(activityName).append(" took ").append(delay * 1E-9).append(" s").commit();

        timeIn = 0;
    }
}
