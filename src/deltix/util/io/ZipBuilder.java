package deltix.util.io;

import deltix.util.Util;
import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 *
 */
public class ZipBuilder implements Closeable {
    private ZipOutputStream     mZipOut;
    private Set <String>        mEntryNames = new HashSet <String> ();
    
    public ZipBuilder (File f) throws IOException {
        FileOutputStream        fos = new FileOutputStream (f);
        
        mZipOut = new ZipOutputStream (new BufferedOutputStream (fos));
    }
    
    public ZipBuilder (ZipOutputStream zos) {
        mZipOut = zos;
    }
    
    public boolean          containsEntry (String name) {
        return (mEntryNames.contains (name));
    }
    
    public void             close () throws IOException {
        mZipOut.close ();
    }
    
    private void            addEntry (
        String                  name,
        long                    size,
        long                    time,
        InputStream             is
    )
        throws IOException, InterruptedException
    {
        ZipEntry                e = new ZipEntry (name);
        
        e.setSize (size);
        e.setTime (time);
        
        mZipOut.putNextEntry (e);
        StreamPump.pump (is, mZipOut);        
        mZipOut.closeEntry ();
    }
    
    public void             addEntry (ZipFile f, String name) 
        throws IOException, InterruptedException
    {
        if (!mEntryNames.add (name))
            return;

        ZipEntry        from = f.getEntry (name);        
        InputStream     is = f.getInputStream (from);
        
        try {
            addEntry (name, from.getSize (), from.getTime (), is);
        } finally {
            Util.close (is);
        }
    }
    
    public void             addFile (File f, String name) 
        throws IOException, InterruptedException
    {
        if (!mEntryNames.add (name))
            return;
        
        FileInputStream     fis = new FileInputStream (f);

        try {
            addEntry (name, f.length (), f.lastModified (), fis);
        } finally {
            Util.close (fis);
        }
    }
    
    public void             addDir (File dir, String name) 
        throws IOException, InterruptedException
    {
        File []         children = dir.listFiles ();
        
        for (File f : children) {
            String      childName = name + "/" + f.getName ();
            
            if (f.isDirectory ())
                addDir (f, childName);
            else
                addFile (f, childName);
        }
    }    
}

