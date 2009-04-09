package deltix.util.bcel;

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
        new HashMap <String, JavaClass> ();

    protected void                  processClass (
        JavaClass                       jc,
        String                          fileName
    )
        throws IOException
    {
        classes.put (jc.getClassName (), jc);
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

                        JavaClass   jc = new ClassParser (is, fileName).parse ();
                        
                        processClass (jc, fileName);
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
