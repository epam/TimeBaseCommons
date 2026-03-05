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
package com.epam.deltix.util.bcel;

import java.io.*;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.apache.bcel.classfile.*;

/**
 *
 */
public class ByteCodeProcessor {
    protected Map <String, JavaClass>   classes =
        new TreeMap <String, JavaClass> ();

    protected void                  processClass (
        JavaClass                       jc
    )
        throws IOException
    {
        classes.put (jc.getClassName (), jc);
    }

    public void                     processClass (InputStream is, String fileName)
        throws IOException
    {
        processClass (new ClassParser (is, fileName).parse ());
    }

    public void                     processClass (File f)
        throws IOException
    {
        InputStream     is = new FileInputStream (f);

        try {
            processClass (is, f.getPath ());
        } finally {
            is.close();
        }
    }

    public void                     processJar (File f)
        throws IOException
    {
        JarFile                     jf = new JarFile (f);

        try {
            Enumeration             entries = jf.entries ();

            while (entries.hasMoreElements ()) {
                ZipEntry            e = (ZipEntry) entries.nextElement ();
                String              fileName = e.getName ();

                if (fileName.endsWith (".class")) {
                    InputStream     is = null;

                    try {
                        is = jf.getInputStream (e);

                        processClass (is, fileName);
                    } finally {
                        is.close();
                    }
                }
            }
        } finally {
            jf.close ();
        }
    }
}