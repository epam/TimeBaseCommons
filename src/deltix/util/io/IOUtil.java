package deltix.util.io;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import java.nio.channels.FileLock;

import deltix.util.Util;

/**
 *
 */
public class IOUtil {
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

    public static void		writeTextFile (String filepath, String content)
        throws IOException
    {
        FileWriter fw = new FileWriter (filepath);

        fw.write(content);
        fw.close ();
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
        FileInputStream         fis = new FileInputStream (file);

        try {
            new DataInputStream (fis).readFully (bytes, offset, length);
        } finally {
            Util.close (fis);
        }
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

    public static void      extractResource (String resource, File dest)
        throws IOException, InterruptedException
    {
        InputStream		is = null;
        OutputStream    os = null;

        try {
            is = IOUtil.class.getClassLoader ().getResourceAsStream (resource);

            if (is == null)
                throw new FileNotFoundException (resource);

            os = new FileOutputStream (dest);
            StreamPump.pump (is, os);
            os.close ();
            is.close ();
        } finally {
            Util.close (is);
            Util.close (os);
        }
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

    /**
     * Method attempts to obtian exclusive lock on given file.
     * Can be used e.g. to prevent two instances of application from running at the same time.
     * Example:
     * <pre>
     * FileLock lock = null;
     * try {
     *    lock = IOUtil.lock ("fleet.client.lock", true);
     *    runApplication (...);
     * } finally {
     *       IOUtil.unlock (lock);
     * }
     * </pre>
     *
     *
     *
     * @param fileLockName - name of the log file to be created in user home directory
     * @param exitProcessOnFail - specify true to exit the process on lock failure or any other error
     * @return non-null FileLock if given file can be locked
     */
    public static final FileLock lock (String fileLockName, boolean exitProcessOnFail) {

        File lockFile = new File (System.getProperty ("user.home"), fileLockName);
        FileLock result = null;
        try {
            result = new RandomAccessFile (lockFile, "rw").getChannel().tryLock();
        } catch (IOException xcp) {
            xcp.printStackTrace();
        }

        if (result == null) {
            System.err.println("Looks like application is already running (cannot create file lock " +
                lockFile.getAbsolutePath() + ")");

            if (exitProcessOnFail)
                System.exit (1);
        }


        return result;
    }

    /**
     * @param lock - lock obtained by lock()
     */
    public static final void unlock (FileLock lock) {
        if (lock != null) {
            try {
                lock.release ();
            } catch (IOException ignore) {
                ignore.printStackTrace();
            }
        }
    }



    public static final class FileOnlyFilter implements FileFilter {

        public boolean accept(File pathname)
        {
            return pathname.isFile ();
        }    
    }
}
