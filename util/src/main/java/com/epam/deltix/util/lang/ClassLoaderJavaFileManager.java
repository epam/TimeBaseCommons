package com.epam.deltix.util.lang;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.*;

/**
 * {@link JavaFileManager} implementation based on {@link ClassLoader} and {@link ClassDirectory}
 */
public class ClassLoaderJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final ClassDirectory listClasses;

    public ClassLoaderJavaFileManager(JavaFileManager fileManager, ClassDirectory listClasses) {
        super(fileManager);
        this.listClasses = listClasses;
    }

    @Override
    public Iterable <JavaFileObject> list (
        Location                        location, 
        String                          packageName, 
        Set<JavaFileObject.Kind>        kinds, 
        boolean                         recurse
    )
        throws IOException 
    {
        ArrayList <JavaFileObject>    ret = new ArrayList <JavaFileObject> ();

        // first of all try listClasses
        Collection <Class <?>> clist = listClasses.listClassesForPackage (packageName);

        if (clist == null || clist.isEmpty()) {
            // try one-level recursion here
            final ClassLoader parent = ((ClassLoader) listClasses).getParent();
            if (parent instanceof ClassDirectory) {
                clist = ((ClassDirectory) parent).listClassesForPackage(packageName);
            }
        }

        if (clist != null) {
            for (Class<?> cls : clist)
                ret.add(new ClassBasedJavaFileObject(cls));
        }

        Iterable<JavaFileObject> list = super.list(location, packageName, kinds, recurse);
        for (JavaFileObject javaFileObject : list)
            ret.add (javaFileObject);
            
        return (ret);
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof ClassBasedJavaFileObject)
            return ((ClassBasedJavaFileObject) file).getBinaryName();
        else
            return super.inferBinaryName(location, file);
    }

    private static class ClassBasedJavaFileObject implements JavaFileObject {
        private final Class<?> clazz;

        private ClassBasedJavaFileObject(Class<?> clazz) {
            this.clazz = clazz;
        }

        private String getBinaryName() {
            return clazz.getName();
        }

        @Override
        public Modifier getAccessLevel() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Kind getKind() {
            return Kind.CLASS;
        }

        @Override
        public boolean isNameCompatible(String simpleName, Kind kind) {
            throw new UnsupportedOperationException();
        }

        @Override
        public NestingKind getNestingKind() {
            throw new UnsupportedOperationException();
        }

        @Override
        public URI toUri() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            return "Deltix " + getClass().getSimpleName();
        }

        @Override
        public InputStream openInputStream() throws IOException {
            final String shortClassName;
            if (clazz.isMemberClass()) {
                final String name = clazz.getName();
                shortClassName = name.substring(name.lastIndexOf('.') + 1);
            } else
                shortClassName = clazz.getSimpleName();

            final InputStream is = clazz.getResourceAsStream(shortClassName + ".class");
            if (is == null)
                throw new IllegalStateException(getBinaryName());
            return is;
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Writer openWriter() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getLastModified() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean delete() {
            throw new UnsupportedOperationException();
        }
    }
}
