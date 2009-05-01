package deltix.util.time;

import deltix.util.lang.Util;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final Logger        logger;
    private final Level         level;
    private long                timeIn = 0;

    public DelayTracker (long delayThreshold, String activityName) {
        this (delayThreshold, activityName, Util.LOGGER, Level.INFO);
    }

    public DelayTracker (long delayThreshold, String activityName, Logger logger, Level level) {
        if (delayThreshold < 2 * TimeKeeper.RESOLUTION) {
            logger.warning ("delayThreshold too low: " + delayThreshold + "; DelayReporter will not function correctly.");
        }

        this.delayThreshold = delayThreshold;
        this.activityName = activityName;
        this.logger = logger;
        this.level = level;
    }

    public void                 in () {
        timeIn = TimeKeeper.currentTime;
    }

    public void                 out () {
        if (timeIn == 0) {
            logger.warning ("Out of sequence call: out () not called after in ()");
            return;
        }

        long    delay = TimeKeeper.currentTime - timeIn;

        if (delay >= delayThreshold && logger.isLoggable (level))
            logger.log (level, activityName + " took " + delay * 0.001 + "s");

        timeIn = 0;
    }
}
