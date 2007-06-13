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
            set (key, args);
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
        
        for (int ii = 0; ii < args.length; ) {
            String          arg = args [ii++];

            if (arg.startsWith ("-") || arg.startsWith("/")) {
                String      option = arg.substring (1).toLowerCase ();

                if (option.equals ("do")) {
                    String          key = args [ii++];
                    String          cmdArgs;
                    
                    if (ii == args.length)
                        cmdArgs = null;
                    else {
                        StringBuilder   sb = new StringBuilder (args [ii++]);
                    
                        while (ii < args.length) {
                            sb.append (" ");
                            sb.append (args [ii++]);
                        }
                        
                        cmdArgs = sb.toString ();
                    }
                    
                    runCommand (key, cmdArgs);
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
            
            while (ws < len) {
                if (Character.isWhitespace (line.charAt (ws)))
                    break;
                
                ws++;
            }
            
            String      key = line.substring (0, ws);
                        
            if (key.equalsIgnoreCase ("quit") || key.equalsIgnoreCase ("exit"))
                break;

            String      cmdargs = ws < len ? line.substring (ws + 1).trim () : null;
            
            runCommand (key, cmdargs);
        }
    }
}
