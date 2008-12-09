package deltix.qsrv.hf.metadata;

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
class CompilerUtil {

    static Class<?> compileClass(String className, String code) throws ClassNotFoundException {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager sjfm = javac.getStandardFileManager(null, null, null);
        SpecialClassLoader cl = new SpecialClassLoader();
        SpecialJavaFileManager fileManager = new SpecialJavaFileManager(sjfm, cl);

        List<MemorySource> compilationUnits = Arrays.asList(new MemorySource(className, code));
        DiagnosticListener<JavaFileObject> dianosticListener = null;
        Writer out = new PrintWriter(System.err);
        JavaCompiler.CompilationTask compile = javac.getTask(out, fileManager, dianosticListener, null, null, compilationUnits);
        boolean res = compile.call();
        if (res)
            return cl.findClass(className);
        else
            throw new RuntimeException("compilation failed");
    }

    private static class MemorySource extends SimpleJavaFileObject {
        private String src;

        public MemorySource(String name, String src) {
            super(URI.create("string:///" + name + ".java"), Kind.SOURCE);
            this.src = src;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return src;
        }

        public OutputStream openOutputStream() {
            throw new IllegalStateException();
        }

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

        public JavaFileObject getJavaFileForOutput(Location location, String name, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            MemoryByteCode mbc = new MemoryByteCode(name);
            xcl.addClass(name, mbc);
            return mbc;
        }

        public ClassLoader getClassLoader(Location location) {
            return xcl;
        }
    }


    private static class MemoryByteCode extends SimpleJavaFileObject {
        private ByteArrayOutputStream baos;

        public MemoryByteCode(String name) {
            super(URI.create("byte:///" + name + ".class"), Kind.CLASS);
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            throw new IllegalStateException();
        }

        public OutputStream openOutputStream() {
            baos = new ByteArrayOutputStream();
            return baos;
        }

        public InputStream openInputStream() {
            throw new IllegalStateException();
        }

        public byte[] getBytes() {
            return baos.toByteArray();
        }
    }

    private static class SpecialClassLoader extends ClassLoader {
        private Map<String, MemoryByteCode> m = new HashMap<String, MemoryByteCode>();

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
}
