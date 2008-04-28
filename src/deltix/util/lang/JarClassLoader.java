package deltix.util.lang;

import deltix.util.Util;
import deltix.util.io.IOUtil;
import java.io.*;
import java.util.*;
import java.util.jar.*;

/**
 *  Caches the contents of a jar file (selectively) mainly for the purpose
 *  of loading classes.
 */
public class JarClassLoader extends AbstractClassLoader {
    private final JarFile               mJarFile;
    
    public JarClassLoader (JarFile f, ClassLoader parent, boolean searchParentFirst) {
        super (parent, searchParentFirst);
        mJarFile = f;
    }
        
    @Override
    protected byte []           loadClassBytes (String name) 
        throws ClassNotFoundException 
    {
        JarEntry    e = mJarFile.getJarEntry (classNameToResourcePath (name));
        
        if (e == null)
            throw new ClassNotFoundException (name);
        
        InputStream is = null;

        try {
            is = mJarFile.getInputStream (e);
            return (IOUtil.readBytes (is));
        } catch (Exception x) {
            throw new ClassNotFoundException (
                "Failed to load class " + name,
                x
            );
        } finally {
            Util.close (is);
        }
    }
    
    public void                 loadAllClasses (Collection <Class <?>> classes)
        throws ClassNotFoundException
    {
        for (
            Enumeration <JarEntry> entries = mJarFile.entries ();
            entries.hasMoreElements ();
        )
        {
            JarEntry    e = entries.nextElement ();
            String      name = e.getName ();
            
            if (name.endsWith (".class")) 
                classes.add (loadClass (resourcePathToClassName (name)));            
        }
    }
    
    public void                 loadAllClassesSkipErrors (
        Collection <Class <?>>      classes
    )
    {
        for (
            Enumeration <JarEntry> entries = mJarFile.entries ();
            entries.hasMoreElements ();
        )
        {
            JarEntry    e = entries.nextElement ();
            String      name = e.getName ();
            
            if (name.endsWith (".class")) {
                try {
                    classes.add (loadClass (resourcePathToClassName (name)));
                } catch (ClassNotFoundException x) {
                    // Skip
                }
            }
        }
    }
    /*
    public static void          main (String [] args) throws Exception {
        JarClassLoader               jcl = 
            new JarClassLoader (            
                new JarFile ("D:/dev/build/dxstrats.jar"),
                JarClassLoader.class.getClassLoader (),
                false
            );
        
        Collection <Class <?>>  classes = new ArrayList <Class <?>> ();
        
        jcl.loadAllClasses (classes);
        
        for (Class <?> c : classes) {
            System.out.println (c + " from " + c.getClassLoader ());
        }
    }  
     */  
}
