package deltix.util.lang;

import java.io.*;

/**
 *  Trivial utility class which adapts the ClassLoader base class to
 *  the simple paradigm of <code>byte [] loadClassBytes (String name)</code>
 */
public abstract class AbstractClassLoader extends ClassLoader {
    protected abstract byte []      loadClassBytes (String name)
        throws ClassNotFoundException;
    
    protected Class <?>             findClass (String name)
        throws ClassNotFoundException
    {
        byte []     b = loadClassBytes (name);
        return (defineClass (name, b, 0, b.length));        
    }
}
