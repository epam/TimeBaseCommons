package deltix.util.time;

/**
 *  Use TimeKeeper.currentTime instead of System.currentTimeMillis ().
 *  It's 5 times faster and equally precise.
 */
public abstract class TimeKeeper {
    public static final long            RESOLUTION = 15;
    
    public static volatile long         currentTime = System.currentTimeMillis ();

    static {
        Thread  t = 
            new Thread ("Time Keeper") {

                @Override
                public void             run () {
                    for (;;) {
                        currentTime = System.currentTimeMillis ();
                        try {
                            sleep (RESOLUTION);
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
