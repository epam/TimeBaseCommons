package deltix.util.lang;

import java.io.*;

import deltix.util.io.IOUtil;
import deltix.util.io.RegexFilenameFilter;

import java.net.URL;
import java.util.*;

/**
 *  UNTESTED
 */
public final class FileSystemClassLoader 
    extends AbstractClassLoader 
    implements ClassDirectory 
{
    private final File mClassDir;
    
    public FileSystemClassLoader (File rootDir, boolean searchParentFirst) {
        this (rootDir, FileSystemClassLoader.class.getClassLoader (), searchParentFirst);
    }

    public FileSystemClassLoader (File rootDir, ClassLoader parent, boolean searchParentFirst) {
        super (parent, searchParentFirst);
        mClassDir = rootDir;        
        map (mClassDir, null);
    }

    private void            map (File dir, String packPath) {
        File []     files = dir.listFiles ();
        
        if (files == null)
            return;
        
        for (File f : files) {
            String          name = f.getName ();
            
            if (f.isDirectory ())
                map (f, packPath == null ? name : packPath + '.' + name);
            else if (name.equals ("package-info.class"))
                definePackage (packPath, null, null, null, null, null, null, null);
        }
    }
    
    @Override
    public URL              findResource (String name) {
        File        f = new File (mClassDir, name);
        return (f.exists () ? IOUtil.createFileUrl (f) : null);
    }
    
    @Override
    protected InputStream   findResourceAsStream (String name) {
        try {
            return (new FileInputStream (new File (mClassDir, name)));
        } catch (FileNotFoundException x) {
            return (null);
        }
    }

    private final static FilenameFilter CLASS_FILE_FILTER = new RegexFilenameFilter(".*\\.class");

    @Override
    public Collection <Class <?>> listClassesForPackage(final String packageName) {
        final String                subfolder = packageName.replace('.', File.separatorChar);
        final StringBuilder         sb = new StringBuilder();
        final ArrayList <Class <?>> ret = new ArrayList <Class <?>> ();        
        final File                  folder = new File (mClassDir, subfolder);
        
        for (File classf : folder.listFiles (CLASS_FILE_FILTER)) {
            final String            fileName = classf.getName();
            
            sb.setLength(0);
            sb.append(packageName);
            sb.append ('.');
            sb.append (fileName, 0, fileName.length() - 6);
            
            try {
                ret.add (loadClass (sb.toString ()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        
        return (ret);
    }                  
}
