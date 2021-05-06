package com.epam.deltix.util.lang;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * Verify that runtime included java compiler.
 */
public class JavaVerifier {

    private static final Log LOG = LogFactory.getLog(JavaVerifier.class);
    private static final String toolsJarClassLoader = "com.sun.tools.javac.api.JavacTool";

    private static boolean verified = false;

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

    public static synchronized void verify() {
        if (!verified) {
            LOG.debug().append("Checking, if application is running under JDK.").commit();
            JavaCompiler compiler = getJavaCompilerInstance();
            if (compiler != null) {
                verified = true;
            } else {
                throw new RuntimeException("Application is running not under JDK, cause Java Compiler couldn't be loaded.");
            }
        }
    }

}
