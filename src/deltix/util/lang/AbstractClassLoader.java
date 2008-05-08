package deltix.util.lang;

import deltix.util.Util;
import deltix.util.io.IOUtil;
import java.io.*;
import java.net.URL;
import java.util.logging.Level;

/**
 *  Utility class which adapts the ClassLoader base class to
 *  the simple paradigm of <code>byte [] loadClassBytes (String name)</code>.
 *  Additionally, this class allows the control over whether this class loader
 *  or the parent class loader is searched for resources first.
 * 
 *  @see #setSearchParentFirst
 */
public abstract class AbstractClassLoader extends ClassLoader {
    public static String        classNameToResourcePath (String className) {
        return (className.replace ('.', '/') + ".class");
    }
    
    public static String        resourcePathToClassName (String path) {
        if (!path.endsWith (".class"))
            throw new IllegalArgumentException (
                path + " does not end with .class"
            );
        
        return (path.substring (0, path.length () - 6).replace ('/', '.'));
    }
    
    private boolean             mSearchParentFirst;
    
    /**
     *  Constructs an instance of AbstractClassLoader
     * 
     *  @param parent                Parent class loader.
     *  @param searchParentFirst
     *      Determines if classes will be first searched in the parent or this
     *      class loader. This flag is important when classes are or may be present
     *      both in this class loader and its parent. in that case, the flag 
     *      determines which class will be returned.
     */
    protected AbstractClassLoader (
        ClassLoader                     parent, 
        boolean                         searchParentFirst
    ) 
    {
        super (parent);
        mSearchParentFirst = searchParentFirst;
    }

    @Override
    protected synchronized Class <?>    loadClass (String name, boolean resolve)
        throws ClassNotFoundException
    {
        Class   c = findLoadedClass (name);
        
        if (name.equals ("deltix.qsrv.hf.blocks.InstrumentState"))
            System.out.println ();
        
        if (c == null) {
            if (mSearchParentFirst) {
                try {
                    c = getParent ().loadClass (name);
                } catch (ClassNotFoundException e) {
                    c = findClass (name);
                }
            }
            else {
                try {
                    c = findClass (name);                    
                } catch (ClassNotFoundException e) {
                    c = getParent ().loadClass (name);
                }
            }
        }
        
        if (resolve) 
            resolveClass (c);
        
        return c;
    }
    
    /**
     *  
     * 
     *  @param flag     Whether the parent class laoder should be searched
     *                  first for all class names.
     */
    public synchronized final void  setSearchParentFirst (boolean flag) {
        mSearchParentFirst = flag;
    }
    
    @Override
    protected Class <?>             findClass (String name)
        throws ClassNotFoundException
    {
        byte []     b;
        
        try {
            b = findResourceAsByteArray (classNameToResourcePath (name));
        } catch (Exception iox) {
            Util.LOGGER.log (Level.WARNING, "Failed to read " + name, iox);
            return (null);
        }
        
        if (b == null)
            throw new ClassNotFoundException (name);

        return (defineClass (name, b, 0, b.length));     
    }
    
    protected byte []               findResourceAsByteArray (String name) 
        throws IOException, InterruptedException
    {
        InputStream     is = findResourceAsStream (name);
        
        if (is == null)
            return (null);
        
        try {
            return (IOUtil.readBytes (is));        
        } finally {
            Util.close (is);
        }
    }
    
    protected InputStream           findResourceAsStream (String name)
        throws IOException 
    {
        URL     url = findResource (name);
        
        try {
            return (url == null ? null : url.openStream ());
        } catch (IOException iox) {
            Util.LOGGER.log (Level.WARNING, "Failed to open " + url, iox);
            return (null);
        }
    }
    
    protected InputStream           findResourceAsStreamNoX (String name) {
        try {
            return (findResourceAsStream (name));
        } catch (IOException iox) {
            Util.LOGGER.log (Level.WARNING, "Failed to open " + name, iox);
            return (null);
        }
    }
        
    @Override
    public final InputStream              getResourceAsStream (String name) {
        InputStream     is;
        
        if (mSearchParentFirst) {        
            is = getParent ().getResourceAsStream (name);
        
            if (is == null) 
                is = findResourceAsStreamNoX (name);
        }
        else {        
            is = findResourceAsStreamNoX (name);
        
            if (is == null) 
                is = getParent ().getResourceAsStream (name);
        }
        
        return (is);
    }
    
    @Override
    public final URL                      getResource(String name) {
        URL         url;
        
        if (mSearchParentFirst) {        
            url = getParent ().getResource (name);
        
            if (url == null) 
                url = findResource (name);
        }
        else {        
            url = findResource (name);
        
            if (url == null) 
                url = getParent ().getResource (name);
        }
            
        return url;
    }                  
}
