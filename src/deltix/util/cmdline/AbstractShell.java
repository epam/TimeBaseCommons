package deltix.util.cmdline;

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
    
    protected boolean       doCommand (String key, String args) throws Exception {
        if (key.equalsIgnoreCase ("help") || key.equalsIgnoreCase ("?")) {
            printUsage (System.err);
            return (true);
        }
        
        if (key.equalsIgnoreCase ("set")) {
            int     argLength = args.length ();
            int     delim = 0;
            
            while (delim < argLength && !Character.isWhitespace (args.charAt (delim)))
                delim++;
            
            String  option = args.substring (0, delim);
            String  value = args.substring (delim).trim ();
            
            set (option, value);
            return (true);
        }
        
        return (false);
    }
    
    protected void          set (String option, String value) {
        try {
            if (!doSet (option, value)) {
                System.err.println ("set " + option + ": unrecognized option.");
                printUsage (System.err);
            }
        } catch (Throwable x) {
            printException (x, true);
        }
    }
    
    protected final void    runCommand (String key, String args) {
        key = key.trim ();

        try {
            if (!doCommand (key, args)) {
                System.err.println (key + ": unrecognized command.");
                printUsage (System.err);
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
        
        LineNumberReader    rd = 
            new LineNumberReader (new InputStreamReader (System.in));
        
        for (;;) {
            System.err.print ("==> ");
            System.err.flush ();
            
            String      line = rd.readLine ();

            if (line == null)
                break;
            
            line = line.trim ();
            
            int         len = line.length ();
            
            if (len == 0)
                continue;
            
            int         ws = 0;
            
            while (ws < len && !Character.isWhitespace (line.charAt (ws)))
                ws++;
            
            String      key = line.substring (0, ws);
                        
            if (key.equalsIgnoreCase ("quit") || key.equalsIgnoreCase ("exit"))
                break;

            String      cmdargs = line.substring (ws).trim ();
            
            runCommand (key, cmdargs);
        }
    }
}
