package deltix.util.os;

import deltix.util.lang.Util;
import deltix.util.lang.StringUtils;

import java.io.*;

/**
 * Date: May 28, 2010
 *
 * @author alex
 */
public class Executor {

    static class StreamGrabber extends Thread {
        private InputStream is;
        private PrintStream out;

        StreamGrabber(InputStream is, PrintStream out) {
            this.is = is;
            this.out = out;
        }

        public void run() {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ( (line = reader.readLine()) != null)
                    out.println(line);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                Util.close(reader);
            }
        }
    }    

    /**
     * Executes the specified file.
     *
     * @param   file      a specified file to execute     
     *
     * @throws  SecurityException
     *          If a security manager exists and its
     *          {@link SecurityManager#checkExec checkExec}
     *          method doesn't allow creation of the subprocess
     *
     * @throws  IOException
     *          If an I/O error occurs
     *
     * @throws  NullPointerException
     *          If <code>command</code> is <code>null</code>
     *
     * @throws  IllegalArgumentException
     *          If <code>command</code> is empty
     *
     * @see     Runtime#exec(String[], String[], java.io.File)
     * @see     ProcessBuilder
     */

    public static void exec(File file) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/C", "start", StringUtils.quote(""),
                StringUtils.quote(file.getPath()));
        builder.start();
    }

    public static void exec(ProcessBuilder builder, boolean redirect) throws IOException {
        Process process = builder.start();
        
        if (redirect) {
            new StreamGrabber(process.getErrorStream(), System.err).start();
            new StreamGrabber(process.getInputStream(), System.out).start();
        }
    }

}
