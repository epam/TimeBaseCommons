package deltix.util.io;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 *
 */
public class ZipBuilder implements Closeable {
    private ZipOutputStream     mJar;
    private Set <String>        mAddedEntries = new HashSet <String> ();
    
    public ZipBuilder (ZipOutputStream zos) {
        mJar = zos;
    }
    
    public void             close () throws IOException {
        mJar.close ();
    }
    
    public void             addFile (File f, String name) 
        throws IOException, InterruptedException
    {
        if (!mAddedEntries.add (name))
            return;
        
        ZipEntry        e = new ZipEntry (name);
        
        e.setSize (f.length ());
        e.setTime (f.lastModified ());
        
        mJar.putNextEntry (e);
        
        FileInputStream     fis = new FileInputStream (f);
        StreamPump.pump (fis, mJar);
        fis.close ();
        
        mJar.closeEntry ();
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

