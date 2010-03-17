package deltix.util.lang;

import java.io.*;

import deltix.util.io.IOUtil;
import deltix.util.io.RegexFilenameFilter;

import java.net.URL;
import java.util.Iterator;

/**
 *  UNTESTED
 */
public final class FileSystemClassLoader extends AbstractClassLoader implements ListClasses {
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
    public Iterator<Class<?>> list(final String packageName) {
        final String subfolder = packageName.replace('.', File.separatorChar);
        final StringBuilder sb = new StringBuilder();

        return new Iterator<Class<?>>() {
            private final File[] files = new File(mClassDir, subfolder).listFiles(CLASS_FILE_FILTER);
            private int idx = 0;

            @Override
            public boolean hasNext() {
                return files != null && idx < files.length;
            }

            @Override
            public Class<?> next() {
                final File file = files[idx++];
                sb.setLength(0);
                final String fileName = file.getName();
                sb.append(packageName).append('.').append(fileName, 0, fileName.length() - 6);
                try {
                    return loadClass(sb.toString());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
