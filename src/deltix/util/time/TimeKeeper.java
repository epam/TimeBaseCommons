package deltix.util.time;

import deltix.util.lang.Util;
import java.util.concurrent.locks.LockSupport;

/**
 *  Use TimeKeeper.currentTime instead of System.currentTimeMillis ().
 *  It's 5 times faster and almost equally precise.
 */
public abstract class TimeKeeper {
    public static volatile long         currentTime = System.currentTimeMillis ();
    
    public static final long            RESOLUTION = 1;
    
    static {
        if (Util.IS_WINDOWS_OS) {
            //
            // Force the Windows system clock into fast mode
            // Workaround per   http://bugs.sun.com/view_bug.do?bug_id=6435126
            //
            Thread  magic =
                new Thread ("Neverending Thread") {
                    @Override
                    public void run() {
                        for (;;) {
                            try {
                                Thread.sleep (Integer.MAX_VALUE);
                            } catch(InterruptedException ex) {
                            }
                        }
                    }
                };        

            magic.setDaemon (true);
            magic.start ();
        }
        
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
                                        Util.LOGGER.warning(
                                            "time-back jump ignored. from " +
                                            GMT.formatDateTimeMillis(currentTime) + " to " +
                                            GMT.formatDateTimeMillis(ct)
                                        );
                                        wasBackJumpReported = true;
                                    }
                                } else {
                                    if (wasBackJumpReported)
                                        wasBackJumpReported = false;
                                    currentTime = ct;
                                }
                                
                                LockSupport.parkNanos (RESOLUTION * 500000);
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
