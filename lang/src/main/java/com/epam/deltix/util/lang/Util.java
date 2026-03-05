/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.lang;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;

import java.io.*;
import java.lang.management.*;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.prefs.Preferences;

/** Set of useful methods */
public class Util {
    public static final boolean  IS64BIT            = "64".equals(System.getProperty("sun.arch.data.model"));
    public static final boolean  IS32BIT            = "32".equals(System.getProperty("sun.arch.data.model"));

    public static final String   LOGGER_NAME        = "deltix.util";
    private static final Log     LOG                = LogFactory.getLog(Util.class);
    public static final boolean  IS_WINDOWS_OS      = System.getProperty ("path.separator").equals(";");
    public static final String   NATIVE_LINE_BREAK  = System.getProperty("line.separator");
    public static final String[] EMPTY_STRING_ARRAY = {};
    public static final boolean  QUIET              = Boolean.getBoolean("quiet");

    // https://stackoverflow.com/questions/3038392/do-java-arrays-have-a-maximum-size
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private static final int HALF_OF_MAX_INTEGER = Integer.MAX_VALUE / 2;

    public static void collectLocalFiles(String path, Collection<String> files) {
        File file = new File(path);
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (String filename : children) {
                    collectLocalFiles(file.getAbsolutePath() + File.separator + filename, files);
                }
            }
        } else {
            files.add(file.getAbsolutePath());
        }
    }

    public static void collectLocalFiles(String path, Collection<String> files, Filter<File> filter) {
        File file = new File(path);
        if (filter.accept(file)) {
            if (file.isDirectory()) {
                String[] children = file.list();
                if (children == null) {
                    // Either dir does not exist or is not a directory
                } else {
                    for (String filename : children) {
                        collectLocalFiles(file.getAbsolutePath() + File.separator + filename, files, filter);
                    }
                }
            } else {
                files.add(file.getAbsolutePath());
            }
        }
    }

    /**
     * WARN: Consider using {@link #growArraySize(int, int)} instead, if you do not need the produced value
     * to be double of the original value and if the produced value will be used to create an array.
     */
    public static int           doubleUntilAtLeast (int a, int limit) {
        if (a == 0)
            return limit;

        while (a < limit) {
            if (a > HALF_OF_MAX_INTEGER) {
                throw new IllegalArgumentException("Cannot double " + a + " to at least " + limit + ", it will overflow");
            }
            // Double value
            a = a << 1;
        }

        return (a);
    }

    /**
     * Returns a new size of the array that is at least <code>minSize</code>.
     * Similar to {@link #doubleUntilAtLeast(int, int)}, but allows to specify
     *
     * @param currentSize Current size of the array
     * @param minSize required minimum value
     */
    public static int growArraySize(int currentSize, int minSize) {
        if (currentSize == 0) {
            return minSize;
        }

        while (currentSize < minSize) {
            if (currentSize > HALF_OF_MAX_INTEGER) {
                if (minSize > MAX_ARRAY_SIZE) {
                    throw new IllegalArgumentException("Cannot grow " + currentSize + " to at least " + minSize + ", the limit is higher than MAX_ARRAY_SIZE");
                } else {
                    return MAX_ARRAY_SIZE;
                }
            }

            currentSize = currentSize << 1;
        }

        return currentSize;
    }

    /**
     *  Returns the sign of a - b
     */
    public static int           compare (long a, long b) {
        return a > b ? 1 : (a == b ? 0 : -1);
    }

    /**
     *  Returns the sign of a - b
     */
    public static int           compare (double a, double b) {
        double        diff = a - b;

        if (diff < 0)
            return (-1);

        if (diff > 0)
            return (1);

        return (0);
    }

    /**
     *  Compare two CharSequences for equality. A null equals null.
     */
    public static boolean       equals (CharSequence s1, CharSequence s2) {
        return (compare (s1, s2, true) == 0);
    }

    /**
     *  Compare two CharSequences for equality. A null equals null.
     */
    public static boolean       equals (CharSequence s1, CharSequence s2, int maxLength) {
        return (compare (s1, s2, maxLength, true) == 0);
    }

    /**
     *  Compare two CharSequences. A null argument is always less than a non-null argument
     *  and is equal to another null argument.
     *
     *  @param fast     When true, use a fast algorithm, which makes a
     *                  char sequence greater than another if it is longer.
     *                  When false, performs lexicographic comparison.
     */
    public static int           compare (CharSequence s1, CharSequence s2, boolean fast) {
        return (compare (s1, s2, 0, fast));
    }

    /**
     *  Compare two CharSequences. A null argument is always less than a non-null argument
     *  and is equal to another null argument.
     *
     *  @param maxLength Only compare the first <code>maxLength</code> characters.
     *                      Send 0 to unlimit.
     *  @param fast     When true, use a fast algorithm, which makes a
     *                  char sequence greater than another if it is longer.
     *                  When false, performs lexicographic comparison.
     */
    public static int           compare (
        CharSequence                s1,
        CharSequence                s2,
        int                         maxLength,
        boolean                     fast
    )
    {
        if (s1 == null)
            if (s2 == null)
                return (0);
            else
                return (-1);
        else if (s2 == null)
            return (1);
        else if (s1 == s2)
            return (0);
        else {
            int         len1 = s1.length ();
            int         len2 = s2.length ();

            if (maxLength > 0) {
                if (maxLength < len1)
                    len1 = maxLength;

                if (maxLength < len2)
                    len2 = maxLength;
            }

            int         diff = len1 - len2;

            if (fast && diff != 0)
                return (diff);

            int         minLength = diff > 0 ? len2 : len1;

            for (int ii = 0; ii < minLength; ii++) {
                int     cdiff = s1.charAt (ii) - s2.charAt (ii);

                if (cdiff != 0)
                    return (cdiff);
            }

            return (diff);
        }
    }

    public static int           fastCompare (CharSequence s1, CharSequence s2) {

        int         len1 = s1.length ();
        int         len2 = s2.length ();

        int         diff = len1 - len2;

        if (diff != 0)
            return (diff);

        for (int ii = 0; ii < len1; ii++) {
            int     cdiff = s1.charAt (ii) - s2.charAt (ii);

            if (cdiff != 0)
                return (cdiff);
        }

        return (diff);
    }

    /**
     * Searches target CharSequence in the source.
     * The source is the character sequence being searched, and the target
     * is the character sequence being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   targetOffset offset of the target string.
     * @param   targetCount  count of the target string.
     * @param   fromIndex    the index to begin searching from.
     */
    public static int indexOf(CharSequence source, int sourceOffset, int sourceCount,
                              CharSequence target, int targetOffset, int targetCount,
                              int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target.charAt(targetOffset);
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source.charAt(i) != first) {
                while (++i <= max && source.charAt(i) != first);
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source.charAt(j) == target.charAt(k); j++, k++);

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

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

    public static void      writeNullableString (String s, DataOutput os)
        throws IOException
    {
        os.writeBoolean (s == null);

        if (s != null)
            os.writeUTF (s);
    }

    public static String    readNullableString (DataInput is)
        throws IOException
    {
        if (is.readBoolean ())
            return (null);

        return (is.readUTF ());
    }

    /**
     *  Loads and instantiates the specified class using the no-argument constructor
     */
    public static Object    newInstance (String className, Object ... args)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, NoSuchMethodException, InvocationTargetException
    {
        return (newInstance (Class.forName (className), args));
    }

    /**
     *  Call a static method of the specified class. Figure out the method
     *  signature from the types of the supplied arguments (which must not contain
     *  null elements).
     */
    public static Object    callStaticMethod (
        String                  className,
        String                  methodName,
        Object ...              args
    )
        throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException
    {
        Class<?>    c = Class.forName (className);
        Class []    paramTypes = new Class [args.length];
        for (int ii = 0; ii < args.length; ii++)
            paramTypes [ii] = args [ii].getClass ();

        Method      m = c.getDeclaredMethod (methodName, paramTypes);
        m.setAccessible (true);
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
        Object ...              args
    )
        throws
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException
    {
        Class []    paramTypes = new Class [args.length];
        for (int ii = 0; ii < args.length; ii++)
            paramTypes [ii] = args [ii].getClass ();

        Method      m = object.getClass ().getDeclaredMethod (methodName, paramTypes);
        m.setAccessible (true);
        return (m.invoke (object, args));
    }


    /**
     *  Call a method of the specified class. Unlike {@linkplain #callNonStaticMethod} figures out the method
     *  signature from the types of the supplied arguments using recursive look up of the types base classes
     */
    public static Object    callNonStaticMethodRecursive (
            Object                  object,
            String                  methodName,
            Object ...              args
     )
        throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException
    {
        final Method[] mm = object.getClass().getDeclaredMethods();
        if (mm == null || mm.length == 0)
            throw new NoSuchMethodException(object.getClass().getName() + "." + methodName);

        Class[] paramTypes = new Class[args.length];
        for (int ii = 0; ii < args.length; ii++)
            paramTypes[ii] = args[ii].getClass();

        m1:
        for (Method method : mm) {
            if (method.getName().equals(methodName)) {
                final Class<?>[] types = method.getParameterTypes();
                for (int i = 0; i < types.length; i++) {
                    Class<?> type = types[i];
                    if (type != paramTypes[i] && !type.isAssignableFrom(paramTypes[i]))
                        continue m1;
                }

                method.setAccessible(true);
                return (method.invoke(object, args));
            }
        }

        throw new NoSuchMethodException(object.getClass().getName() + "." + methodName + " " + Util.printArray(paramTypes));
    }

    @SuppressWarnings ("unchecked")
    public static void      setFieldValue (
        Object                  object,
        String                  fieldName,
        Object                  value
    )
        throws
            NoSuchFieldException,
            IllegalAccessException
    {
        if (object == null)
            throw new NullPointerException(fieldName);


        Field       f = findFieldByName (object, fieldName);
                
        if (f == null)
            throw new NoSuchFieldException (fieldName + " in " + object.getClass ());
        
        f.setAccessible (true);

        final Class<?> type = f.getType();

        if (type.isPrimitive()) {
            if (value == null)
                throw new IllegalArgumentException("Attempt to set NULL value to a field of primitive type (" + f.getDeclaringClass().getSimpleName() + "." + fieldName + ")");

            if (type == long.class)
                f.setLong(object, ((Number) value).longValue ());
            else if (type == int.class)
                f.setInt(object, ((Number) value).intValue ());
            else if (type == short.class)
                f.setInt(object, ((Number) value).shortValue ());
            else if (type == byte.class)
                f.setInt(object, ((Number) value).byteValue ());
            else if (type == double.class)
                f.setDouble(object, ((Number) value).doubleValue ());
            else if (type == float.class)
                f.setDouble(object, ((Number) value).doubleValue ());
            else if (type == boolean.class)
                f.setBoolean(object, (Boolean) value);
            else
                throw new IllegalArgumentException("field type is not supported " + type);
        } else {
            if (type.isEnum () && value instanceof String)
                value = Enum.valueOf ((Class <? extends Enum>) type, (String) value);            
            
            f.set(object, value);
         }
    }

    /**
     * Find class field hierarchically by name
     */
    public static Field findFieldByName(Object object, String fieldName) {
        Field f = null;

        for (
                Class <?>   c = object.getClass ();
                c != Object.class;
                c = c.getSuperclass ()
                )
        {
            try {
                f = c.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException x) {
            }
        }

        return f;
    }

    /**
     *  Call a constructor of the specified class. Figure out the method
     *  signature from the types of the supplied arguments (which must not contain
     *  null elements).
     */
    public static <T> T    newInstance (
        Class <T>               clazz,
        Object ...              args
    )
        throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        Constructor <T>     m;
        int                 numArgs = args.length;
        
        if (numArgs == 0)
            m = clazz.getDeclaredConstructor ();
        else {
            Class <?> []    paramTypes = new Class [numArgs];
            
            for (int ii = 0; ii < numArgs; ii++)
                paramTypes [ii] = args [ii].getClass ();

            m = clazz.getDeclaredConstructor (paramTypes);
        }
        
        m.setAccessible (true);
        return (m.newInstance (args));
    }

    public static <T> T    newInstanceNoX (
        Class <T>               clazz,
        Object ...              args
    )
    {
        try {                        
            return (newInstance (clazz, args));
        } catch (RuntimeException x) {
            throw x;
        } catch (Error x) {
            throw x;
        } catch (Throwable other) {
            throw new RuntimeException (clazz.getName () + " instantiation failed", other);
        }
    }
    
    public static Object    newInstanceNoX (
        String                  className,
        Object ...              args
    )
    {
        try {
            return (newInstance (className, args));
        } catch (RuntimeException x) {
            throw x;
        } catch (Error x) {
            throw x;
        } catch (Throwable other) {
            throw new RuntimeException (className + " instantiation failed", other);
        }
    }

    public static Runnable  methodRunnable (final Object obj, String methodName) {                
        Method      m = null;

        Class <?> cls = obj.getClass ();
        while (true)  {
            try {
                m = cls.getDeclaredMethod (methodName);            
                break;
            } catch (NoSuchMethodException x) {
                cls = cls.getSuperclass ();
                if (cls == null)
                    throw new RuntimeException(x);
                // continue
            }
        }

        assert m != null;

        m.setAccessible(true);

        final Method fm = m;

        return (
                new Runnable() {
                    public void run() {
                        try {
                            fm.invoke(obj);
                        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException x) {
                            throw new RuntimeException(x);
                        }
                    }
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> T unwrap (T instance) {
        while (instance instanceof Wrapper)
            instance = ((Wrapper<T>)instance).getNestedInstance();

        return instance;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unwrap (Object instance, Class<T> upToClass) {
        boolean found = false;
        
        while (!(found = instance.getClass() == upToClass) &&
                instance instanceof Wrapper)
            instance = ((Wrapper<T>)instance).getNestedInstance();

        return found ? (T) instance : null;
    }
    
    /**
     *	Gets to the bottom of the exception.
     */
    public static Throwable		unwrap (Throwable ex) {
        Throwable result = ex;
        while (result != null) {
            Throwable nested;

            nested = result.getCause ();

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
            LOG.error ("Error getting resource  '%s': %s").with(path).with(mux);
            return (null);
        }
    }

    public static void			handleException (Throwable x) {
        LOG.error ("Ignoring (but Logging) Exception %s").with(x);
    }

    public static void			logException (String message, Throwable x) {
        LOG.error ("%s %s").with(message).with(x);
    }


    /**
     *  Closes a Closeable without throwing an exception. Checks for null.
     *  Allows to do:
     *  <pre>
     *  stream = Util.close(stream);
     *  </pre>
     * instead of:
     *  <pre>
     *  Util.close(stream);
     *  stream = null;
     *  </pre>
     * if required
     */
    public static <T extends Closeable> T close (T closeable) {
        if (closeable != null)
            try {
                closeable.close ();
            } catch (Exception x) {
                handleException (x);
            }
        
        return null;
    }

    public static void closeAll(Iterable<? extends Disposable> values) {
        for (Disposable value : values) {
            Util.close(value);
        }
    }

    /**
     *	@return true if two arrays contain identical bytes
     */
    public static boolean			arrayequals (
        byte [] 					arr1,
        int							off1,
        int							len1,
        byte [] 					arr2,
        int							off2,
        int							len2
    )
    {
        if (len1 != len2)
            return false;

        if (arr1 == arr2 && off1 == off2)
            return true;

        for (int i=0; i<len1; i++)
            if (arr1[i+off1] != arr2[i+off2])
                return false;

        return true;
    }

    /**
     *	Returns the difference between the specified segments of
     *	arr1 and arr2, MSBF.
     */
    public static int			arraycomp (
        byte [] 					arr1,
        int							off1,
        int							len1,
        byte [] 					arr2,
        int							off2,
        int							len2
    )
    {
        int		diff = arraycomp (arr1, off1, arr2, off2, Math.min (len1, len2));

        if (diff != 0)
            return (diff);

        return (len1 - len2);
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
        int							len1,
        byte [] 					arr2,
        int							off2,
        int							len2
    )
    {
        int		diff = arrayucomp (arr1, off1, arr2, off2, Math.min (len1, len2));

        if (diff != 0)
            return (diff);

        return (len1 - len2);
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

    public static int       hashCode (long value) {
        return ((int) (value ^ (value >>> 32)));
    }

    public static int       hashCode (double value) {
        return (hashCode (Double.doubleToLongBits (value)));
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

    public static <T extends Comparable <T>> int    xcompare (T o1, T o2) {
        if (o1 == null)
            if (o2 == null)
                return (0);
            else
                return (-1);
        else if (o2 == null)
            return (1);
        else
            return (o1.compareTo (o2));
    }


    /**
     * Method identical to "obj1.equals(obj2)", it also handles <code>null</code> values.
     */
    public static boolean xequals(Object obj1, Object obj2)
    {
        return (obj1 == obj2 || obj1 != null && obj2 != null && obj1.equals (obj2));
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

    public static String printStackTrace(StackTraceElement[] stack) {
        final int stackSize = stack.length;
        if (stackSize > 0) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < stackSize; i++) {
                sb.append(stack[i]);
                if (i < stackSize - 1)
                    sb.append("\n\t");
            }
            return sb.toString();
        }

        return "";
    }

    public static Map<Thread, ThreadInfo>   getAllStackTraces() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        Map<Thread, ThreadInfo> info = new HashMap<Thread, ThreadInfo>();

        for (Thread thread : traces.keySet())
            info.put(thread, bean.getThreadInfo(new long[] {thread.getId()}, true, true)[0]);

        return info;
    }

    public static String       getThreadStackTrace(Thread thread, ThreadInfo info) {
        StringBuilder sb = new StringBuilder("\"" + thread.getName() + "\"" +
                (thread.isDaemon() ? " daemon" : "") + " priority=" + thread.getPriority());

        if (info.getLockName() != null)
            sb.append(" on ").append(info.getLockName());

        if (info.getLockOwnerName() != null)
            sb.append(" owned by \"").append(info.getLockOwnerName()).append("\" id=").append(info.getLockOwnerId());

//        if (isSuspended()) {
//            sb.append(" (suspended)");
//        }
//        if (isInNative()) {
//            sb.append(" (in native)");
//        }

        sb.append('\n');
        sb.append("Thread State: ").append(thread.getState()).append("\n");
        StackTraceElement[] stackTrace = info.getStackTrace();

        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append("\tat " + ste.toString());
            sb.append('\n');
            LockInfo lock = info.getLockInfo();

            if (i == 0 && lock != null) {
                Thread.State ts = info.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on ").append(lock);
                        sb.append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on ").append(lock);
                        sb.append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on ").append(lock);
                        sb.append('\n');
                        break;
                    default:
                }
            }

            for (MonitorInfo mi : info.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked ").append(mi);
                    sb.append('\n');
                }
            }
        }

        LockInfo[] locks = info.getLockedSynchronizers();
        if (locks.length > 0) {
            sb.append("\n\tLocked ownable synchronizers:");
            sb.append('\n');
            for (LockInfo li : locks) {
                sb.append("\t- ").append(li.toString());
                sb.append('\n');
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    public static void printStackTraces () {
        Map <Thread, ThreadInfo> threads = Util.getAllStackTraces ();
            
        for (Map.Entry <Thread, ThreadInfo> e : threads.entrySet ())
            System.out.println (Util.getThreadStackTrace (e.getKey (), e.getValue ()));
    }
    
    public static RuntimeException asRuntimeException(Throwable exception) {
        if (exception instanceof RuntimeException)
            return (RuntimeException) exception;
        else if (exception instanceof Error)
            throw (Error) exception;
        else
            return new RuntimeException(exception);
    }

    public static <T extends Throwable> T findCause(Throwable error, Class<T> causeClass) {
        while (error != null) {
            if (causeClass.isInstance(error))
                return causeClass.cast(error);
            error = error.getCause();
        }
        return null;
    }

    /** @return Array of all interfaces implemented by given class (calls cls.getInterfaces() recursively), never null */
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings("rawtypes")
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
            LOG.error ("Property \"%s\" cannot be less than %s").with(propName).with(minValue);
            result = minValue;
        }
        if (result > maxValue) {
            LOG.error ("Property \"%s\" cannot be more than %s").with(propName).with(maxValue);
            result = maxValue;
        }
        return result;
    }

    public static long getLongSystemProperty (String propName, long defaultValue) {
        return getLongSystemProperty(propName, defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
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
            LOG.error ("Property \"%s\" cannot be less than %s").with(propName).with(minValue);
            result = minValue;
        }
        if (result > maxValue) {
            LOG.error ("Property \"%s\" cannot be more than %s").with(propName).with(maxValue);
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
    public static <T> T []    arrayadd (T [] array, T newObject) {
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
    @SuppressWarnings("unchecked")
    public static <T>  T []    arrayadd (T [] array, int atIdx, T newObject) {
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

        return (T[])ret;
    }

    /**
     *  Remove an element from the specified array, returning a new array of the same
     *  type.
     *
     *  @exception ArrayIndexOutOfBoundsException
     *                          If <code>atIdx</code> is out of bounds.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T []    arraydel (T [] array, int atIdx) {
        int                 oldDim = array.length;

        if (atIdx < 0 || atIdx >= oldDim)
            throw new ArrayIndexOutOfBoundsException (atIdx);

        Class               compType = array.getClass ().getComponentType ();
        T []                ret = (T []) Array.newInstance (compType, oldDim - 1);
        int                 shiftIdx = atIdx + 1;

        System.arraycopy (array, 0, ret, 0, atIdx);
        System.arraycopy (array, shiftIdx, ret, atIdx, oldDim - shiftIdx);

        return (ret);
    }

    /**
     *  Find an element in the specified array that equals to the specified element
     *  and remove it. If element is not found, return <code>array</code>.
     */
    public static <T> T []     arraydel (T [] array, Object elem) {
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

    /**
     *  Find an element in the specified array that equals to the specified element
     *  and return its index, or -1 if not found.
     */
    public static boolean           contains (Object [] array, Object elem) {
        return (indexOf (array, elem) >= 0);
    }

    /**
     *  Find the index of the minimum element. Returns -1 if array is empty.
     */
    public static <T> int           indexOfMinMax (T [] array, Comparator <T> comp, int order) {
        int                             len = arraylen (array);

        if (len == 0)
            return (-1);

        int                             idx = 0;
        T                               minmax = array [0];

        for (int ii = 1; ii < len; ii++) {
            T                           cur = array [ii];

            if (comp.compare (cur, minmax) * order > 0) {
                minmax = cur;
                idx = ii;
            }
        }

        return (idx);
    }

    /**
     *  Find the index of the minimum element. Returns -1 if array is empty.
     */
    public static <T extends Comparable <T>> int indexOfMinMax (T [] array, int order) {
        int                             len = arraylen (array);

        if (len == 0)
            return (-1);

        int                             idx = 0;
        T                               minmax = array [0];

        for (int ii = 1; ii < len; ii++) {
            T                           cur = array [ii];

            if (cur.compareTo (minmax) * order > 0) {
                minmax = cur;
                idx = ii;
            }
        }

        return (idx);
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

    /**
     *  Creates a string that is a copy of the specified char sequence
     *  without calling CharSequence.toString ().
     */
    public static String               toString (CharSequence cs) {
        return (cs.getClass () == String.class ? (String) cs : new StringBuilder (cs).toString ());
    }

    /**
     *  Creates a string that is a copy of the specified char sequence
     *  without calling CharSequence.toString (). Supports null argument.
     */
    public static String               toNullableString (CharSequence cs) {
        return (cs == null) ? null : toString(cs);
    }

    /**
     *  Replicates the String.hashCode () logic for arbitrary CharSequence instances.
     *  Returns 0 for null.
     */
    public static int               hashCode (CharSequence cs) {
        if (cs == null)
            return (0);
        
        if (cs.getClass () == String.class) // String caches hash code
            return (cs.hashCode ());
        
        int         len = cs.length ();
        int         hc = 0;

        for (int i = 0; i < len; i++)
            hc = 31 * hc + cs.charAt (i);

        return (hc);
    }

    public static boolean           isBoxedPrimitive (Class <?> c) {
        return (
            c == Boolean.class || c == Character.class ||
            c == Byte.class || c == Short.class ||
            c == Integer.class || c == Long.class ||
            c == Float.class || c == Double.class
        );
    }

    public static Class <?>         toBoxed (Class <?> c) {
        if (c == boolean.class)
            return (Boolean.class);

        if (c == char.class)
            return (Character.class);

        if (c == byte.class)
            return (Byte.class);

        if (c == short.class)
            return (Short.class);

        if (c == int.class)
            return (Integer.class);

        if (c == long.class)
            return (Long.class);

        if (c == float.class)
            return (Float.class);

        if (c == double.class)
            return (Double.class);

        return (c);
    }

    public static String            getPackagePrefix (Class <?> cls) {
        String      qname = cls.getName ();
        String      sname = cls.getSimpleName ();

        assert qname.endsWith (sname);

        return (qname.substring (0, qname.length () - sname.length ()));
    }

    public static  long           getAvailableMemory () {
        final Runtime     rt = Runtime.getRuntime ();

        return (rt.maxMemory () - rt.totalMemory () + rt.freeMemory ());
    }

    public static long          fractionOfAvailableMemory (double k) {
        final Runtime rt = Runtime.getRuntime();
        final long maxMem = rt.maxMemory();
        final long freeMem = rt.freeMemory();
        final long currentMem = rt.totalMemory();
        final long usedMem = currentMem - freeMem;
        final long availMem = maxMem - usedMem;
        final long mem = (long) (availMem * k);

        return (mem);
    }

    public static int          availableMemoryPercent () {
        final Runtime rt = Runtime.getRuntime();
        final long maxMem = rt.maxMemory();
        final long freeMem = rt.freeMemory();
        final long currentMem = rt.totalMemory();
        final long usedMem = currentMem - freeMem;
        final long availMem = maxMem - usedMem;

        return (int) (availMem * 100 / maxMem);
    }

    public static boolean       asAdministrator () {
        // attempt to set a preference
        try {
            final String path = "/deltix/dummyPref";
            final Preferences prefs = Preferences.systemRoot ().node (path);
            prefs.putLong ("dummyKey",
                           System.currentTimeMillis ());
            prefs.flush ();
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    public static <K,V> HashMap<K,V> newHashMap(int size) {
        return new HashMap<K, V>(1 + (int) (size/0.75));
    }
    
    public static String            getPackage (String className) {
        int                     dot = className.lastIndexOf ('.');
        
        return (dot < 0 ? "" : className.substring (0, dot));
    }
    
    public static String            getSimpleName (String className) {
        int                     dot = className.lastIndexOf ('.');
        
        return (dot < 0 ? className : className.substring (dot + 1));
    }
}