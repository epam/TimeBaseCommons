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
import com.epam.deltix.gflog.api.LogLevel;
import com.epam.deltix.util.io.ByteArrayInputStreamEx;
import com.epam.deltix.util.io.ByteArrayOutputStreamEx;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.*;

/**
 * Provides helper methods to compile one or several classes on-the-fly.
 */
public class JavaCompilerHelper {
    private static final Log LOG = LogFactory.getLog(JavaCompilerHelper.class);
    private static final JavaCompiler           JAVA_COMPILER_INSTANCE;
    private static final JavaFileManager        JAVA_FILEMGR_INSTANCE;

    private static final String toolsJarClassLoader = "com.sun.tools.javac.api.JavacTool";

    static {
        JAVA_COMPILER_INSTANCE = getJavaCompilerInstance();
        if (JAVA_COMPILER_INSTANCE == null)
            throw new ExceptionInInitializerError("Cannot instantiate Java Compiler using ToolProvider");
        JAVA_FILEMGR_INSTANCE = JAVA_COMPILER_INSTANCE.getStandardFileManager(null, null, null);
    }

    private SpecialJavaFileManager  fileManager;
    private SpecialClassLoader      cl;

    public JavaCompilerHelper (ClassLoader loader) {
        if (loader instanceof ClassDirectory)
            init (loader, (ClassDirectory) loader);
        else {
            cl = new SpecialClassLoader(loader);
            fileManager = new SpecialJavaFileManager (JAVA_FILEMGR_INSTANCE, cl);
        }
    }

    private static JavaCompiler getJavaCompilerInstance() {
        JavaCompiler compiler;
        try {
            Class<? extends JavaCompiler> c = Class.forName(toolsJarClassLoader, false,
                    Thread.currentThread().getContextClassLoader()).asSubclass(JavaCompiler.class);
            compiler = c.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            compiler = ToolProvider.getSystemJavaCompiler();
        }
        return compiler;
    }
    
    public JavaCompilerHelper (ClassLoader loader, ClassDirectory classDir) {
        init (loader, classDir);
    }

    private void        init (ClassLoader loader, ClassDirectory classDir) {
        cl = (classDir instanceof SpecialClassLoader) ? (SpecialClassLoader) classDir : new SpecialClassLoader(loader);
        
        fileManager = 
            new SpecialJavaFileManager (
                new ClassLoaderJavaFileManager (JAVA_FILEMGR_INSTANCE, classDir), 
                cl
            );
    }

    public ClassLoader  getClassLoader () {
        return (cl);
    }

    public Class<?> compileClass (String className, String code) throws ClassNotFoundException {
        List<MemorySource> compilationUnits = Arrays.asList(new MemorySource(className, code));
        Writer out = new PrintWriter(System.err);
        DiagnosticCollector<JavaFileObject> dianosticListener = new DiagnosticCollector<>();
        final String optionString = System.getProperty("javac.options");
        final Iterable<String> options = optionString == null ? null : Arrays.asList(optionString.split(" "));
        JavaCompiler.CompilationTask compile = JAVA_COMPILER_INSTANCE.getTask(out, fileManager, dianosticListener, options, null, compilationUnits);
        final boolean ok;
        synchronized (JAVA_COMPILER_INSTANCE) {
            ok = compile.call();
        }

        final boolean hasDiagnostic = dianosticListener.getDiagnostics().size() > 0;
        StringBuilder sb = null;
        if (hasDiagnostic) {
            sb = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> s : dianosticListener.getDiagnostics()) {
                sb.append(s).append(Util.NATIVE_LINE_BREAK);
            }
            if (ok)
                LOG.log(LogLevel.DEBUG).append(sb.toString()).commit();
        }

        if (ok) {
            //Util.LOGGER.info(code);
            return cl.findClass (className);
        }
        else
            throw new CompilationExceptionWithDiagnostic (
                "compilation failed:\n" + (sb != null ? sb : ""), 
                code,
                dianosticListener.getDiagnostics()
            );
    }

    public Map<String, Class<?>> compileClasses(Map<String, String> mapClassName2Code) throws ClassNotFoundException {
//        for (String code : mapClassName2Code.values())
//            Util.LOGGER.warning(code);

        final ArrayList<MemorySource> compilationUnits = new ArrayList<>(mapClassName2Code.size());
        for (Map.Entry<String, String> entry : mapClassName2Code.entrySet()) {
            compilationUnits.add(new MemorySource(entry.getKey(), entry.getValue()));
        }
        Writer out = new PrintWriter(System.err);
        DiagnosticCollector<JavaFileObject> dianosticListener = new DiagnosticCollector<>();
        final String optionString = System.getProperty("javac.options");
        final Iterable<String> options = optionString == null ? null : Arrays.asList(optionString.split(" "));
        JavaCompiler.CompilationTask compile = JAVA_COMPILER_INSTANCE.getTask(out, fileManager, dianosticListener, options, null, compilationUnits);
        final boolean ok;
        synchronized (JAVA_COMPILER_INSTANCE) {
            ok = compile.call();
        }

        final boolean hasDiagnostic = dianosticListener.getDiagnostics().size() > 0;
        StringBuilder sb = null;
        if (hasDiagnostic) {
            sb = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> s : dianosticListener.getDiagnostics()) {
                sb.append(s).append(Util.NATIVE_LINE_BREAK);
            }
            if (ok)
                LOG.log(LogLevel.DEBUG).append(sb.toString()).commit();
        }

        if (ok) {
            final HashMap<String, Class<?>> result = new HashMap<>(mapClassName2Code.size());
            for (String className : mapClassName2Code.keySet())
                result.put(className, cl.findClass(className));

            return result;
        } 
        else {
            StringBuilder   code = new StringBuilder ();
            
            for (Map.Entry<String, String> entry : mapClassName2Code.entrySet ()) {
                code.append (entry.getKey ());
                code.append (":\n--------------------------------------------\n");
                code.append (entry.getValue ());
                code.append ("\n\n");
            }
            
            throw new CompilationExceptionWithDiagnostic (
                "compilation failed:\n" + (sb != null ? sb : ""), 
                code.toString (),
                dianosticListener.getDiagnostics()
            );
        }
    }

    private static class MemorySource extends SimpleJavaFileObject {
        private String src;

        public MemorySource(String name, String src) {
            super(URI.create("string:///" + name.replace ('.', '/') + ".java"), Kind.SOURCE);
            this.src = src;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return src;
        }

        @Override
        public OutputStream openOutputStream() {
            throw new IllegalStateException();
        }

        @Override
        public InputStream openInputStream() {
            return new ByteArrayInputStream(src.getBytes());
        }
    }


    private static class SpecialJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        private SpecialClassLoader xcl;

        public SpecialJavaFileManager(JavaFileManager sjfm, SpecialClassLoader xcl) {
            super(sjfm);
            this.xcl = xcl;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String name, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            MemoryByteCode mbc = new MemoryByteCode(name);
            xcl.addClass(name, mbc);
            return mbc;
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return xcl;
        }

        @Override
        public String inferBinaryName(Location location, JavaFileObject file) {
            if (file instanceof JarEntryObject) {
                return ((JarEntryObject) file).binaryName();
            } else {
                return fileManager.inferBinaryName(location, file);
            }
        }

        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
            if (location == StandardLocation.PLATFORM_CLASS_PATH) {
                return fileManager.list(location, packageName, kinds, recurse);
            } else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
                Iterable<JavaFileObject> list = fileManager.list(location, packageName, kinds, recurse);

                List<JavaFileObject> objects = JarScanner.list(xcl, packageName);
                for (JavaFileObject object : list)
                    objects.add(object);

                return objects;
            }

            return super.list(location, packageName, kinds, recurse);
        }
    }


    private static class MemoryByteCode extends SimpleJavaFileObject {
        private ByteArrayOutputStreamEx baos;

        public MemoryByteCode(String name) {
            super(URI.create("byte:///" + name + ".class"), Kind.CLASS);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            throw new IllegalStateException();
        }

        @Override
        public OutputStream openOutputStream() {
            baos = new ByteArrayOutputStreamEx();
            return baos;
        }

        @Override
        public InputStream openInputStream() {
            return (new ByteArrayInputStreamEx (baos));
        }

        public byte[] getBytes() {
            return baos.toByteArray();
        }
    }

    public static class SpecialClassLoader
        extends ClassLoader 
        implements ClassDirectory
    {
        private Map<String, MemoryByteCode> m = new HashMap<>();

        public SpecialClassLoader(ClassLoader parent) {
            super(parent);
        }

        @Override
        public InputStream      getResourceAsStream (String name) {            
            if (name.endsWith (".class")) {
                String          cname = name.substring (0, name.length () - 6).replace ('/', '.');
                MemoryByteCode  bc = m.get (cname);
                
                if (bc != null)
                    return (bc.openInputStream ());
            }
            
            return super.getResourceAsStream (name);
        }
        
        @Override
        public Class <?>     findClass(String name)
            throws ClassNotFoundException 
        {
            MemoryByteCode mbc = m.get(name);
            if (mbc == null) {
                mbc = m.get(name.replace(".", "/"));
                if (mbc == null) {
                    return super.findClass(name);
                }
            }
            final Class<?> clazz = findLoadedClass (name);
            return clazz == null ? defineClass(name, mbc.getBytes(), 0, mbc.getBytes().length) : clazz;
        }

        public void addClass(String name, MemoryByteCode mbc) {
            m.put(name, mbc);
        }

        public Collection <Class<?>> listClassesForPackage (String packageName) {
            if (packageName != null && packageName.isEmpty ())
                packageName = null;
            else
                packageName += '.';
            
            ArrayList <Class <?>>       ret = new ArrayList <> ();
            
            for (String cname : m.keySet ()) {
                if (packageName == null ? !cname.contains (".") : cname.startsWith (packageName)) {
                    try {
                        ret.add (loadClass (cname));
                    } catch (ClassNotFoundException x) {
                        throw new RuntimeException ("Unable to load own class " + cname, x);
                    }
                }
            }
            
            return (ret);
        }                
    }
}