package deltix.util.time;

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

                @Override
                public void             run () {
                    for (;;) {
                        try {
                            for (;;) {
                                currentTime = System.currentTimeMillis ();
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
