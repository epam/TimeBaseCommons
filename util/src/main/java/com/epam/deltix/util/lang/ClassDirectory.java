package com.epam.deltix.util.lang;

import java.util.*;

/**
 * Lists classes within the specified package.
 * <p>
 * Used by <tt>ClassLoaderJavaFileManager</tt>. Any class loader, which keeps
 * classes outside the CLASSPATH, must implement this interface to make them visible
 * to the on-the-fly Java compiler.   
 * </p>
 */
public interface ClassDirectory {
    public Collection <Class <?>> listClassesForPackage (String packageName);
}
