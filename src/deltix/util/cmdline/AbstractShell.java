package deltix.util.cmdline;

import deltix.util.Version;
import deltix.util.lang.StringUtils;
import java.io.*;
import java.lang.reflect.Method;

/**
 *
 */
public abstract class AbstractShell extends DefaultApplication {
    private int             errorCode = 0;
    private boolean         exitOnError = false;
    private boolean         confirm = true;
    
    protected AbstractShell (String [] args) {
        super (args);
    }

    public void          confirm (String msg) {
        if (getConfirm ())
            System.out.println (msg);
    }
        
    protected boolean       doSet (String option, String value) throws Exception {
        if (option.equalsIgnoreCase ("exitOnError")) {            
            exitOnError = Boolean.parseBoolean (value);
            confirm ("exitOnError: " + exitOnError);
            return (true);
        }
        
        if (option.equalsIgnoreCase ("confirm")) {            
            confirm = Boolean.parseBoolean (value);
            confirm ("confirm: " + confirm);
            return (true);
        }
        
        return (false);
    }

    protected void          doSet () {
        System.out.println ("exitOnError    " + exitOnError);        
        System.out.println ("confirm        " + confirm);        
    }

    protected boolean       doCommand (
        String                  key, 
        String                  args,
        String                  fileId,
        LineNumberReader        rd
    )
        throws Exception 
    {
        return (doCommand (key, args));
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
            if (args == null)
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
                runScript (args, rd, false, true);
            } finally {
                rd.close ();
            }

            return (true);
        }

        return (false);
    }

    protected final void    set (String option, String value) {
        Method      m;
        
        try {
            m = getClass ().getMethod ("set_" + option, String.class);
        } catch (NoSuchMethodException x) {
            m = null;
        }
        
        try {
            if (m != null)
                m.invoke (this, value);
            else if (!doSet (option, value)) 
                System.err.println ("set " + option + ": unrecognized option. (Type ? for usage)");            
        } catch (Throwable x) {
            printException (x, true);
        }
    }

    public final void    error (int level) {
        if (errorCode < level)
            errorCode = level;
        
        if (exitOnError) {
            System.err.println ("Exiting on error.");
            System.exit (errorCode);
        }
    }
    
    public final void    runCommand (String key, String args, String fileId, LineNumberReader rd) {
        key = key.trim ();

        if (args != null && args.length () == 0)    // eliminate check
            args = null;
        
        Method      m = null;
        int         sig = -1;
        String      mname = "cmd_" + key;
        
        try {
            m = getClass ().getMethod (mname, String.class, String.class, LineNumberReader.class);
            sig = 3;
        } catch (NoSuchMethodException x) {
        }
        
        if (m == null) {
            try {
                m = getClass ().getMethod (mname, String.class);
                sig = 1;
            } catch (NoSuchMethodException x) {
            }
        }
        
        if (m == null) {
            try {
                m = getClass ().getMethod (mname);
                sig = 0;
            } catch (NoSuchMethodException x) {
            }
        }
        
        try {
            switch (sig) {
                case -1:
                    if (!doCommand (key, args, fileId, rd)) {
                        System.err.println (key + ": unrecognized command. (Type ? for usage)");                
                        error (1);
                    }
                    break;
                    
                case 3:
                    m.invoke (this, args, fileId, rd);
                    break;
                    
                case 1:
                    m.invoke (this, args);
                    break;
                                        
                case 0:
                    m.invoke (this);
                    break;
                    
                default:
                    throw new RuntimeException ();
            }            
        } catch (Throwable x) {
            printException (x, true);            
            error (2);
        }
        
        outWriter.flush ();
        errWriter.flush ();
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
                    runCommand (key, cmdArgs, null, null);

                    if (exitWhenDone)
                        return;
                }
                else
                    doSet (option, args [ii++]);
            }
        }

        runScript ("stdin", new InputStreamReader (System.in), true, false);
        doQuit ();
    }
    
    protected void      doQuit () {
        if (errorCode != 0)
            System.err.println (">>> Error Level: " + errorCode);
        
        System.exit (errorCode);
    }

    public void        runScript (String fileId, Reader in, boolean showPrompt, boolean echo)
        throws IOException, InterruptedException
    {
        LineNumberReader    rd;
        
        if (in instanceof LineNumberReader)
            rd = (LineNumberReader) in;
        else
            rd = new LineNumberReader (in);
        
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

            if (key.equalsIgnoreCase ("exit"))
                break;

            if (key.equalsIgnoreCase ("quit"))
                doQuit ();

            String      cmdargs = line.substring (ws).trim ();

            if (Thread.interrupted())
                throw new InterruptedException();
            
            runCommand (key, cmdargs, fileId, rd);
        }
    }

    public int          getErrorCode () {
        return errorCode;
    }

    public void         setErrorCode (int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean      getExitOnError () {
        return exitOnError;
    }

    public void         setExitOnError (boolean exitOnError) {
        this.exitOnError = exitOnError;
    }

    public boolean      getConfirm () {
        return confirm;
    }

    public void         setConfirm (boolean verbose) {
        this.confirm = verbose;
    }        
}
