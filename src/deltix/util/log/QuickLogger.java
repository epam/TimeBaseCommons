package deltix.util.log;

import deltix.util.io.*;
import deltix.util.lang.*;
import deltix.util.time.TimeKeeper;
import java.io.*;
import java.util.TimeZone;
import java.util.logging.Level;

/**
 *  Specialized logger for high-performance situations. Capable of logging about 
 *  4 million messages per second. Flushes itself every 50 milliseconds,
 *  and flushes the log when JVM shuts down. Logs to current folder, but creates a new file
 *  for every JVM session.
 */
public class QuickLogger {
    static long                             dayStart;
    static Writer                           writer;

    static {
        long                                t = System.currentTimeMillis ();

        dayStart = t - (t % 86400000) - TimeZone.getDefault ().getOffset (t);

        String                              logName =
            String.format ("quicklog.%tH.%<tM.%<tS.txt", t);

        try {            
            writer = new BufferedWriter (new FileWriter (logName));
        } catch (IOException ex) {
            Util.LOGGER.log (Level.SEVERE, null, ex);
        }

        Thread                              flusher =
            new Thread ("QuickLogger Flusher") {
                @Override
                public void     run () {
                    for (;;) {
                        try {
                            writer.flush ();
                            sleep (50);
                        } catch (Exception ex) {
                            Util.LOGGER.log (Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            
        flusher.setDaemon (true);
        flusher.start ();

        ShutdownHook.closeOnShutdown (writer);
    }

    private static void     writeTime () throws IOException {
        long        t = TimeKeeper.currentTime - dayStart;

        int     ms = (int) (t % 1000);

        t /= 1000;

        int     s = (int) (t % 60);

        t /= 60;

        int     m = (int) (t % 60);

        t /= 60;

        int     h = (int) t;

        writer.write ('0' + (h / 10));
        writer.write ('0' + (h % 10));
        writer.write (':');
        writer.write ('0' + (m / 10));
        writer.write ('0' + (m % 10));
        writer.write (':');
        writer.write ('0' + (s / 10));
        writer.write ('0' + (s % 10));
        writer.write ('.');
        writer.write ('0' + (ms / 100));
        writer.write ('0' + ((ms / 10) % 10));
        writer.write ('0' + (ms % 10));
    }

    public static void      log (String id, String event, String message) {
        try {
            synchronized (writer) {
                writeTime ();

                writer.write (": ");

                if (id != null)
                    writer.write (id);

                if (event != null) {
                    writer.write ('.');
                    writer.write (event);
                }

                if (message != null) {
                    writer.write (": ");
                    writer.write (message);
                }
                
                writer.write (IOUtil.CR);
            }
        } catch (IOException ex) {
            Util.LOGGER.log (Level.SEVERE, null, ex);
        }
    }
}
