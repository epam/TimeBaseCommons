package deltix.util.os;

import java.io.*;

import deltix.util.concurrent.*;
import deltix.util.io.*;

public class LinuxOS {
    
    public static boolean isX64 () {
        try {
            final ProcessBuilder pb = new ProcessBuilder ("uname",
                                                          "-m");

            pb.redirectErrorStream (true);

            final Process proc = pb.start ();
            final BufferedReader rd = new BufferedReader (new InputStreamReader (proc.getInputStream ()));

            String out = "";
            for (;;) {
                final String line = rd.readLine ();

                if (line == null)
                    break;
                out += line;
            }

            final int exitVal = proc.waitFor ();
            if (exitVal != 0)
                throw new IOException ("isX64 function failed with error code " + exitVal);

            return out.indexOf ("64") != -1;
        } catch (IOException e) {
            throw new UncheckedIOException (e);

        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException (e);
        }
    }

    public static void command (String... parameters) {
        try {

            if (parameters == null || parameters.length == 0)
                return;
            final ProcessBuilder pb = new ProcessBuilder (parameters);

            pb.redirectErrorStream (true);

            final Process proc = pb.start ();
            final BufferedReader rd = new BufferedReader (new InputStreamReader (proc.getInputStream ()));

            for (; ;) {
                final String line = rd.readLine ();

                if (line == null)
                    break;
                System.out.println (line);
            }

            final int exitVal = proc.waitFor ();
            if (exitVal != 0)
                throw new IOException (parameters[0] + " function failed with error code " + exitVal);

        } catch (IOException e) {
            throw new UncheckedIOException (e);

        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException (e);
        }
    }
    
}
