package deltix.util.lang;

import deltix.util.io.IOUtil;
import java.io.*;

import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *  
 */
public final class MemoryZipClassLoader extends AbstractClassLoader {
    private final Map <String, byte []>     mResources =
        new HashMap <String, byte []> ();
    
    public MemoryZipClassLoader (InputStream is, boolean searchParentFirst) 
        throws IOException, InterruptedException 
    {
        this (is, MemoryZipClassLoader.class.getClassLoader (), searchParentFirst);
    }
    
    public MemoryZipClassLoader (InputStream is, ClassLoader parent, boolean searchParentFirst) 
        throws IOException, InterruptedException 
    {
        super (parent, searchParentFirst);
        
        ZipInputStream      zis = new ZipInputStream (is);
        
        for (;;) {
            ZipEntry        zentry = zis.getNextEntry ();
            
            if (zentry == null)
                break;
            
            String          name = zentry.getName ();
            
            if (name.endsWith ("/"))
                continue;
            
            mResources.put (name, IOUtil.readBytes (zis));
        }
    }

    @Override
    protected byte []           findResourceAsByteArray (String name) {
        return (mResources.get (name));
    }

    @Override
    protected InputStream       findResourceAsStream (String name) 
        throws IOException 
    {
        byte []     bytes = mResources.get (name);
        
        return (bytes == null ? null : new ByteArrayInputStream (bytes));
    }        
}
