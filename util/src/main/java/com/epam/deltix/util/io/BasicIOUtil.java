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
package com.epam.deltix.util.io;

import com.epam.deltix.util.lang.*;
import com.epam.deltix.util.lang.SortedProperties;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

import com.epam.deltix.util.lang.Util;

import com.epam.deltix.util.text.ShellPatternCSMatcher;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.*;

/**
 *  I/O Utilities which have no dependencies on Deltix CG code.
 */
public abstract class BasicIOUtil {
    public static final String  CR = System.getProperty ("line.separator");
    public static final String  TEMP_FILE_PREFIX = "~";

    /**
     *  Closes a Socket without throwing an exception. Checks for null.
     */
    public static void			close (Socket s) {
        if (s != null)
            try {
                s.close ();
            } catch (Exception x) {
                Util.handleException (x);
            }
    }

    /**
     *  Closes a ServerSocket without throwing an exception. Checks for null.
     */
    public static void			close (ServerSocket s) {
        if (s != null)
            try {
                s.close ();
            } catch (Exception x) {
                Util.handleException (x);
            }
    }

    public static void          close (ZipFile f) {
        if (f != null)
            try {
                f.close ();
            } catch (Exception x) {
                Util.handleException (x);
            }
    }

    public static URL       createFileUrl (File f) {
        String          path = f.getAbsolutePath ().replace ('\\', '/');

        try {
            if (path.length () > 1 && path.charAt (1) == ':')   // drive letter
                return (new URL ("file:///" + path));
            else
                return (new URL ("file://" + path));
        } catch (IOException iox) {
            throw new com.epam.deltix.util.io.UncheckedIOException(iox);
        }
    }

    public static void      force (FileChannel fc, boolean metaData)
        throws IOException, InterruptedException
    {
        fc.force (metaData);
        //  For some reason we can be here without any exceptions
        //  but with a suddenly closed file.
        if (Thread.interrupted ())
            throw new InterruptedException ();
        else if (!fc.isOpen ())
            throw new IOException ("FileChannel is closed.");
    }

    public static void      marshall (Marshaller m, File file, Object object)
            throws IOException, JAXBException
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            m.marshal(object, out);
            out.getChannel().force(false);
        } finally {
            Util.close(out);
        }
    }

    public static void      marshall (Marshaller m, OutputStream out, Object object)
            throws IOException, JAXBException
    {
        try {
            m.marshal(object, out);
            out.flush();
        } finally {
            Util.close(out);
        }
    }

    public static Object            unmarshal(Unmarshaller u, File file)
        throws FileNotFoundException, JAXBException
    {
        return unmarshal(u, new FileInputStream(file));
    }


    public static Object            unmarshal(Unmarshaller u, String resource)
            throws FileNotFoundException, JAXBException
    {
        return unmarshal(u, openResourceAsStream(resource));
    }

    public static Object            unmarshal(Unmarshaller u, InputStream in)
            throws FileNotFoundException, JAXBException
    {
        try {
            return u.unmarshal(in);
        } finally {
            Util.close(in);
        }
    }

    public static void      rename (File from, File to) throws IOException {
        if (!from.renameTo (to))
            throw new IOException ("Failed to rename " + from + " -> " + to);
    }

    public static void      delete (File f) throws IOException {
        if (! deleteFileOrDir(f))
            throw new IOException ("Failed to delete " + f);
    }

    public static void      deleteUnchecked (File f) {
        if (! deleteFileOrDir(f))
            throw new com.epam.deltix.util.io.UncheckedIOException("Failed to delete " + f);
    }

    /**
     *  Deletes the specified file, and continues to attempt to delete its parent directories
     *  up the directory path, until the deletion fails, or the limit file is reached.
     *  The deletion of a directory
     *  fails if the directory is not empty (as well as possibly for a number of other reasons).
     *
     *  @param f        The file that will be deleted, and then its parent directories
     *                  will be deleted, if they are empty.
     *  @param limit    Unless null, the deletion process will stop at this file. The
     *                  limit is exclusive, i.e. this file will never be deleted.
     *  @return The first file that failed to be deleted.
     */
    public static File      deleteWithEmptyParentPath (File f, File limit) {
        while (!f.equals (limit) && f.delete ())
            f = f.getParentFile ();

        return (f);
    }

    /**
     * Recursively deletes given directory and all its content
     *
     * @return  <code>true</code> if and only if the directory is
     *          successfully deleted; <code>false</code> otherwise
     */
    public static boolean deleteFileOrDir(File file) {
        if(file == null)
            return false;
        if(!file.exists())
            return true;
        if(file.isDirectory()) {
            File items[] = file.listFiles();
            if(items != null) {
                for(File item : items)
                    if( ! deleteFileOrDir(item))
                        return false;
            }
        }
        return file.delete();
    }

    public static boolean clearDir(File file) {
        if (file == null)
            return false;

        if (!file.exists())
            return true;

        if (file.isDirectory()) {
            File items[] = file.listFiles();
            if(items != null) {
                for(File item : items)
                    if( ! deleteFileOrDir(item))
                        return false;
            }
        }

        return true;
    }

    public static void      createNew (File f) throws IOException {
        if (!f.createNewFile ())
            throw new IOException ("Failed to create " + f);
    }

    /**
     *  Output character <code>ch</code> <code>n</code> times to Writer
     *  <code>wr</code>.
     */
    public static void      pad (char ch, int n, Writer wr) throws IOException {
        for (int ii = 0; ii < n; ii++)
            wr.write (ch);
    }

    /**
     *  Output character <code>ch</code> <code>n</code> times to PrintStream
     *  <code>ps</code>.
     */
    public static void      pad (char ch, int n, PrintStream ps) throws IOException {
        for (int ii = 0; ii < n; ii++)
            ps.print (ch);
    }
    /**
     *  Return the short name of teh file without the extension.
     */
    public static String    getNameNoExt (File f) {
        String  name = f.getName ();
        int     dotIdx = name.lastIndexOf ('.');
        if (dotIdx < 0)
            return (name);
        else
            return (name.substring (0, dotIdx));
    }

    /**
     * Clones the Serializable object by first writing it to a byte stream
     * and then reading it back.
     *
     * @param object the Serializable object being cloned
     *
     * @return Object - the cloned object
     *
     * @throws CloneNotSupportedException - thrown when the afore-mentioned cloning algorithm fails
     */
    @SuppressFBWarnings("OBJECT_DESERIALIZATION")
    public static Object clone(Serializable object)
        throws CloneNotSupportedException
    {
        Object clone = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream (4096);
            ObjectOutputStream oos = new ObjectOutputStream (baos);
            oos.writeObject (object);
            oos.close ();

            ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
            ObjectInputStream ois = new ObjectInputStream (bais);
            clone = ois.readObject ();
            ois.close ();
        } catch (Exception x) {
            x.printStackTrace();
            throw new CloneNotSupportedException (x.getMessage ());
        }

        return clone;
    }

    /**
     * Reads entire stream into bufffer
     *
     * @param is the InputStream to read
     * @param readBlockSize - [IMPORTANT] - expected size of the stream or block size
     *
     * @return Input Stream as array of bytes
     * @throws IOException - InputStream.read() fails
     */
    public static byte[] getStreamBytes(InputStream is, final int readBlockSize)
        throws IOException
    {

        int bufSize = 0; // actual bytes read
        int bufCapacity = readBlockSize; // current buffer capacity
        byte buf[] = new byte[bufCapacity];

        try {
            while (true) {
                int bytesRead = is.read (buf, bufSize, readBlockSize);

                //System.err.println ("request.is.read() bytesRead=" + bytesRead);

                bufSize += (bytesRead > 0) ? bytesRead : 0;

                if (bytesRead <= 0) // no more data?
                    break;

                if (bufSize + readBlockSize > bufCapacity) {
                    // buffer is full but we have more bytes in stream - realloc
                    bufCapacity += 2 * readBlockSize;

                    byte[] newbuf = new byte[bufCapacity];
                    System.arraycopy (buf, 0, newbuf, 0, bufSize);
                    buf = newbuf;
                }
            }
        } finally {
            // Closing stream is responsibility of stream creator!
            // if (is != null) is.close();
        }

        byte[] result = new byte[bufSize];
        if (bufSize > 0)
            System.arraycopy (buf, 0, result, 0, bufSize);
        return result;
    }

    /** Copy src file into dest */
    public static void			copyFile (File src, File dest)
        throws IOException, InterruptedException
    {
        FileInputStream		in = null;
        
        try {
            in = new FileInputStream (src);
            copyToFile (in, dest, src.length ());
        } finally {
            Util.close (in);
        }
    }

    public static void copyFile1(File sourceFile, File destFile) throws IOException {
        
        if (destFile.exists())
            destFile.delete();

        destFile.createNewFile();

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            Util.close(source);
            Util.close(destination);
        }
    }

    public static void	copyFile7 (File src, File dest)
        throws IOException, InterruptedException {
        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }    

    /**
    *   Copies file and creates full destination path if needed
    */
    public static void copyFileWithPathCreate (File src, File dest)
        throws IOException, InterruptedException
    {
        File directory = dest.getParentFile();
        if (!directory.exists()){
            directory.mkdirs();
        }
        copyFile (src, dest);
    }

    public static void          mkParentDirIfNeeded (File f)
        throws FileNotFoundException
    {
        File     pd = f.getParentFile ();
        if (pd != null)
            mkDirIfNeeded (pd);
    }

    public static void          mkDirIfNeeded (File f)
        throws FileNotFoundException
    {
        if (!f.isDirectory () && !f.mkdirs ())
            throw new FileNotFoundException ("Cannot create " + f.getPath ());
    }

    public static void  		removeRecursive (
        File						file,
        FileFilter					filter,
        boolean						includeThisFile
    )
        throws IOException
    {
        if (file.isDirectory ()) {
            File []	fileElements = file.listFiles (filter);

            if (fileElements != null)
                for (int iElement = 0; iElement < fileElements.length; iElement++)
                    removeRecursive (fileElements [iElement], filter, true);
        }

        if (includeThisFile) {
            file.delete ();
            
            if (file.exists ())
                throw new IOException ("Failed to delete " + file);
        }
    }

     public static void  		removeRecursive (
        File						file,
        FileFilter					filter,
        List<File>                  remains,
        boolean                     include
    )
        throws IOException
    {
        if (file.isDirectory ()) {
            File []	fileElements = file.listFiles (filter);

            if (fileElements != null)
                for (int iElement = 0; iElement < fileElements.length; iElement++)
                    removeRecursive (fileElements [iElement], filter, remains, true);
        }

        if (include && !file.delete ())
            remains.add(file);
    }

    public static void  		removeRecursive (File file)
        throws IOException
    {
        removeRecursive (file, null, true);
    }

    public static void  		removeRecursiveBefore (File file, Date date,
                                                        boolean includeThisDir)
        throws IOException
    {
        long	lastMod = file.lastModified();

        if (file.isDirectory ()) {
            File []	fileElements = file.listFiles ();
            for (int iElement = 0; iElement < fileElements.length; iElement++)
                removeRecursiveBefore (fileElements [iElement], date);
        }

        if (includeThisDir && lastMod < date.getTime())
            file.delete ();
    }

    public static void  		removeRecursiveBefore (File file, Date date)
        throws IOException
    {
        long	lastMod = file.lastModified();

        if (file.isDirectory ()) {
            File []	fileElements = file.listFiles ();
            for (int iElement = 0; iElement < fileElements.length; iElement++)
                removeRecursiveBefore (fileElements [iElement], date);
        }

        if (lastMod < date.getTime())
            file.delete ();
    }

    public static void			readFile (File file, OutputStream out)
        throws IOException, InterruptedException
    {
        FileInputStream	fis = new FileInputStream (file);

        try {
            StreamPump.pump (fis, out);
        } finally {
            Util.close (fis);
        }
    }

    public static String		readTextFromClassPathNoX (String relPath) {
        try {
            return (readTextFromClassPath (relPath));
        } catch (InterruptedException | IOException x) {
            throw new com.epam.deltix.util.io.UncheckedIOException(x);
        }
    }
    
    public static String		readTextFromClassPath (String relPath)
        throws IOException, InterruptedException
    {
        InputStream		is = openResourceAsStream (relPath);

        try {
            return (readFromStream (is));
        } finally {
            Util.close (is);
        }
    }

    public static String []     readLinesFromClassPath (String relPath)
        throws IOException, InterruptedException
    {
        return readLinesFromClassPath (relPath, false);
    }
    
    public static String []     readLinesFromClassPath (String relPath, boolean trim)
        throws IOException, InterruptedException
    {
        InputStream		is = openResourceAsStream (relPath);

        try {
            return (readLinesFromReader (new InputStreamReader (is), trim));
        } finally {
            Util.close (is);
        }
    }

    public static String		readTextFile (String filepath)
        throws IOException, InterruptedException
    {
        FileReader      fr = null;

        try {
            fr = new FileReader (filepath);
            return (readFromReader (fr));
        } finally {
            Util.close (fr);
        }
    }

    public static String		readTextFile (File f)
        throws IOException, InterruptedException
    {
        FileReader      fr = null;

        try {
            fr = new FileReader (f);
            return (readFromReader (fr));
        } finally {
            Util.close (fr);
        }
    }

    public static String []     readLinesFromTextFile (String filepath)
        throws IOException, InterruptedException
    {
        FileReader      fr = null;

        try {
            fr = new FileReader (filepath);
            return (readLinesFromReader (fr));
        } finally {
            Util.close (fr);
        }
    }

    public static String []     readLinesFromTextFile (File f)
        throws IOException, InterruptedException
    {
        return (readLinesFromTextFile (f, false));
    }
    
    public static String []     readLinesFromTextFile (File f, boolean trim)
        throws IOException, InterruptedException
    {
        try (FileReader fr = new FileReader (f)) {            
            return (readLinesFromReader (fr, trim));
        }
    }

    /**
     */
    public static String		readFromStream (InputStream is, String encoding)
        throws IOException, InterruptedException
    {
        return (readFromReader (new InputStreamReader (is, encoding)));
    }

    /**
     */
    public static String		readFromStream (InputStream is)
        throws IOException, InterruptedException
    {
        return (readFromReader (new InputStreamReader (is)));
    }    
    
    public static String		readFromReader (Reader r)
        throws IOException, InterruptedException
    {
        if (!(r instanceof BufferedReader))
            r = new BufferedReader (r);

        StringBuffer	fileContents = new StringBuffer ();
        char []			tmpContent = new char [4096];

        for (;;) {
            int         numRead = r.read (tmpContent, 0, tmpContent.length);

            if (Thread.interrupted ())
                throw new InterruptedException ();

            if (numRead < 0)
                break;

            fileContents.append (tmpContent, 0, numRead);
        }

        return (fileContents.toString ());
    }

    public static String []     readLinesFromReader (Reader r)
        throws IOException, InterruptedException {
        return readLinesFromReader (r, false);
    }

    public static String []     readLinesFromReader (Reader r, boolean trim)
        throws IOException, InterruptedException
    {
        BufferedReader      brd;

        if (r instanceof BufferedReader)
            brd = (BufferedReader) r;
        else
            brd = new BufferedReader (r);

        ArrayList <String>  lines = new ArrayList <String> ();

        for (;;) {
            String          line = brd.readLine ();

            if (line == null)
                break;
            
            if (Thread.interrupted ())
                throw new InterruptedException ();

            if (trim) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
            }
            
            lines.add (line);
        }

        return (lines.toArray (new String [lines.size ()]));
    }
    
    public static <T extends Appendable> T readFromReaderWithProperties (Reader r, final Properties props, final T to)
        throws IOException, InterruptedException
    {                
        
        if (!(r instanceof BufferedReader))
            r = new BufferedReader (r);

        if (props != null) {
            r = new TokenReplacingReader(r, new TokenReplacingReader.TokenResolver() {

                @Override
                public String resolveToken(String token) {
                    return props.getProperty(token);
                }
            });
        }
        
        CharBuffer			tmpContent = CharBuffer.allocate(4096);

        for (;;) {
            int         numRead = r.read (tmpContent);

            if (Thread.interrupted ())
                throw new InterruptedException ();

            if (numRead < 0)
                break;

            to.append (tmpContent, 0, numRead);
        }
        
        return to;
    }
    
    public static void replaceProperties (Reader r, Appendable w, Properties props)
        throws IOException, InterruptedException
    {                
        readFromReaderWithProperties(r, props, w);
    }
    
    public static String getFileText (File file) throws IOException, InterruptedException {
        FileInputStream is = new FileInputStream(file);
        try {
            return BasicIOUtil.readFromStream (is);
        } finally {
            is.close();
        }
    }
    
    public static void		writeTextFile (String filepath, String content)
        throws IOException
    {
        writeTextFile (new File (filepath), content);
    }

    public static void		writeTextFile (File f, String content)
        throws IOException
    {
        FileWriter fw = new FileWriter (f);

        try {
            fw.write (content);
            fw.close ();
        } finally {
            Util.close (fw);
        }
    }

    public static void		writeLines (File f, Collection <String> lines)
        throws IOException
    {
        PrintWriter fw = new PrintWriter (f);

        try {
            for (String s : lines)
                fw.println (s);

            fw.close ();
        } finally {
            Util.close (fw);
        }
    }

    public static void		writeLines (File f, String[] lines)
            throws IOException
    {
        PrintWriter fw = new PrintWriter (f);

        try {
            for (String s : lines)
                fw.println (s);

            fw.close ();
        } finally {
            Util.close (fw);
        }
    }

    public static void		writeBytes (
        File                    file,
        byte []                 bytes,
        int                     offset,
        int                     length
    )
        throws IOException
    {
        FileOutputStream        fos = new FileOutputStream (file);

        try {
            fos.write (bytes, offset, length);
        } finally {
            Util.close (fos);
        }
    }

    public static byte []   readBytes (File file)
        throws IOException
    {
        int         flen = (int) file.length ();
        byte []     ret = new byte [flen];

        readBytes (file, ret, 0, flen);
        return (ret);
    }

    /** @return number of bytes remaining in buffer after read completion */
    public static int      readFully (
        InputStream             is, 
        byte []                 bytes, 
        int                     offset, 
        int                     length
    ) 
        throws IOException
    {
        while (length > 0) {
            int     count = is.read (bytes, offset, length);
            
            if (count < 0)
                throw new EOFException ();
            
            offset += count;
            length -= count;
        }
        return length;
    }
    
    public static void      skipFully (
        InputStream             is, 
        long                    length
    ) 
        throws IOException
    {
        while (length > 0) {
            long        count = is.skip (length);
            // Note: actually count can't be negative
            if (count <= 0)
                throw new EOFException ();
            
            length -= count;
        }
    }
    
    public static void		readBytes (
        File                    file,
        byte []                 bytes,
        int                     offset,
        int                     length
    )
        throws IOException
    {
        try (FileInputStream fis = new FileInputStream (file)) {
            readFully (fis, bytes, offset, length);
        } 
    }

    public static byte []   readBytes (InputStream is)
        throws IOException, InterruptedException
    {
        ByteArrayOutputStream    bais = new ByteArrayOutputStream ();
        StreamPump.pump (is, bais);
        return (bais.toByteArray ());
    }

    public static Properties	readPropsFromFile (File file)
        throws IOException
    {
        Properties		props = new SortedProperties ();
        FileInputStream	fis = new FileInputStream (file);

        try {
            props.load (fis);
        } finally {
            Util.close (fis);
        }

        return (props);
    }

    /**
     *  Opens a resource and wraps in a LineNumberReader, throws a FileNotFoundException
     *  if not found.
     */
    public static LineNumberReader   openResourceAsReader (String relPath)
        throws FileNotFoundException
    {
        return (new LineNumberReader (new InputStreamReader (openResourceAsStream (relPath))));
    }

    /**
     *  Opens a resource as stream, but throws a FileNotFoundException
     *  if not found.
     */
    public static InputStream   openResourceAsStream (String relPath)
        throws FileNotFoundException
    {
        InputStream		is =
            BasicIOUtil.class.getClassLoader ().getResourceAsStream (relPath);

        if (is == null)
            throw new FileNotFoundException ("CLASSPATH/" + relPath);

        return (is);
    }

    public static void          copyResource (String path, String encoding, Writer wr)
        throws IOException, InterruptedException
    {
        InputStream             is = openResourceAsStream (path);

        try {
            InputStream         bis = is;
            Writer              bwr = wr;
            
            if (!(bis instanceof BufferedInputStream))
                bis = new BufferedInputStream (bis);
            
            if (!(bwr instanceof BufferedWriter))
                bwr = new BufferedWriter (wr);
            
            InputStreamReader   rd = new InputStreamReader (bis, encoding);
            
            for (;;) {
                int             ch = rd.read ();
                
                if (ch < 0)
                    break;
                
                bwr.write (ch);
            }
            
            is.close ();
        } finally {
            Util.close (is);
        }
    }
    
    public static void          copyResource (String path, OutputStream os)
        throws IOException, InterruptedException
    {
        InputStream     is = openResourceAsStream (path);

        try {
            StreamPump.pump (is, os);
            is.close ();
        } finally {
            Util.close (is);
        }
    }

    public static void          extractResource (String path, File dest)
        throws IOException, InterruptedException
    {
        OutputStream    os = new FileOutputStream (dest);

        try {
            copyResource (path, os);
        } finally {
            Util.close (os);
        }
    }

    public static Properties	readPropsFromClassPath (String relPath)
        throws IOException
    {
        Properties		props = new SortedProperties ();
        InputStream		is = openResourceAsStream (relPath);

        try {
            props.load (is);
        } finally {
            Util.close (is);
        }

        return (props);
    }

    public static void		storePropsToFile (
        Properties				props,
        File					file,
        String					header
    )
        throws IOException
    {
        FileOutputStream	fos = new FileOutputStream (file);

        try {
            props.store (fos, header);
        } finally {
            Util.close (fos);
        }
    }


    public static void  copyToFile (InputStream is, File destFile)
        throws IOException, InterruptedException
    {
        copyToFile (is, destFile, 0);
    }

    public static void  copyToFile (InputStream is, File destFile, long size)
        throws IOException, InterruptedException
    {
        byte []             buffer = new byte [4096];
        
        copyToFile (is, destFile, size, buffer);
    }
    
    public static void  copyToFile (InputStream is, File destFile, long size, byte [] buffer)
        throws IOException, InterruptedException
    {        
        RandomAccessFile out;
        try {
            out = new RandomAccessFile (destFile,
                                        "rw");
        } catch (IOException ioe) {
            throw new AccessibleIOException (ioe,
                                             destFile);
        }

        try {
            if (size < 0)
                size = 0;

            out.setLength (size);   // prevent fragmentation

            for (;;) {
                if (Thread.interrupted ())
                    throw new InterruptedException ();

                int     n = is.read (buffer);

                if (n < 0)
                    break;

                out.write (buffer, 0, n);
            }

            out.close ();
        } finally {
            Util.close (out);
        }
    }
    
    /**
     *	Checks if the file is present.
     *	@exception FileNotFoundException	If the file does not exists.
     */
    public static void	assertExists (File f) throws FileNotFoundException {
        if (!f.exists ())
            throw (new FileNotFoundException (f.getPath ()));
    }

    private static final String	SPECIAL_ZIP_ENTRY_NAME = "SERIALIZED-OBJECT";

    public static boolean		looksLikeZip (File f) {
        return (f.getName ().toLowerCase ().endsWith (".zip"));
    }

    public static OutputStream	openMaybeZip (OutputStream fos, boolean compress)
        throws IOException
    {
        if (compress) {
            ZipOutputStream		zipos = new ZipOutputStream (fos);
            zipos.putNextEntry (new ZipEntry (SPECIAL_ZIP_ENTRY_NAME));
            return (zipos);
        }
        else
            return (fos);
    }

    public static InputStream	openMaybeZip (InputStream fis, boolean compress)
        throws IOException
    {
        if (compress) {
            ZipInputStream		zipis = new ZipInputStream (fis);
            zipis.getNextEntry ();
            return (zipis);
        }
        else
            return (fis);
    }

    public static InputStream	openInputMaybeZip (File f, boolean compress)
        throws IOException
    {
        return (
            openMaybeZip (
                new BufferedInputStream (new FileInputStream (f)),
                compress
            )
        );
    }

    public static InputStream	openInputMaybeZip (File f)
        throws IOException
    {
        return (openInputMaybeZip (f, looksLikeZip (f)));
    }

    public static OutputStream	openOutputMaybeZip (File f, boolean compress)
        throws IOException
    {
        return (
            openMaybeZip (
                new BufferedOutputStream (new FileOutputStream (f)),
                compress
            )
        );
    }

    public static OutputStream	openOutputMaybeZip (File f)
        throws IOException
    {
        return (openOutputMaybeZip (f, looksLikeZip (f)));
    }

    public static void			closeMaybeZip (InputStream is) throws IOException {
        is.close ();
    }

    public static void			closeMaybeZip (OutputStream os) throws IOException {
        if (os instanceof ZipOutputStream) {
            ZipOutputStream		zipos = (ZipOutputStream) os;
            zipos.closeEntry ();
            zipos.finish ();
        }
        else
            os.close ();
    }

    /**
     *	Write a serializable object to file.
     *
     *	@param f			File to write to.
     *	@param compress		Whether to zip up the object.
     */
    public static void			writeSerializable (
        File						f,
        Serializable				obj,
        boolean						compress
    )
        throws IOException
    {
        FileOutputStream	fos = new FileOutputStream (f);

        try {
            OutputStream	os = openMaybeZip (fos, compress);

            {
                ObjectOutputStream	oos = new ObjectOutputStream (os);
                oos.writeObject (obj);
                oos.flush ();
            }

            closeMaybeZip (os);
        } finally {
            Util.close (fos);
        }
    }

    /**
     *	Write a serializable object to file. If the file has the ".zip" extension,
     *	the object is compressed on write, and a zip file is created.
     *
     *	@param f			File to write to.
     */
    public static void			writeSerializable (File f, Serializable obj)
        throws IOException
    {
        writeSerializable (f, obj, looksLikeZip (f));
    }

    /**
     *  Copies files from the specified directory to the specified directory.
     *  @param from -- directory to copy files from
     *  @param to   -- directory to copy files to
     *  @param create -- if true and to directory does not exist create it.
     *  @param recursive -- if true also copies child directories
     *  @param filter -- if not null only copies files that satisfy the specified filter
     *  @param excludeFilter -- if not null does not copy files that satisfy the filter
     */
    public static void copyDirectory (
        File from,
        File to,
        boolean create,
        boolean recursive,
        FilenameFilter  filter,
        FilenameFilter excludeFilter
    )
        throws IOException, InterruptedException
    {
        if (create){
            if (!to.exists()){
                to.mkdirs();
            }
        }
        File [] fileList = from.listFiles(filter);

        if (fileList == null)
            throw new FileNotFoundException (from.getPath ());

        for (int i = 0; i < fileList.length; i++){
            File f = fileList [i];
            if ((excludeFilter == null)||(!excludeFilter.accept(to, f.getName()))){
                File copy = new File (to, f.getName());
                if (f.isDirectory()){
                    copy.mkdir();
                    if (recursive){
                        copyDirectory (f, copy, false, recursive, filter, excludeFilter);
                    }
                }
                else{
                    copyFile(f, copy);
                }
            }
        }
    }
    
    /**
     *  Native Java7-way, copies files from the specified directory to the specified directory.
     *  @param from -- directory to copy files from
     *  @param to   -- directory to copy files to
     *  @param create -- if true and to directory does not exist create it.
     *  @param recursive -- if true also copies child directories
     *  @param filter -- if not null only copies files that satisfy the specified filter
     *  @param excludeFilter -- if not null does not copy files that satisfy the filter
     */
    public static void copyDirectory7 (
        final File from,
        final File to,
        final boolean create,
        final boolean recursive,
        final FilenameFilter filter,
        final FilenameFilter excludeFilter
    )
        throws IOException, InterruptedException
    {
        
        final SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            private Path fromPath = Paths.get(from.toURI());
            private Path toPath = Paths.get(to.toURI());
            private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;
    
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetPath = toPath.resolve(fromPath.relativize(dir));
                if(!Files.exists(targetPath)){
                    if (create) {
                        Files.createDirectory(targetPath);                        
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                final String fileName = file.toFile().getName();
                if (
                        ((filter == null) || (filter.accept(from, fileName)))
                        &&
                        ((excludeFilter == null) || (!excludeFilter.accept(to, fileName)))
                   ) {
                    Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);                                        
                }
                return FileVisitResult.CONTINUE;
            }        
        };
        
        Files.walkFileTree(Paths.get(from.toURI()), EnumSet.of(FileVisitOption.FOLLOW_LINKS), recursive ? Integer.MAX_VALUE : 1, visitor);
    }    

    private static String	getCmdAsString (String [] arr) {
        StringBuffer	sb = new StringBuffer ();

        for (int ii = 0; ii < arr.length; ii++) {
            if (ii > 0)
                sb.append (' ');
            sb.append ('\'');
            sb.append (arr [ii]);
            sb.append ('\'');
        }

        return (sb.toString ());
    }

    public static long getFreeDiskSpaceOn(File filesys)
        throws IOException, InterruptedException {
        if (!filesys.exists())
            throw new FileNotFoundException(filesys.getPath());

        String fsep = System.getProperty("file.separator");
        String[] cmd = null;
        boolean dos = true;

        if (fsep.equals("\\"))
            cmd = new String[] { "cmd.exe", "/c", "dir", filesys.getPath()};
        else {
            cmd = new String[] { "/usr/bin/df", "-k", filesys.getPath()};
            dos = false;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            int ret =
                ProcessHelper.execAndWait(
                    cmd,
                    null,
                    null,
                    false,
                    baos,
                    true,
                    null,
                    false);
        } catch (IOException e) {
            cmd = new String[] { "df", "-k", filesys.getPath()};
                int ret =
                    ProcessHelper.execAndWait(
                        cmd,
                        null,
                        null,
                        false,
                        baos,
                        true,
                        null,
                        false);

        if (ret != 0)
            throw new IOException(
                "Command\n" + getCmdAsString(cmd) + "\nexited with " + ret);
        }
        String s = new String(baos.toByteArray());

        if (System.getProperty("debug.diskspace") != null)
            System.out.println(
                "Output from\n" + getCmdAsString(cmd) + ":\n" + s);

        int pos = -1;
        long value = 0;

        if (dos) {
            pos = s.lastIndexOf(" bytes free");

            if (pos < 0)
                throw new IOException(
                    "Unrecognized output from\n"
                        + getCmdAsString(cmd)
                        + ":\n"
                        + s);
            long exp = 1;

            for (;;) {
                pos--;

                char ch = s.charAt(pos);

                if (ch == ' ')
                    break;

                if (ch == ',')
                    continue;

                value += exp * (ch - '0');
                exp *= 10;
            }

        } else {
            StringTokenizer st = new StringTokenizer(s);
            for (int i = 1; i < 11; i++) {
                st.nextToken();
                if (i == 10)
                    value = Long.parseLong(st.nextToken());
                value *= 1024; // df -k gives us in kilobytes
            }

        }

        return (value);
    }

    public static final class FileOnlyFilter implements FileFilter {

        public boolean accept(File pathname)
        {
            return pathname.isFile ();
        }
    }

    /**
     *  Writes any CharSequence to DataOutput as a 2-byte length (in characters), followed by
     *  that many characters in raw 2-byte form.
     */
    public static int      writeUnicode (CharSequence str, DataOutput out) throws IOException {
        int     strlen = str.length ();

        if (strlen > 65535)
            throw new UTFDataFormatException ("string too long: " + strlen + " bytes");

        out.writeShort ((short) strlen);

        for (int ii = 0; ii < strlen; ii++)
            out.writeChar (str.charAt (ii));

        return (strlen * 2 + 2);
    }
    
    /**
     *  Writes any CharSequence to DataOutput in a way identical to
     *  DataOutputStream.writeUTF, which is groundlessly defined too narrowly
     *  by forcing the argument to be a String.
     */
    public static int      writeUTF (CharSequence str, DataOutput out) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;

            /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++) {
                c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
            utflen++;
            } else if (c > 0x07FF) {
            utflen += 3;
            } else {
            utflen += 2;
            }
        }

        if (utflen > 65535)
            throw new UTFDataFormatException(
                    "encoded string too long: " + utflen + " bytes");

        if (out instanceof LittleEndianDataOutputStream) {
            out.writeByte((utflen >>> 0) & 0xFF);
            out.writeByte((utflen >>> 8) & 0xFF);

        } else {
            out.writeByte((utflen >>> 8) & 0xFF);
            out.writeByte((utflen >>> 0) & 0xFF);
        }


        int i=0;
        for (i=0; i<strlen; i++) {
           c = str.charAt(i);
           if (!((c >= 0x0001) && (c <= 0x007F))) break;
           out.writeByte (c);
        }

        for (;i < strlen; i++){
            c = str.charAt(i);

            if ((c >= 0x0001) && (c <= 0x007F))
                out.writeByte (c);
            else if (c > 0x07FF) {
                out.writeByte (0xE0 | ((c >> 12) & 0x0F));
                out.writeByte (0x80 | ((c >>  6) & 0x3F));
                out.writeByte (0x80 | ((c >>  0) & 0x3F));
            }
            else {
                out.writeByte (0xC0 | ((c >>  6) & 0x1F));
                out.writeByte (0x80 | ((c >>  0) & 0x3F));
            }
        }

        return utflen + 2;
    }
    
    /**
     *  Reads (appends) a UTF string to an Appendable (such as StringBuidler),
     *  without clearing it first.
     */
    @Deprecated // buggy
    public final static void readUTF(DataInput in, Appendable sb) throws IOException {
        int utflen = in.readUnsignedShort();

        if (utflen == 0)
            return;

        int c;
        int char2, char3;
        int count = 0;

        for (;;) {
            c = in.readByte (); //NB: result in range [-128, 127] -- Andy
            //if (c > 127)
            //    break;

            count++;
            sb.append ((char) c);

            if (count >= utflen)
                return;
        }
// de-facto unreachable -- Andy
//        //  If we are here, we have broken out of the previous loop and there is an
//        //  unhandled escape character in variable c.
//        for (;;) {
//            switch (c >> 4) {
//                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
//                    /* 0xxxxxxx*/
//                    count++;
//                    sb.append ((char)c);
//                    break;
//
//                case 12: case 13:
//                    /* 110x xxxx   10xx xxxx*/
//                    count += 2;
//                    if (count > utflen)
//                        throw new UTFDataFormatException(
//                            "malformed input: partial character at end");
//                    char2 = in.readByte ();
//                    if ((char2 & 0xC0) != 0x80)
//                        throw new UTFDataFormatException(
//                            "malformed input around byte " + count);
//                    sb.append ((char)(((c & 0x1F) << 6) |
//                                                    (char2 & 0x3F)));
//                    break;
//
//                case 14:
//                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
//                    count += 3;
//                    if (count > utflen)
//                        throw new UTFDataFormatException(
//                            "malformed input: partial character at end");
//                    char2 = in.readByte ();
//                    char3 = in.readByte ();
//                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
//                        throw new UTFDataFormatException(
//                            "malformed input around byte " + (count-1));
//                    sb.append ((char)(((c & 0x0F) << 12) |
//                                                    ((char2 & 0x3F) << 6)  |
//                                                    ((char3 & 0x3F) << 0)));
//                    break;
//
//                default:
//                    /* 10xx xxxx,  1111 xxxx */
//                    throw new UTFDataFormatException(
//                        "malformed input around byte " + count);
//            }
//
//            if (count >= utflen)
//                break;
//
//            c = in.readByte ();
//        }
    }

    /**
     *  Reads (appends) a UTF string to an Appendable (such as StringBuidler),
     *  without clearing it first.
     */
    @Deprecated // buggy
    public final static void readUTF(ByteBuffer in, Appendable sb) throws BufferUnderflowException, IOException {
        int utflen = 0xFFFF & in.getShort();

        if (utflen == 0)
            return;

        int c;
        int char2, char3;
        int count = 0;

        for (;;) {
            c = in.get (); //NB: result in range [-128, 127] -- Andy
//            if (c > 127)
//                break;

            count++;
            sb.append ((char) c);

            if (count >= utflen)
                return;
        }
// de-facto unreachable -- Andy
//        //  If we are here, we have broken out of the previous loop and there is an
//        //  unhandled escape character in variable c.
//        for (;;) {
//            switch (c >> 4) {
//                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
//                    /* 0xxxxxxx*/
//                    count++;
//                    sb.append ((char)c);
//                    break;
//
//                case 12: case 13:
//                    /* 110x xxxx   10xx xxxx*/
//                    count += 2;
//                    if (count > utflen)
//                        throw new UTFDataFormatException(
//                            "malformed input: partial character at end");
//                    char2 = in.get ();
//                    if ((char2 & 0xC0) != 0x80)
//                        throw new UTFDataFormatException(
//                            "malformed input around byte " + count);
//                    sb.append ((char)(((c & 0x1F) << 6) |
//                                                    (char2 & 0x3F)));
//                    break;
//
//                case 14:
//                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
//                    count += 3;
//                    if (count > utflen)
//                        throw new UTFDataFormatException(
//                            "malformed input: partial character at end");
//                    char2 = in.get ();
//                    char3 = in.get ();
//                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
//                        throw new UTFDataFormatException(
//                            "malformed input around byte " + (count-1));
//                    sb.append ((char)(((c & 0x0F) << 12) |
//                                                    ((char2 & 0x3F) << 6)  |
//                                                    ((char3 & 0x3F) << 0)));
//                    break;
//
//                default:
//                    /* 10xx xxxx,  1111 xxxx */
//                    throw new UTFDataFormatException(
//                        "malformed input around byte " + count);
//            }
//
//            if (count >= utflen)
//                break;
//
//            c = in.get ();
//        }
    }

    public static long      addFileToZip (File f, ZipOutputStream zos, String path)
        throws IOException, InterruptedException
    {
        return (addFileToZip (f, zos, path, null));
    }

    public static interface EntryListener {
        public void     entryAdded (ZipEntry e)
            throws IOException, InterruptedException;
    }

    public static long      addFileToZip (
        File                    f,
        ZipOutputStream         zos,
        String                  path,
        EntryListener           listener
    )
        throws IOException, InterruptedException
    {
        if (f.isDirectory ()) {
            File []         files = f.listFiles ();
            long            length = 0;

            if (files != null) {
                for (File ff : files)
                    length +=
                        addFileToZip (
                            ff,
                            zos,
                            path == null ? ff.getName () : path + '/' + ff.getName (),
                            listener
                        );
            }

            return (length);
        }
        else {
            ZipEntry        e = new ZipEntry (path);
            long            length = f.length ();

            e.setTime (f.lastModified ());
            e.setSize (length);

            zos.putNextEntry (e);

            FileInputStream     fis = new FileInputStream (f);

            try {
                StreamPump.pump (fis, zos);
            } finally {
                fis.close ();
            }

            zos.closeEntry ();

            if (listener != null)
                listener.entryAdded (e);

            return (length);
        }
    }

    public static void      rezip (
        ZipFile                 src,
        ZipOutputStream         zos,
        String                  pathPrefix,
        boolean                 ignoreDuplicates
    )
        throws IOException, InterruptedException
    {
        Enumeration <? extends ZipEntry>  entries = src.entries ();

        while (entries.hasMoreElements ()) {
            ZipEntry        srcEntry = entries.nextElement ();
            String          name = srcEntry.getName ();

            if (pathPrefix != null)
                name = pathPrefix + name;

            ZipEntry        e = new ZipEntry (name);

            e.setTime (srcEntry.getTime ());
            e.setSize (srcEntry.getSize ());

            try {
                zos.putNextEntry (e);
            } catch (ZipException x) {
                if (ignoreDuplicates && x.getMessage ().startsWith ("duplicate entry"))
                    continue;

                throw x;
            }

            InputStream     is = src.getInputStream (srcEntry);

            try {
                StreamPump.pump (is, zos);
            } finally {
                is.close ();
            }

            zos.closeEntry ();
        }
    }

    public static String replaceProperties(Properties properties, File xmlFile, String xml) throws IOException {
        Pattern p = Pattern.compile("\\$\\{([^\\}]*)\\}");
        Matcher m = p.matcher(xml);

        StringBuffer result = new StringBuffer (xml.length() + 128);

        while (m.find()) {
            String propertyName = m.group(1);
            String propertyValue = properties.getProperty(propertyName);
            if (propertyValue != null) {
                // escape / and $ as they have special meaning for Matcher.appendReplacement()
                propertyValue = propertyValue.replace ("\\", "\\\\");
                propertyValue = propertyValue.replace ("$", "\\$");
                m.appendReplacement(result, propertyValue);
            } else {
                throw new IOException ("Cannot find system property \"" + propertyName + "\" defined in " + xmlFile.getAbsolutePath());
            }
        }
        m.appendTail(result);

        return result.toString();
    }

    public static String replaceProperties(Properties properties, File xmlFile) throws IOException, InterruptedException {
        String xml = BasicIOUtil.readTextFile(xmlFile);
        return replaceProperties(properties, xmlFile, xml);
    }

    /** Replaces System properties defined in given file as "${property name}" into "{property value}" */
    public static String replaceSystemProperties (File xmlFile) throws IOException, InterruptedException {
        return replaceProperties(System.getProperties(), xmlFile);
    }

    /** Replaces System properties defined in given string "${property name}" into "{property value}" */
    public static String replaceSystemProperties(File xmlFile, String xml) throws IOException {
        return replaceProperties(System.getProperties(), xmlFile, xml);
    }

    public static LineNumberReader toLineNumberReader (Reader r) {
        return (
            r instanceof LineNumberReader ?
                (LineNumberReader) r :
                new LineNumberReader (r)
        );
    }

    public static void             dumpWithLineNumbers (Reader text, PrintWriter out)
        throws IOException
    {
        LineNumberReader        lnr = toLineNumberReader (text);
        String                  line;

        while ((line = lnr.readLine ()) != null)
            out.printf ("%-4d: %s\n", lnr.getLineNumber (), line);
    }

    public static void             dumpWithLineNumbers (Reader text, PrintStream out)
        throws IOException
    {
        LineNumberReader        lnr = toLineNumberReader (text);
        String                  line;

        while ((line = lnr.readLine ()) != null)
            out.printf ("%-4d: %s\n", lnr.getLineNumber (), line);
    }

    public static void             dumpWithLineNumbers (CharSequence text, PrintWriter out) {
        try {
            dumpWithLineNumbers (new CharSequenceReader (text), out);
        } catch (IOException x) {
            throw new RuntimeException (x);
        }
    }
    
    public static void             dumpWithLineNumbers (CharSequence text, PrintStream out) {
        try {
            dumpWithLineNumbers (new CharSequenceReader (text), out);
        } catch (IOException x) {
            throw new RuntimeException (x);
        }
    }
    
    public static void              recursiveListFiles(File root, FileFilter filter, Collection<File> listFiles)   {
        if (root != null && root.isDirectory ()) {

            final File[] contents = root.listFiles ();

            if (contents != null) {
                for (int index = 0; index < contents.length; index++) {
                    final File f = contents[index];
                    if (f.isDirectory ()) {
                        recursiveListFiles (f,
                                            filter,
                                            listFiles);
                    } else {
                        if (filter.accept (f))
                            listFiles.add (f);
                    }
                }
            }
        }
    }

    private static void     expandChildPath (
        File                    root,
        String []               dirs, 
        int                     dirPos,
        ArrayList <File>        out,
        boolean                 assertExists,
        Comparator <File>       comparator
    ) 
        throws FileNotFoundException
    {
        File                    cur = root;
        
        for (;;) {
            if (dirPos == dirs.length) {
                out.add (cur);
                return;
            }
                
            String          s = dirs [dirPos++];
            
            if (ShellPatternCSMatcher.isPattern (s)) {
                File []     children = cur.listFiles ();
                
                if (assertExists && !cur.isDirectory ())
                    throw new FileNotFoundException ("Not a directory: " + cur.getPath ()); 
                
                if (children == null) 
                    return;
                
                if (comparator != null)
                    Arrays.sort (children, comparator);
                
                for (File child : children) {
                    if (ShellPatternCSMatcher.INSTANCE.matches (child.getName (), s))
                        expandChildPath (child, dirs, dirPos, out, false, comparator);
                }
                
                return;
            }
            
            cur = new File (cur, s);

            if (assertExists && !cur.exists ())
                throw new FileNotFoundException (cur.getPath ());            
        }        
    }
    
    public static ArrayList <File>  expandPath (String path) 
        throws FileNotFoundException 
    {
        return (expandPath (path, true));
    }
    
    public static ArrayList <File>  expandPath (
        String                          path, 
        boolean                         assertRootExists
    ) 
        throws FileNotFoundException 
    {
        return (expandPath (path, new ComparableComparator<File> (), assertRootExists));
    }
    
    public static ArrayList <File>  expandPath (
        String                          path, 
        Comparator <File>               comparator,
        boolean                         assertRootExists
    ) 
        throws FileNotFoundException 
    {
        String []           dirs = path.replace ('\\', '/').split ("/");
        ArrayList <File>    files = new ArrayList <> ();

        File root = new File(dirs[0].endsWith(":") ? dirs[0] + "\\" : dirs[0]);

        expandChildPath (root, dirs, 1, files, assertRootExists, comparator);
        return (files);
    }    
    
    public static String            fname (String path) {
        int a = path.lastIndexOf ('/');
        int b = path.lastIndexOf ('\\');
        
        if (a < 0 && b < 0)
            return (path);
        
        return (path.substring (Math.max (a, b) + 1));        
    } 
    
    public static String            fhead (String path) {
        int     n = path.lastIndexOf ('.');
        
        return (n < 0 ? path : path.substring (0, n));
    }
}