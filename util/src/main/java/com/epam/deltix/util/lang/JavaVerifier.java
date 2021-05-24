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