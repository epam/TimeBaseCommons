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
