package com.epam.deltix.util.cmdline;

import com.epam.deltix.util.io.StreamPump;
import java.util.*;
import java.io.*;

import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.collections.generated.IntegerArrayList;
import com.epam.deltix.util.io.IOUtil;
import com.epam.deltix.util.time.GMT;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.xml.sax.SAXParseException;

/**
 *  Helps parse command-line arguments and handle exceptions.
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
    public static final PrintWriter             errWriter = 
        new PrintWriter (new OutputStreamWriter (System.err));
    
    public static final PrintWriter             outWriter = 
        new PrintWriter (new OutputStreamWriter (System.out), true);
    
    private String []                           mArgs;
    private Map <String, IntegerArrayList>      mMap =
        new HashMap <String, IntegerArrayList> ();

    protected DefaultApplication (String [] args) {
        ArrayList <String>      expArgs = new ArrayList <String> ();

        if (args != null)
            for (int ii = 0; ii < args.length; ii++) {
                String              arg = args [ii];
                String              fpath = arg.substring (1);

                if (arg.startsWith ("@")) {
                    String      line;

                    try {
                        line = IOUtil.readTextFile (fpath);
                    } catch (Exception x) {
                        throw new RuntimeException ("Cannot read file: " + fpath);
                    }

                    StringTokenizer     stk = new StringTokenizer (line);

                    while (stk.hasMoreTokens ())
                        expArgs.add (stk.nextToken ());
                }
                else
                    expArgs.add (arg);
            }

    	mArgs = expArgs.toArray (new String [expArgs.size ()]);

    	for (int ii = 0; ii < mArgs.length; ii++) {
            String              arg = mArgs [ii];
            IntegerArrayList    index = mMap.get (arg);

            if (index == null) {
                index = new IntegerArrayList ();
                mMap.put (arg, index);
            }

    		index.add (ii);
        }

        //
        //  Do some default argument processing
        //
        if (isArgSpecified ("-showargs")) {
            for (int ii = 0; ii < mArgs.length; ii++) {
                if (ii > 0)
                    System.out.print (" ");

                System.out.print (mArgs [ii]);
            }

            System.out.println ();
        }

        if (isArgSpecified ("-?") || isArgSpecified ("-help")) {
            printUsageAndExit ();
        }
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
    public final boolean				isArgSpecified (String key) {
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
    	IntegerArrayList    index = mMap.get (key);

    	if (index == null)
    		return (-1);

        if (index.size () == 1)
    		return (index.get (0));

        throw new IllegalArgumentException ("Argument " + key + " was specified more than once.");
    }

    /**
     *	Returns the arguments following the specified key on the
     *	command line, which can be present more than once.
     *
     *	@param key		The argument being looked for.
     *	@return			An array of arguments following <i>key</i>,
     *						or <i>null</i> if not found. The return value is
     *                      NEVER an empty array.
     */
    public String []					getArgValues (String key) {
    	IntegerArrayList    index = mMap.get (key);

    	if (index == null)
    		return (null);

        int                 num = index.size ();
        String []           ret = new String [num];

        for (int ii = 0; ii < num; ii++) {
            int             pos = index.get (ii) + 1;

            if (pos >= mArgs.length)
                throw new IllegalArgumentException (
                    "Argument " + key + " must not be the last argument on the command line."
                );

            ret [ii] = mArgs [pos];
        }

        return (ret);
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
     *						or <i>null</i> if not found.
     */
    public String						getArgValue (String key, String defval) {
    	int			idx = findArg (key);

        if (idx == -1)
    		return (defval);

        if (idx + 1 >= mArgs.length)
            throw new IllegalArgumentException (
                "Argument " + key + " must not be the last argument on the command line."
            );

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
    		return (Integer.parseInt (str.trim()));
    }

    /**
     *	Returns file argument, or null, if the
     *	key is not specified.
     *
     *	@param key		The argument being looked for.
     *	@return			The file value of the next argument
     *						following <i>key</i>,
     *						or <tt>null</tt> if the key is not found.
     */
    public File 			getFileArg (String key) {
    	String				str = getArgValue (key);
    	if (str == null)
    		return (null);
    	else
    		return (new File (str));
    }

    /**
     *	Returns file argument.
     *
     *	@param key		The argument being looked for.
     *	@return			The file value of the next argument
     *						following <i>key</i>.
     */
    public File 			getMandatoryFileArg (String key) {
    	String				str = getMandatoryArgValue (key);
    	if (str == null)
    		return (null);
    	else
    		return (new File (str));
    }

    /**
     *	Returns file arguments, or null, if the
     *	key is not specified.
     *
     *	@param key		The argument being looked for.
     *	@return			The file value of the next argument
     *						following <i>key</i>,
     *						or <tt>null</tt> if the key is not found.
     */
    public File []          getFileArgs (String key) {
    	String []			str = getArgValues (key);
    	if (str == null)
    		return (null);
        else {
            int         n = str.length;
            File []     ret = new File [n];
            
            for (int ii = 0; ii < n; ii++)
                ret [ii] = new File (str [ii]);
            
    		return (ret);
        }
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
        printException (x, wantStackTrace, errWriter);
        errWriter.flush ();
    }
    
    /**
     *	Prints out a standardized diagnostic line. Handles
     *	known wrapper exceptions intelligently, such as,
     *	for example, prints out the line number and position
     *	if a SAXParseException is thrown.
     */
    public static void					printException (
        Throwable                           x,
        boolean                             wantStackTrace,
        PrintWriter                         out
    ) 
    {
		if (x instanceof SAXParseException) {
			SAXParseException	saxx = (SAXParseException) x;
			out.write (                
				">>> XML Error at " + saxx.getLineNumber () + "" +
				saxx.getColumnNumber () + ": "
			);
		}
		else
			out.write (">>> Error: ");

		Throwable   ux = Util.unwrap (x);

  		if (wantStackTrace) {
            if (ux.getStackTrace ().length > 0)             
                ux.printStackTrace (out);        
            else
                out.println (ux.toString ());
        }
        else
            out.println (ux.getClass ().getSimpleName () + ": " + ux.getLocalizedMessage ());
    }

    /**
     *	Prints out a standardized diagnostic line. Handles
     *	known wrapper exceptions intelligently, such as,
     *	for example, prints out the line number and position
     *	if a SAXParseException is thrown.
     */
    public void						handleException (Throwable x) {
        printException (x, true);       
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

    public final void               printUsageAndExit () {
        try {
            printUsage ();
        } catch (Throwable x) {
            x.printStackTrace ();
        }

        System.exit (0);
    }

    public final void               printUsage ()
        throws IOException, InterruptedException
    {
        printUsage (System.out);
    }

    protected long                  getDateArg (String key, long defValue) {
        String      arg = getArgValue (key);

        if (arg == null)
            return (defValue);

        try {
            return (Long.parseLong (arg));
        } catch (NumberFormatException x) {
            // continue trying different formats
        }

        return parseDateTime(arg);
    }

    protected long parseDateTime(String value) {
        DateFormat format = createDateFormat ();

        try {
            return (format.parse (value).getTime ());
        } catch (ParseException px) {
            throw new IllegalArgumentException (
                "Bad date format: " + value + " (Expected format: \"" + getDateFormatSpec() + "\")"
            );
        }
    }



    protected String            getDefaultDateFormat () {
        return ("yyyy-MM-dd");
    }

    protected TimeZone          getDefaultTimeZone () {
        return (GMT.TZ);
    }

    protected DecimalFormat     createDecimalFormat () {
        return (new DecimalFormat (getArgValue ("-ff", "#,###.##")));
    }

    protected TimeZone          getTimeZone () {
        String          tzname = getArgValue ("-tz");

        return (tzname == null ? getDefaultTimeZone () : TimeZone.getTimeZone (tzname));
    }

    protected String getDateFormatSpec () {
        return getArgValue ("-tf", getDefaultDateFormat ());
    }

    protected DateFormat        createDateFormat () {
        DateFormat      format = new SimpleDateFormat (getDateFormatSpec());

        format.setTimeZone (getTimeZone ());

        return (format);
    }

    public void                 printUsage (OutputStream os)
        throws IOException, InterruptedException
    {
        boolean         somethingPrinted = false;

        for (Class <?> myClass = getClass (); myClass != Object.class; myClass = myClass.getSuperclass ()) {
            String          path = myClass.getName ().replace ('.', '/') + "-usage.txt";
            InputStream     is = myClass.getClassLoader ().getResourceAsStream (path);

            if (is == null)
                continue;

            try {
                StreamPump.pump (is, os);
            } finally {
                Util.close (is);
            }

            somethingPrinted = true;
        }
    }
}
