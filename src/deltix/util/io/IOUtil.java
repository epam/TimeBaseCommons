package deltix.util.io;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

import deltix.util.lang.Util;
import deltix.util.memory.MemoryDataInput;
import deltix.util.memory.MemoryDataOutput;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.*;

/**
 *
 */
public class IOUtil {
    public static final String  CR = System.getProperty ("line.separator");

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
            throw new UncheckedIOException (iox);
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
            throw new UncheckedIOException ("Failed to delete " + f);
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
        FileOutputStream	out = null;

        try {
            in = new FileInputStream (src);
            out = new FileOutputStream (dest);

            StreamPump.pump (in, out);
        } finally {
            Util.close (in);
            Util.close (out);
        }
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
        mkDirIfNeeded (f.getParentFile ());
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

        if (includeThisFile)
            file.delete ();
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

    public static String		readTextFromClassPath (String relPath)
        throws IOException, InterruptedException
    {
        InputStream		is = openResourceAsStream (relPath);

        try {
            return (readFromReader (new InputStreamReader (is)));
        } finally {
            Util.close (is);
        }
    }

    public static String []     readLinesFromClassPath (String relPath)
        throws IOException, InterruptedException
    {
        InputStream		is = openResourceAsStream (relPath);

        try {
            return (readLinesFromReader (new InputStreamReader (is)));
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
        FileReader      fr = null;

        try {
            fr = new FileReader (f);
            return (readLinesFromReader (fr));
        } finally {
            Util.close (fr);
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

    /**
     */
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

            lines.add (line);
        }

        return (lines.toArray (new String [lines.size ()]));
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

    public static void		readBytes (
        File                    file,
        byte []                 bytes,
        int                     offset,
        int                     length
    )
        throws IOException
    {
        FileInputStream         fis = null;

        try {
        	fis = new FileInputStream (file);
            new DataInputStream (fis).readFully (bytes, offset, length);
        } finally {
        	if (fis != null)
        		Util.close (fis);
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
        Properties		props = new Properties ();
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
            IOUtil.class.getClassLoader ().getResourceAsStream (relPath);

        if (is == null)
            throw new FileNotFoundException ("CLASSPATH/" + relPath);

        return (is);
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
        Properties		props = new Properties ();
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

    public static ZipEntry [] listZipEntries (File f, String regex)
        throws IOException, InterruptedException
    {
        FileInputStream         fis = new FileInputStream (f);
        ArrayList <ZipEntry>    ret = new ArrayList <ZipEntry> ();
        Matcher                 m = null;

        if (regex != null) {
            Pattern                 pat = Pattern.compile (regex);
            m = pat.matcher ("");
        }

        try {
            ZipInputStream      zis = new ZipInputStream (fis);

            for (;;) {
                ZipEntry        zentry = zis.getNextEntry ();

                if (zentry == null)
                    break;

                if (m != null) {
                    m.reset (zentry.getName ());

                    if (!m.matches ())
                        continue;
                }

                ret.add (zentry);
            }
        } finally {
            Util.close (fis);
        }

        return (ret.toArray (new ZipEntry [ret.size ()]));
    }

    public static void          extractZipStream (InputStream is, File destDir)
        throws IOException, InterruptedException
    {
        ZipInputStream      zis = new ZipInputStream (is);

        for (;;) {
            ZipEntry        zentry = zis.getNextEntry ();

            if (zentry == null)
                break;

            String          name = zentry.getName ();
            File            destFile = new File (destDir, name);

            if (name.endsWith ("/"))
                mkDirIfNeeded (destDir);
            else {
                mkParentDirIfNeeded (destFile);

                FileOutputStream    fos = new FileOutputStream (destFile);

                try {
                    StreamPump.pump (zis, fos);
                    fos.close ();
                    fos = null;
                } finally {
                    Util.close (fos);
                }
            }
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
     *	Read a serializable object from file.
     *
     *	@param in_is		InputStream to read from.
     *	@param compress		Whether to treat the file is zipped.
     */
    public static Serializable	readSerializable (InputStream in_is, boolean compress)
        throws IOException, ClassNotFoundException
    {
        InputStream			is = openMaybeZip (in_is, compress);
        ObjectInputStream	ois = new ObjectInputStream (is);
        Serializable		ret = (Serializable) ois.readObject ();

        return (ret);
    }

    /**
     *	Read a serializable object from file.
     *
     *	@param f			File to read from.
     *	@param compress		Whether to treat the file is zipped.
     */
    public static Serializable	readSerializable (File f, boolean compress)
        throws IOException, ClassNotFoundException
    {
        FileInputStream		fis = new FileInputStream (f);

        try {
            return (readSerializable (fis, compress));
        } finally {
            Util.close (fis);
        }
    }

    /**
     *	Read a serializable object from file. If the file has the ".zip" extension,
     *	it is treated as compressed.
     *
     *	@param f			File to read from.
     */
    public static Serializable	readSerializable (File f)
        throws IOException, ClassNotFoundException
    {
        return (readSerializable (f, looksLikeZip (f)));
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
     *  Writes any CharSequence to DataOutput as a 2-byte length (in characters), followed by
     *  that many characters in raw 2-byte form.
     */
    public static int      writeUnicode (CharSequence str, MemoryDataOutput out) throws IOException {
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
     *  Writes any CharSequence to MemoryDataOutput in a way identical to
     *  DataOutputStream.writeUTF, which is groundlessly defined too narrowly
     *  by forcing the argument to be a String.
     */
    public static int      writeUTF (CharSequence str, MemoryDataOutput out) throws IOException {
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

        out.writeByte((utflen >>> 8) & 0xFF);
        out.writeByte((utflen >>> 0) & 0xFF);

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
    public final static void readUTF(DataInput in, Appendable sb) throws IOException {
        int utflen = in.readUnsignedShort();

        if (utflen == 0)
            return;

        int c = -2;
        int char2, char3;
        int count = 0;

        for (;;) {
            c = in.readByte ();
            if (c > 127)
                break;

            count++;
            sb.append ((char) c);

            if (count >= utflen)
                return;
        }
        //  If we are here, we have broken out of the previous loop and there is an
        //  unhandled escape character in variable c.
        for (;;) {
            switch (c >> 4) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    /* 0xxxxxxx*/
                    count++;
                    sb.append ((char)c);
                    break;

                case 12: case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = in.readByte ();
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException(
                            "malformed input around byte " + count);
                    sb.append ((char)(((c & 0x1F) << 6) |
                                                    (char2 & 0x3F)));
                    break;

                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = in.readByte ();
                    char3 = in.readByte ();
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException(
                            "malformed input around byte " + (count-1));
                    sb.append ((char)(((c & 0x0F) << 12) |
                                                    ((char2 & 0x3F) << 6)  |
                                                    ((char3 & 0x3F) << 0)));
                    break;

                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException(
                        "malformed input around byte " + count);
            }

            if (count >= utflen)
                break;

            c = in.readByte ();
        }
    }

    /**
     *  Reads (appends) a UTF string to an Appendable (such as StringBuidler),
     *  without clearing it first.
     */
    public final static void readUTF(ByteBuffer in, Appendable sb) throws BufferUnderflowException, IOException {
        int utflen = 0xFFFF & in.getShort();

        if (utflen == 0)
            return;

        int c = -2;
        int char2, char3;
        int count = 0;

        for (;;) {
            c = in.get ();
            if (c > 127)
                break;

            count++;
            sb.append ((char) c);

            if (count >= utflen)
                return;
        }
        //  If we are here, we have broken out of the previous loop and there is an
        //  unhandled escape character in variable c.
        for (;;) {
            switch (c >> 4) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    /* 0xxxxxxx*/
                    count++;
                    sb.append ((char)c);
                    break;

                case 12: case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = in.get ();
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException(
                            "malformed input around byte " + count);
                    sb.append ((char)(((c & 0x1F) << 6) |
                                                    (char2 & 0x3F)));
                    break;

                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = in.get ();
                    char3 = in.get ();
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException(
                            "malformed input around byte " + (count-1));
                    sb.append ((char)(((c & 0x0F) << 12) |
                                                    ((char2 & 0x3F) << 6)  |
                                                    ((char3 & 0x3F) << 0)));
                    break;

                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException(
                        "malformed input around byte " + count);
            }

            if (count >= utflen)
                break;

            c = in.get ();
        }
    }

    /**
     *  Reads (appends) a UTF string to an Appendable (such as StringBuidler),
     *  without clearing it first.
     */
    public final static void readUTF(MemoryDataInput in, Appendable sb) throws IOException {
        int utflen = in.readUnsignedShort();

        if (utflen == 0)
            return;

        int c = -2;
        int char2, char3;
        int count = 0;

        for (;;) {
            c = in.readByte ();
            if (c > 127)
                break;

            count++;
            sb.append ((char) c);

            if (count >= utflen)
                return;
        }
        //  If we are here, we have broken out of the previous loop and there is an
        //  unhandled escape character in variable c.
        for (;;) {
            switch (c >> 4) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    /* 0xxxxxxx*/
                    count++;
                    sb.append ((char)c);
                    break;

                case 12: case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = in.readByte ();
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException(
                            "malformed input around byte " + count);
                    sb.append ((char)(((c & 0x1F) << 6) |
                                                    (char2 & 0x3F)));
                    break;

                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = in.readByte ();
                    char3 = in.readByte ();
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException(
                            "malformed input around byte " + (count-1));
                    sb.append ((char)(((c & 0x0F) << 12) |
                                                    ((char2 & 0x3F) << 6)  |
                                                    ((char3 & 0x3F) << 0)));
                    break;

                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException(
                        "malformed input around byte " + count);
            }

            if (count >= utflen)
                break;

            c = in.readByte ();
        }
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

    /** Replaces System properties defined in given file as "${property name}" into "{property value}" */
    public static String replaceSystemProperties (File xmlFile) throws IOException, InterruptedException {
        String xml = IOUtil.readTextFile(xmlFile);
        return replaceSystemProperties(xmlFile, xml);
    }

    /** Replaces System properties defined in given string "${property name}" into "{property value}" */
    public static String replaceSystemProperties(File xmlFile, String xml) throws IOException {
        Pattern p = Pattern.compile("\\$\\{([^\\}]*)\\}");
        Matcher m = p.matcher(xml);

        StringBuffer result = new StringBuffer (xml.length()+128);

        while (m.find()) {
             String propertyName = m.group(1);
             String propertyValue = System.getProperty(propertyName);
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

}
