package deltix.util.cmdline;

import deltix.util.io.StreamPump;
import deltix.util.io.SystemIOException;
import java.util.*;
import java.io.*;

import deltix.util.Util;
import deltix.util.io.IOUtil;
import org.xml.sax.SAXParseException;

/**
 *  Helps parse command-line arguments and handle exceptions.
 *	<p>
 *	Usage:
 *<pre>
 *public class MyApp extends deltix.util.cmdline.DefaultApplication {
 *    protected MyApp (String [] args) { 
 *        super (args); 
 *    }
 *
 *    protected void run () throws Throwable {
 *        ...
 *    }
 *
 *    public static void main (String [] args) {
 *        new MyApp (args).start ();
 *    }
 *}
 *</pre>
 */
public abstract class DefaultApplication {
    private String []                   mArgs;
    private Map <String, Integer>       mMap = new HashMap <String, Integer> ();
    private String						mDebugMode;
    private boolean						mVerbose;
    
    protected DefaultApplication (String [] args) {
    	mArgs = args;
    	
    	for (int ii = 0; ii < mArgs.length; ii++)
    		mMap.put (mArgs [ii], ii);
    	
        if (isArgSpecified ("-?") || isArgSpecified ("-help")) {
            printUsageAndExit ();
        }

    	mDebugMode = getArgValue ("-debug");
    	mVerbose = isArgSpecified ("-verbose");
    }
    
    /**
     *	Returns whether the application should give verbose
     *	information about the progress. This is controlled by
     *	passing the <tt>-verbose</tt> argument on the command
     *	line.
     */
    public boolean						isVerboseMode () {
    	return (mVerbose);
    }
    
    /**
     *	Returns whether the application should give debug
     *	information about the specified area. This is controlled by
     *	passing the <tt>-debug &lt;value&gt;</tt> arguments on 
     *	the command line. The &lt;value&gt; is checked for containing
     *	the string passed as parameter to this method.
     *
     *	@param mode		The symbolic name of a debuggable functional area.
     *	@return 		If the <tt>-debug &lt;value&gt;</tt> 
     *					command argument sequence was
     *					specified, and the &lt;value&gt; argument
     *					contains the specified symbolic name.
     */
    public boolean						isDebugMode (String mode) {
    	return (mDebugMode != null && mDebugMode.indexOf (mode) != -1);
    }
    
    /**
     *	Returns the command line argument array.
     */
    public String []					getArgs () {
    	return (mArgs);
    }
    
    /**
     *	Returns whether the specified argument was present on the
     *	command line.
     *
     *	@param key		The argument being looked for.
     */
    public boolean						isArgSpecified (String key) {
    	return (mMap.containsKey (key));
    }
    
    /**
     *	Returns the position of the specified argument on the
     *	command line.
     *
     *	@param key		The argument being looked for.
     *	@return			The 0-based index of the argument, 
     *						or -1 if not found.
     */
    public int							findArg (String key) {
    	Integer		idx = (Integer) mMap.get (key);
    	if (idx == null)
    		return (-1);
    	else
    		return (idx.intValue ());
    }
    
    /**
     *	Returns the argument following the specified argument on the
     *	command line.
     *
     *	@param key		The argument being looked for.
     *	@return			The next argument following <i>key</i>, 
     *						or <i>null</i> if not found, or if 
     *						<i>key</i> was the last argument.
     */
    public String						getArgValue (String key) {
    	return (getArgValue (key, null));
    }
    
    /**
     *	Returns the argument following the specified argument on the
     *	command line, or default value, if the 
     *	former is not specified.
     *
     *	@param key		The argument being looked for.
     *	@param defval	The value to return if the argument is not 
     *						specified.
     *	@return			The next argument following <i>key</i>, 
     *						or <i>null</i> if not found, or if 
     *						<i>key</i> was the last argument.
     */
    public String						getArgValue (String key, String defval) {
    	int			idx = findArg (key);
    	if (idx == -1 || idx + 1 >= mArgs.length)
    		return (defval);
    	else
    		return (mArgs [idx + 1]);
    }
    
    /**
     *	Returns an integer argument, or default value, if the 
     *	former is not specified.
     *
     *	@param key		The argument being looked for.
     *	@param defval	The value to return if the argument is not 
     *						specified.
     *	@return			The integer value of the next argument 
     *						following <i>key</i>, 
     *						or the value of <i>defval</i> if not found, 
     *						or if <i>key</i> was the last argument.
     */
    public int			getIntArgValue (String key, int defval) {
    	String				str = getArgValue (key);
    	if (str == null)
    		return (defval);
    	else
    		return (Integer.parseInt (str));
    }
    
    /**
     *	Returns a long integer argument, or default value, if the 
     *	former is not specified.
     *
     *	@param key		The argument being looked for.
     *	@param defval	The value to return if the argument is not 
     *						specified.
     *	@return			The integer value of the next argument 
     *						following <i>key</i>, 
     *						or the value of <i>defval</i> if not found, 
     *						or if <i>key</i> was the last argument.
     */
    public long			getLongArgValue (String key, long defval) {
    	String				str = getArgValue (key);
    	if (str == null)
    		return (defval);
    	else
    		return (Long.parseLong (str));
    }
    
    /**
     *	Returns a double argument, or default value, if the 
     *	former is not specified.
     *
     *	@param key		The argument being looked for.
     *	@param defval	The value to return if the argument is not 
     *						specified.
     *	@return			The double value of the next argument 
     *						following <i>key</i>, 
     *						or the value of <i>defval</i> if not found, 
     *						or if <i>key</i> was the last argument.
     */
    public double       getDoubleArgValue (String key, double defval) {
    	String				str = getArgValue (key);
    	if (str == null)
    		return (defval);
    	else
    		return (Double.parseDouble (str));
    }
    
    /**
     *	Returns the argument following the specified argument on the
     *	command line. If it is not specified, an exception is thrown.
     *
     *	@param key		The argument being looked for.
     *	@return			The next argument following <i>key</i>.
     *	@exception IllegalArgumentException
     *					If <i>key</i> is not present, or is the last
     *					argument on the command line.
     */
    public String						getMandatoryArgValue (String key) 
    	throws IllegalArgumentException
    {
    	int			idx = findArg (key);
    	if (idx == -1)
    		throw new IllegalArgumentException (
    			"Argument '" + key + "' is missing."
    		);
    	
    	int			next = idx + 1;
    	
    	if (next >= mArgs.length)
    		throw new IllegalArgumentException (
    			"Argument '" + key + "' must be followed by a value."
    		);
    		
    	return (mArgs [idx + 1]);
    }
    
    /**
     *	Override to do the work.
     */
    protected abstract void				run () throws Throwable;
    
    /**
     *	Prints out a standardized diagnostic line. Handles
     *	known wrapper exceptions intelligently, such as,
     *	for example, prints out the line number and position
     *	if a SAXParseException is thrown.
     */
    public static void					printException (
        Throwable                           x,
        boolean                             wantStackTrace
    )
    {
		if (x instanceof SAXParseException) {
			SAXParseException	saxx = (SAXParseException) x;
			System.err.print (
				">>> XML Error at " + saxx.getLineNumber () + "." +
				saxx.getColumnNumber ()
			);
		}
		else
			System.err.print (">>> Error");
		
		Throwable   ux = Util.unwrap (x);
		
		System.err.println (": " + ux.getClass ().getName () + ": " + ux.getMessage ());

  		if (wantStackTrace)
			ux.printStackTrace ();
  }

    /**
     *	Prints out a standardized diagnostic line. Handles
     *	known wrapper exceptions intelligently, such as,
     *	for example, prints out the line number and position
     *	if a SAXParseException is thrown. If the debug mode includes
     *	<b>trace</b>, the stack trace is printed out.
     */
    public void						handleException (Throwable x) {
		printException (x, isDebugMode ("trace"));
    }
    
    /**
     *	Call from a <code>main</code> method to run the application.
     *	This will handle exceptions, and make sure that if an
     *	exception is caught, the VM process will exit with an error code.
     */
    protected void					start () {
    	try {
    		run ();
    	} catch (Throwable x) {
    		handleException (x);
    		System.exit (1);
    	}
    }
    
    public void                     printUsageAndExit () {
        try {
            printUsage ();
        } catch (Throwable x) {
            x.printStackTrace ();
        }
        
        System.exit (0);
    }
    
    public void                     printUsage () 
        throws IOException, InterruptedException
    {
        Class           myClass = getClass ();        
        String          path = myClass.getName ().replace ('.', '/') + "-usage.txt";
        
        InputStream     is = myClass.getClassLoader ().getResourceAsStream (path);
        
        if (is == null)
            throw new FileNotFoundException ("Cannot open resource " + path);

        try {
            StreamPump.pump (is, System.out);
        } finally {
            Util.close (is);
        }
    }
}
