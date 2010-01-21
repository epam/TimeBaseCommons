package deltix.util.lang;

import javax.tools.*;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: BazylevD
 * Date: Dec 8, 2008
 * Time: 10:48:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class JavaCompilerHelper {
    public static final JavaCompiler              JAVA_COMPILER_INSTANCE;
    public static final StandardJavaFileManager   JAVA_FILEMGR_INSTANCE;

    static {
        JAVA_COMPILER_INSTANCE = ToolProvider.getSystemJavaCompiler();
//        if (JAVA_COMPILER_INSTANCE == null)
//            JAVA_COMPILER_INSTANCE = getCompiler4IKVM(loader);
        JAVA_FILEMGR_INSTANCE = JAVA_COMPILER_INSTANCE.getStandardFileManager(null, null, null);
    }

    private SpecialJavaFileManager  fileManager;
    private SpecialClassLoader      cl;

    public JavaCompilerHelper (ClassLoader loader) {        
        cl = new SpecialClassLoader (loader);
        fileManager = new SpecialJavaFileManager (JAVA_FILEMGR_INSTANCE, cl);
    }

    public Class<?> compileClass (String className, String code) throws ClassNotFoundException {
        List<MemorySource> compilationUnits = Arrays.asList(new MemorySource(className, code));
        Writer out = new PrintWriter(System.err);
        DiagnosticCollector<JavaFileObject> dianosticListener = new DiagnosticCollector<JavaFileObject>();
        //Iterable<String> options = Arrays.asList("-verbose");
        JavaCompiler.CompilationTask compile = JAVA_COMPILER_INSTANCE.getTask(out, fileManager, dianosticListener, null, null, compilationUnits);
        boolean ok = compile.call();

        final boolean hasDiagnostic = dianosticListener.getDiagnostics().size() > 0;
        StringBuilder sb = null;
        if (hasDiagnostic) {
            sb = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> s : dianosticListener.getDiagnostics()) {
                sb.append(s).append(Util.NATIVE_LINE_BREAK);
            }
            if (ok)
                Util.LOGGER.warning(sb.toString());
        }

        if (ok)
            return cl.findClass(className);
        else
            throw new RuntimeException("compilation failed:\n" + (sb != null ? sb.toString() : ""));
    }

    private static class MemorySource extends SimpleJavaFileObject {
        private String src;

        public MemorySource(String name, String src) {
            super(URI.create("string:///" + name + ".java"), Kind.SOURCE);
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


    private static class SpecialJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private SpecialClassLoader xcl;

        public SpecialJavaFileManager(StandardJavaFileManager sjfm, SpecialClassLoader xcl) {
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
    }


    private static class MemoryByteCode extends SimpleJavaFileObject {
        private ByteArrayOutputStream baos;

        public MemoryByteCode(String name) {
            super(URI.create("byte:///" + name + ".class"), Kind.CLASS);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            throw new IllegalStateException();
        }

        @Override
        public OutputStream openOutputStream() {
            baos = new ByteArrayOutputStream();
            return baos;
        }

        @Override
        public InputStream openInputStream() {
            throw new IllegalStateException();
        }

        public byte[] getBytes() {
            return baos.toByteArray();
        }
    }

    private static class SpecialClassLoader extends ClassLoader {
        private Map<String, MemoryByteCode> m = new HashMap<String, MemoryByteCode>();

        private SpecialClassLoader(ClassLoader parent) {
            super(parent);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            MemoryByteCode mbc = m.get(name);
            if (mbc == null) {
                mbc = m.get(name.replace(".", "/"));
                if (mbc == null) {
                    return super.findClass(name);
                }
            }
            return defineClass(name, mbc.getBytes(), 0, mbc.getBytes().length);
        }

        public void addClass(String name, MemoryByteCode mbc) {
            m.put(name, mbc);
        }
    }

    private static final String defaultJavaCompilerName
            = "com.sun.tools.javac.api.JavacTool";

/*
    private static JavaCompiler getCompiler4IKVM() {
        try {
            String jre_home = System.getProperty("java.home");
            if (jre_home == null || jre_home.endsWith("virtual-ikvm-home"))
                jre_home = System.getenv("JAVA_HOME");
            if (jre_home == null)
                throw new RuntimeException("Neither the JAVA_HOME environment, nor the java.home system property is set");
            if (new File(jre_home, "jre").exists())
                jre_home += "/jre";
            System.setProperty("sun.boot.class.path", jre_home + "/lib/rt.jar");

            String class_path = System.getProperty("java.class.path");
            if (class_path == null || class_path.length() == 0)
                class_path = System.getenv("CLASSPATH");
            System.setProperty("java.class.path", class_path);

            Class<?> clazz = Class.forName(defaultJavaCompilerName, true, MY CLASS LOADER);
            return (JavaCompiler) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 *
 */
}
