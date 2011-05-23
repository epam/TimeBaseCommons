package deltix.util.time;

import deltix.util.lang.Util;

/**
 *  Use TimeKeeper.currentTime instead of System.currentTimeMillis ().
 *  It's 5 times faster and almost equally precise.
 */
public abstract class TimeKeeper {
    public static final long            RESOLUTION = 10; // should be multiple x10 to avoid calling timeBeginPeriod/timeEndPeriod per each sleep
    
    public static volatile long         currentTime = System.currentTimeMillis ();

    static {
        Thread  t =
            new Thread ("Time Keeper") {
                private boolean wasBackJumpReported = false;

                @Override
                public void             run () {
                    for (;;) {
                        try {
                            for (;;) {
                                final long ct = System.currentTimeMillis();
                                if (ct < currentTime) {
                                    if (!wasBackJumpReported) {
                                        Util.LOGGER.warning("time-back jump ignored. from " +
                                                GMT.formatDateTimeMillis(currentTime) + " to " +
                                                GMT.formatDateTimeMillis(ct));
                                        wasBackJumpReported = true;
                                    }
                                } else {
                                    if (wasBackJumpReported)
                                        wasBackJumpReported = false;
                                    currentTime = ct;
                                }
                                sleep (RESOLUTION);
                            }
                        } catch (Throwable x) {
                            // Ignore.
                        }
                    }
                }

            };

        t.setDaemon (true);

        t.start ();
    }
}
