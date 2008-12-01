package deltix.util.cmdline;

import deltix.util.Version;
import deltix.util.lang.StringUtils;
import java.io.*;

/**
 *
 */
public abstract class AbstractShell extends DefaultApplication {
    protected AbstractShell (String [] args) {
        super (args);
    }

    protected boolean       doSet (String option, String value) throws Exception {
        return (false);
    }

    protected void          doSet () throws Exception {
    }

    protected boolean       doCommand (String key, String args) throws Exception {
        if (key.equalsIgnoreCase ("help") || key.equalsIgnoreCase ("?")) {
            printUsage (System.err);
            return (true);
        }

        if (key.equalsIgnoreCase ("showtime")) {
            System.out.println (new java.util.Date ());
            return (true);
        }

        if (key.equalsIgnoreCase ("version")) {
            System.out.println ("Version " + Version.VERSION_STRING);
            return (true);
        }

        if (key.equalsIgnoreCase ("set")) {
            int     argLength = args.length ();

            if (argLength == 0)
                doSet ();
            else {
                String []   split = StringUtils.splitAtWhitespace (args);

                set (split [0], split [1]);
            }

            return (true);
        }

        if (key.equalsIgnoreCase ("exec")) {
            FileReader  rd = new FileReader (args);

            try {
                runScript (rd, false, true);
            } finally {
                rd.close ();
            }

            return (true);
        }

        return (false);
    }

    protected void          set (String option, String value) {
        try {
            if (!doSet (option, value)) {
                System.err.println ("set " + option + ": unrecognized option. (Type ? for usage)");
            }
        } catch (Throwable x) {
            printException (x, true);
        }
    }

    protected final void    runCommand (String key, String args) {
        key = key.trim ();

        try {
            if (!doCommand (key, args)) {
                System.err.println (key + ": unrecognized command. (Type ? for usage)");
            }
        } catch (Throwable x) {
            printException (x, true);
        }
    }

    protected void          run () throws Throwable {
        String []               args = getArgs ();
        boolean                 exitWhenDone = false;

        for (int ii = 0; ii < args.length; ) {
            String          arg = args [ii++];

            if (arg.startsWith ("-") || arg.startsWith ("/")) {
                String      option = arg.substring (1).toLowerCase ();

                if (option.equals ("exec")) {
                    String          key = args [ii++];
                    String          cmdArgs;

                    StringBuilder   sb = new StringBuilder ();

                    for (;; ii++) {
                        if (ii == args.length)
                            break;

                        arg = args [ii];

                        if (arg.startsWith ("-") || arg.startsWith ("/")) {
                            option = arg.substring (1).toLowerCase ();

                            if (option.equals ("exec"))
                                break;

                            if (option.equals ("exit")) {
                                exitWhenDone = true;
                                break;
                            }
                        }

                        if (sb.length () > 0)
                            sb.append (" ");

                        sb.append (arg);
                    }

                    cmdArgs = sb.toString ();
                    runCommand (key, cmdArgs);

                    if (exitWhenDone)
                        return;
                }
                else
                    doSet (option, args [ii++]);
            }
        }

        runScript (new InputStreamReader (System.in), true, false);
    }

    protected void        runScript (Reader in, boolean showPrompt, boolean echo)
        throws IOException, InterruptedException
    {
        LineNumberReader    rd = new LineNumberReader (in);

        for (;;) {
            if (showPrompt) {
                System.err.print ("==> ");
                System.err.flush ();
            }

            String      line = rd.readLine ();

            if (line == null)
                break;

            line = line.trim ();

            int         len = line.length ();

            if (len == 0)
                continue;

            if (line.charAt (0) == '#')
                continue;

            if (echo) {
                if (!showPrompt)
                    System.err.print ("==> ");

                System.out.println (line);
                System.out.flush ();
            }

            int         ws = 0;

            while (ws < len && !Character.isWhitespace (line.charAt (ws)))
                ws++;

            String      key = line.substring (0, ws);

            if (key.equalsIgnoreCase ("quit") || key.equalsIgnoreCase ("exit"))
                System.exit (0);

            String      cmdargs = line.substring (ws).trim ();

            if (Thread.interrupted())
                throw new InterruptedException();
            runCommand (key, cmdargs);
        }
    }
}
