package deltix.util;

import deltix.util.memory.DataExchangeUtils;
import java.io.*;
import java.net.*;
import java.security.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.rmi.RemoteException;
import org.xml.sax.SAXException;
import java.lang.reflect.*;
import java.lang.reflect.Array;

/** Set of usefull methods */
public class Util {
    public static final String  LOGGER_NAME = "deltix.util";
    public static final Logger  LOGGER = Logger.getLogger (LOGGER_NAME);

    public static <T extends Comparable <T>> T  max (T a, T b) {
        if (a == null)
            return (b);
        
        if (b == null)
            return (a);
        
        return (a.compareTo (b) > 0 ? a : b);
    }
        
    public static <T extends Comparable <T>> T  min (T a, T b) {
        if (a == null)
            return (b);
        
        if (b == null)
            return (a);
        
        return (a.compareTo (b) < 0 ? a : b);
    }
        
    public static void      writeNullableString (String s, ObjectOutput os)
        throws IOException
    {
        os.writeBoolean (s == null);

        if (s != null)
            os.writeUTF (s);
    }

    public static String    readNullableString (ObjectInput is)
        throws IOException
    {
        if (is.readBoolean ())
            return (null);

        return (is.readUTF ());
    }

    /**
     *  Loads and instantiates the specified class using the no-argument constructor
     */
    public static Object    newInstance (String className) 
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return (Class.forName (className).newInstance ());
    }
    
    /**
     *  Call a static method of the specified class. Figure out the method
     *  signature from the types of the supplied arguments (which must not contain
     *  null elements).
     */
    public static Object    callStaticMethod (
        String                  className,
        String                  methodName,
        Object []               args
    )
        throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException
    {
        Class       c = Class.forName (className);
        Class []    paramTypes = new Class [args.length];
        for (int ii = 0; ii < args.length; ii++)
            paramTypes [ii] = args [ii].getClass ();

        Method      m = c.getMethod (methodName, paramTypes);
        return (m.invoke (null, args));
    }

    /**
     *  Call a method of the specified class. Figure out the method
     *  signature from the types of the supplied arguments (which must not contain
     *  null elements).
     */
    public static Object    callNonStaticMethod (
        Object                  object,
        String                  methodName,
        Object []               args
    )
        throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException
    {
        Class []    paramTypes = new Class [args.length];
        for (int ii = 0; ii < args.length; ii++)
            paramTypes [ii] = args [ii].getClass ();

        Method      m = object.getClass ().getMethod (methodName, paramTypes);
        return (m.invoke (object, args));
    }

    /**
     *	Gets to the bottom of the exception.
     */
    public static Throwable		unwrap (Throwable ex) {

        Throwable result = ex;
        while (result != null) {
            Throwable nested;

            if (result instanceof SAXException)
                nested = ((SAXException) result).getException ();
            else if (result instanceof RemoteException)
                nested = ((RemoteException) result).detail;
            else if (result instanceof InvocationTargetException)
                nested = ((InvocationTargetException) result).getTargetException ();
            else
                nested = result.getCause();

            if (nested == null)
                break;

            result = nested;
        }
        
        return result;
    }

    /**
     *	Same as <code>System.getProperty (prop, def)</code>, but returns
     *		<code>null</code> if access to system properties is prohibited.
     */
    public static String		getSysProp (String prop, String def) {
        try {
            return (System.getProperty (prop, def));
        } catch (java.security.AccessControlException acx) {
            return (null);
        }
    }

    /**
     *	Same as <code>System.getProperty (prop)</code>, but returns
     *		<code>null</code> if access to system properties is prohibited.
     */
    public static String		getSysProp (String prop) {
        try {
            return (System.getProperty (prop));
        } catch (java.security.AccessControlException acx) {
            return (null);
        }
    }

    public static URL			getResource (String path)
        throws MalformedURLException
    {
        if (path.indexOf (":") > 0)
            return (new URL (path));
        else
            return (Util.class.getClassLoader ().getResource (path));
    }

    public static URL			getResourceNoX (String path) {
        try {
            return (getResource (path));
        } catch (MalformedURLException mux) {
            LOGGER.log (Level.SEVERE, "Error getting resource '" + path + "'", mux);
            return (null);
        }
    }

    public static void			handleException (Exception x) {
        LOGGER.log (Level.SEVERE, "Ignoring (but Logging) Exception ...", x);
    }

    /**
     *  Closes a RandomAccessFile without throwing an exception. Checks for null.
     */
    public static void			close (RandomAccessFile wr) {
        if (wr != null)
            try {
                wr.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Closes a Writer without throwing an exception. Checks for null.
     */
    public static void			delete (Writer wr) {
        if (wr != null)
            try {
                wr.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Deletes a File. If fails, logs an error message.
     */
    public static void			delete (File f) {
        if (f != null && !f.delete ())
            LOGGER.log (
                Level.SEVERE,
                "Failed to delete file " + f
            );
    }

    /**
     *  Closes a Writer without throwing an exception. Checks for null.
     */
    public static void			close (Writer wr) {
        if (wr != null)
            try {
                wr.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Closes a Zip File without throwing an exception. Checks for null.
     */
    public static void			close (java.util.zip.ZipFile z) {
        if (z != null)
            try {
                z.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }


    /**
     *  Closes a Reader without throwing an exception. Checks for null.
     */
    public static void			close (Reader rd) {
        if (rd != null)
            try {
                rd.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Closes an InputStream without throwing an exception. Checks for null.
     */
    public static void			close (InputStream is) {
        if (is != null)
            try {
                is.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Closes an OutputStream without throwing an exception. Checks for null.
     */
    public static void			close (OutputStream os) {
        if (os != null)
            try {
                os.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Closes a Socket without throwing an exception. Checks for null.
     */
    public static void			close (Socket s) {
        if (s != null)
            try {
                s.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Closes a ServerSocket without throwing an exception. Checks for null.
     */
    public static void			close (ServerSocket s) {
        if (s != null)
            try {
                s.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Closes a JDBC Connection without throwing an exception.
     *	Checks for null.
     */
    public static void			close (java.sql.Connection conn) {
        if (conn != null)
            try {
                conn.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Rolls back any changes made in connection without throwing an exception.
     *	Checks for null.
     */
    public static void			rollback (java.sql.Connection conn) {
        if (conn != null)
            try {
                conn.rollback ();
            } catch (Exception x) {
                handleException (x);
            }
    }


    /**
     *  Closes a Statement without throwing an exception. Checks for null.
     */
    public static void			close (Statement stmt) {
        if (stmt != null)
            try {
                stmt.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *  Closes a ResultSet without throwing an exception. Checks for null.
     */
    public static void			close (ResultSet rs) {
        if (rs != null)
            try {
                rs.close ();
            } catch (Exception x) {
                handleException (x);
            }
    }

    /**
     *	Returns the difference between the specified segments of
     *	arr1 and arr2, MSBF.
     */
    public static int			arraycomp (
        byte [] 					arr1,
        int							off1,
        byte [] 					arr2,
        int							off2,
        int							len
    )
    {
        int		diff = 0;

        for (int ii = 0; ii < len && diff == 0; ii++)
            diff = arr1 [off1 + ii] - arr2 [off2 + ii];

        return (diff);
    }

    /**
     *	Returns the difference between the specified segments of
     *	arr1 and arr2, MSBF, comparing bytes' unsigned values.
     */
    public static int			arrayucomp (
        byte [] 					arr1,
        int							off1,
        byte [] 					arr2,
        int							off2,
        int							len
    )
    {
        for (int ii = 0; ii < len; ii++) {
            int    b1 = ((int) arr1 [off1 + ii]) & 0xFF;
            int    b2 = ((int) arr2 [off2 + ii]) & 0xFF;
            
            int     diff = b1 - b2;
            
            if (diff != 0) 
                return (diff);
        }
        
        return (0);
    }

    public static void			arraydump (
        PrintStream 				ps,
        byte [] 					bytes,
        int 						offset,
        int 						n
    )
    {
        for (int jj = 0; jj < n; jj++) {
            if (jj > 0)
                ps.print (".");

            ps.print (bytes [offset + jj] & 0xFF);
        }
    }

    public static void			arraydump (
        StringBuffer 				sb,
        byte [] 					bytes,
        int 						offset,
        int 						n
    )
    {
        for (int jj = 0; jj < n; jj++) {
            if (jj > 0)
                sb.append (".");

            sb.append (bytes [offset + jj] & 0xFF);
        }
    }

    public static String		arraydump (
        byte [] 					bytes,
        int 						offset,
        int 						n
    )
    {
        StringBuffer	sb = new StringBuffer ();
        arraydump (sb, bytes, offset, n);
        return (sb.toString ());
    }

    /**
     *  returns <code>obj == null ? 1 : obj.hashCode ()</code>
     */
    public static int       xhashCode (Object obj) {
        return (obj == null ? 1 : obj.hashCode ());
    }

    /**
     *  Adds up hash codes of all array elements, plus array length.
     */
    public static int       arrayHashCode (Object [] arr) {
        if (arr == null)
            return (0);

        int     ret = arr.length;

        for (int ii = 0; ii < arr.length; ii++)
            ret += xhashCode (arr [ii]);

        return (ret);
    }

    /**
     * Method identical to "obj1.equals(obj2)", it also handles <code>null</code> values.
     */
    public static boolean xequals(Object obj1, Object obj2)
    {
        return obj1 == obj2 || // shortcut
            ((obj1 != null) ? (obj2 != null) && obj1.equals (obj2) : obj2 == null);
    }

    /**
     * Method identical to "obj1.compareTo(obj2)==0", it also handles <code>null</code> values.
     */
    public static <T> boolean xcompare(Comparable<T> obj1, T obj2)
    {
        return obj1 == obj2 || // shortcut
            (obj1 != null ? obj1.compareTo (obj2)==0 : obj2 == null);
    }


    /**
     * Identical to equals() but handles Number.equals() problem:
     *
     * ( new Integer (1).equals (new Long (1)) != true )
     *
     * This method allows to compare different Number types by actual value.
     */
    public static boolean xequals2(Object o1, Object o2)
    {
        if (o1 == o2)
            return true;

        if (o1 == null || o2 == null)
            return o1 == o2;

        if (o1 instanceof Number && o2 instanceof Number) {
            Class c1 = o1.getClass ();
            Class c2 = o2.getClass ();

            if (c1 == c2)
                return o1.equals (o2);

            if (c1 != Double.class && c1 != Float.class && c2 != Double.class && c2 != Float.class) {
                return ((Number) o1).longValue () == ((Number) o2).longValue ();
            } else {
                return ((Number) o1).doubleValue () == ((Number) o2).doubleValue ();
            }
        } else {
            return o1.equals (o2);
        }
    }

    public static final double SMALL_NUMBER = 0.0000000000000001;

    /**
     * Identical to equals() but handles Number.equals() problem:
     *
     * ( new Integer (1).equals (new Long (1)) != true )
     *
     * This method allows to compare different Number types by actual value.
     */
    public static boolean equalsEpsilon(Object o1, Object o2)
    {
        if (o1 == o2) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return o1 == o2;
        }

        if (o1 instanceof Number && o2 instanceof Number) {
            Class c1 = o1.getClass ();
            Class c2 = o2.getClass ();

            if (c1 == c2) {
                return o1.equals (o2);
            }

            if (c1 != Double.class && c1 != Float.class && c2 != Double.class && c2 != Float.class) {
                return ((Number) o1).longValue () == ((Number) o2).longValue ();
            } else {
                return Math.abs (((Number) o1).doubleValue () - ((Number) o2).doubleValue ()) < SMALL_NUMBER;
            }
        } else {
            return o1.equals (o2);
        }
    }

    /**
     * Given a Class object, attempts to find its .class location [returns null
     * if no such definition can be found]. Use for testing/debugging only.
     * @param cls class to lookup
     * @return URL that points to the class definition [null if not found].
     * (From http://www.javaworld.com/javaqa/2003-07/01-qa-0711-classsrc_p.html)
     */
    public static URL getClassLocation(final Class cls)
    {
        if (cls == null)
            throw new IllegalArgumentException ("null input: cls");

        URL result = null;
        final String clsAsResource = cls.getName ().replace ('.', '/').concat (".class");

        final ProtectionDomain pd = cls.getProtectionDomain ();
        // java.lang.Class contract does not specify if 'pd' can ever be null;
        // it is not the case for Sun's implementations, but guard against null
        // just in case:
        if (pd != null) {
            final CodeSource cs = pd.getCodeSource ();
            // 'cs' can be null depending on the classloader behavior:
            if (cs != null)
                result = cs.getLocation ();

            if (result != null) {
                // Convert a code source location into a full class file location
                // for some common cases:
                if ("file".equals (result.getProtocol ())) {
                    try {
                        if (result.toExternalForm ().endsWith (".jar") ||
                            result.toExternalForm ().endsWith (".zip"))
                            result = new URL ("jar:".concat (result.toExternalForm ())
                                              .concat ("!/").concat (clsAsResource));
                        else if (new File (result.getFile ()).isDirectory ())
                            result = new URL (result, clsAsResource);
                    } catch (MalformedURLException ignore) {}
                }
            }
        }

        if (result == null) {
            // Try to find 'cls' definition as a resource; this is not
            // documented to be legal, but Sun's implementations seem to allow this:
            final ClassLoader clsLoader = cls.getClassLoader ();

            result = clsLoader != null ?
                clsLoader.getResource (clsAsResource) :
                ClassLoader.getSystemResource (clsAsResource);
        }

        return result;
    }

    /**
     * Return string byte length (encoded as UTF8),
     * same as s.getBytes("UTF8").length but faster
     */
    public static int strlenUTF8(String s)
    {
        int i, len = s.length (), cnt = 0;
        for (i = 0; i < len; i++) {
            char ch = s.charAt (i);
            if (ch <= 0x7F) {
                cnt += 1;
            } else if (ch <= 0x7FF) {
                cnt += 2;
            } else if (ch <= 0x7FFF) {
                cnt += 3;
            } else if (ch <= 0x7FFFF) {
                cnt += 4;
            } else {
                throw new RuntimeException ("Character is a way too big: " + Long.toHexString (ch));
            }
        }
        return cnt;
    }

    private static long mLastUsedMemory;
    private static long mLastTotalMemory;

    /**
     * Prints current memory usage and memory usage delta to System.out.
     */
    public static synchronized void dumpMemoryUsage(String msgText)
    {

        System.gc ();
        System.runFinalization ();
        System.gc ();

        long freeMemory = Runtime.getRuntime ().freeMemory ();
        long totalMemory = Runtime.getRuntime ().totalMemory ();

        StringBuffer msg = new StringBuffer (256);
        msg.append ("(M)");
        if (msgText != null) {
            msg.append (" [");
            msg.append (msgText);
            msg.append (']');
        }

        msg.append (" JVM Memory: ");
        msg.append (freeMemory * 100 / totalMemory);
        msg.append ("% free.");

        if (mLastTotalMemory != totalMemory) {
            msg.append (" (*) Total memory changed to: ");
            msg.append (totalMemory);
            mLastTotalMemory = totalMemory;
        }

        long newUsedMemory = totalMemory - freeMemory;

        msg.append (" Used memory change: ");
        msg.append (newUsedMemory - mLastUsedMemory);
        msg.append (" bytes");

        mLastUsedMemory = newUsedMemory;

        System.out.println (msg.toString ());
    }

    /**
     * Return a comma separated list of elements or "null".
     * @param array - array to print
     */
    public static String printArray(Object[] array)
    {
        if (array == null)
            return "null";

        StringBuffer sb = new StringBuffer ("[");
        if (array.length > 0) {
            sb.append (array[0]);
            for (int i = 1; i < array.length; i++) {
                sb.append (", ");
                sb.append (array[i]);
            }
        }
        sb.append ("]");
        return sb.toString ();
    }

    /**
     * @return stack trace of given throwable as String
     */
    public static String printStackTrace (Throwable t) {
        StringWriter	swr = new StringWriter (512);
        PrintWriter		pwr = new PrintWriter (swr);
        t.printStackTrace(pwr);
        pwr.close();
        return swr.toString();
    }

    /** @return Array of all interfaces implemented by given class (calls cls.getInterfaces() recursively), never null */
    public static Class [] getClassInterfaces (Class cls) {
        List <Class> result = new ArrayList<Class> ();

        if (cls.isInterface())
            result.add (cls);


        Class c = cls;
        while (c != null) {
            Class [] interfaces = c.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                Class cc = interfaces [i];
                if ( ! result.contains(cc))  // joined multi inheritance
                    result.add(cc);
            }
            c = c.getSuperclass();
        }
        return result.toArray(new Class [result.size()]);
    }

    /** @return true if given cls is instanceof interface specified by className */
    public static boolean isntanceOf (Class cls, String className) {

        Class c = cls;
        while (c != null) {
            if (className.equals(c.getName()))
                return true;

            Class [] interfaces = c.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (className.equals(interfaces [i].getName()))
                    return true;
            }
            c = c.getSuperclass();
        }
        return false;
    }


    /**
     * @param propName System property name
     * @param defaultValue default value
     * @param minValue minimum value (inclusive) or Long.MIN_VALUE
     * @param maxValue maximum value (inclusive) or Long.MAX_VALUE
     * @return long property value
     */
    public static long getLongSystemProperty (String propName, long defaultValue, long minValue, long maxValue) {
        long result =
            Long.parseLong(System.getProperty (propName, String.valueOf (defaultValue)));

        if (result < minValue) {
            System.err.println("Property \"" + propName + "\" cannot be less than " + minValue);
            result = minValue;
        }
        if (result > maxValue) {
            System.err.println("Property \"" + propName + "\" cannot be more than " + maxValue);
            result = maxValue;
        }
        return result;
    }

    public static int getIntSystemProperty (String propName, int defaultValue, int minValue, int maxValue) {
        return (int) getLongSystemProperty(propName, defaultValue, minValue, maxValue);
    }


    /**
     * @param propName System property name
     * @param defaultValue default value
     * @param minValue minimum value (inclusive) or Double.MIN_VALUE
     * @param maxValue maximum value (inclusive) or Double.MAX_VALUE
     * @return double property value
     */
    public static double getDoubleSystemProperty (String propName, double defaultValue, double minValue, double maxValue) {
        double result =
            Double.parseDouble(System.getProperty (propName, String.valueOf (defaultValue)));

        if (result < minValue) {
            System.err.println("Property \"" + propName + "\" cannot be less than " + minValue);
            result = minValue;
        }
        if (result > maxValue) {
            System.err.println("Property \"" + propName + "\" cannot be more than " + maxValue);
            result = maxValue;
        }
        return result;
    }

    /**
     *  Return the length of the specified array, or 0 if the array is null.
     */
    public static int          arraylen (Object [] array) {
        return (array == null ? 0 : array.length);
    }

    /**
     *  Append the element to the specified array, returning a new array of the same
     *  type. If the array is null, and the element is not, construct an array of
     *  components whose type is that of the element.
     *
     *  @exception IllegalArgumentException If both arguments are null.
     */
    public static Object []    arrayadd (Object [] array, Object newObject) {
        return (arrayadd (array, arraylen (array), newObject));
    }

    /**
     *  Insert the element into the specified array, returning a new array of the same
     *  type. If the array is null, and the element is not, construct an array of
     *  components whose type is that of the element.
     *
     *  @exception IllegalArgumentException
     *                          If both arguments are null.
     *  @exception ArrayIndexOutOfBoundsException
     *                          If <code>atIdx</code> is greater than array length.
     */
    public static Object []    arrayadd (Object [] array, int atIdx, Object newObject) {
        if (array == null && newObject == null)
            throw new IllegalArgumentException ("array == null && newObject == null");

        int                 oldDim = arraylen (array);

        if (atIdx < 0 || atIdx > oldDim)
            throw new ArrayIndexOutOfBoundsException (atIdx);

        Class               compType =
            array == null ?
                newObject.getClass () :
                array.getClass ().getComponentType ();

        Object []           ret = (Object []) Array.newInstance (compType, oldDim + 1);

        if (array != null) {
            System.arraycopy (array, 0, ret, 0, atIdx);
            System.arraycopy (array, atIdx, ret, atIdx + 1, oldDim - atIdx);
        }

        ret [atIdx] = newObject;

        return (ret);
    }

    /**
     *  Remove an element from the specified array, returning a new array of the same
     *  type.
     *
     *  @exception ArrayIndexOutOfBoundsException
     *                          If <code>atIdx</code> is out of bounds.
     */
    public static Object []    arraydel (Object [] array, int atIdx) {
        int                 oldDim = array.length;

        if (atIdx < 0 || atIdx >= oldDim)
            throw new ArrayIndexOutOfBoundsException (atIdx);

        Class               compType = array.getClass ().getComponentType ();
        Object []           ret = (Object []) Array.newInstance (compType, oldDim - 1);
        int                 shiftIdx = atIdx + 1;

        System.arraycopy (array, 0, ret, 0, atIdx);
        System.arraycopy (array, shiftIdx, ret, atIdx, oldDim - shiftIdx);

        return (ret);
    }

    /**
     *  Find an element in the specified array that equals to the specified element
     *  and remove it. If element is not found, return <code>array</code>.
     */
    public static Object []     arraydel (Object [] array, Object elem) {
        int             idx = indexOf (array, elem);

        if (idx < 0)
            return (array);

        return (arraydel (array, idx));
    }

    /**
     *  Find an element in the specified array that equals to the specified element
     *  and return its index, or -1 if not found.
     */
    public static int           indexOf (Object [] array, Object elem) {
        int             len = arraylen (array);

        for (int ii = 0; ii < len; ii++)
            if (xequals (array [ii], elem))
                return (ii);

        return (-1);
    }
    
    public static void          format (
        StringBuffer                out,
        Object                      obj, 
        Justification               j,
        int                         width,
        String                      clip
    )
    {
        String                      s = obj.toString ();
        int                         length = s.length ();
        int                         diff = width - length;
        
        if (diff < 0) {
            int                     clipLength = clip.length ();
            int                     showLength = width - clipLength;
            
            if (showLength > 0) {
                out.append (s, 0, showLength);
                showLength = 0;
            }
            
            out.append (clip, 0, clipLength + showLength);
        }
        else {
            int                     lpad, rpad;
            
            switch (j) {
                case LEFT:      lpad = diff;    rpad = 0;   break;
                case RIGHT:     rpad = diff;    lpad = 0;   break;
                case CENTER:    lpad = diff / 2;    rpad = diff - lpad; break;
                default:    throw new RuntimeException ("Unrecognized: " + j);
            }
            
            for (int ii = 0; ii < lpad; ii++)
                out.append (" ");
            
            out.append (s);
            
            for (int ii = 0; ii < rpad; ii++)
                out.append (" ");
        }        
    }
}
