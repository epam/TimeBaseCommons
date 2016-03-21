package deltix.util.os;

import deltix.util.lang.Util;
import deltix.util.lang.StringUtils;

import java.io.*;
import java.util.logging.Logger;

/**
 * Date: May 28, 2010
 *
 * @author alex
 */
public class Executor {
    
    public static final Logger LOG = Logger.getLogger("deltix.util.os");

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
     * @param   title     a specified process title
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
    public static void exec(File file, String title) throws IOException {
        if (Util.IS_WINDOWS_OS){
            exec (null, title, StringUtils.quote (file.getPath ()));
        }
        else{
            LinuxOS.startScriptInTerminal("csh", title, file);
        }
    }

    public static void      exec (
        File                    dir,
        String                  title, 
        String ...              cmd
    )
        throws IOException 
    {
        if (title == null)
            title = cmd [0];
        
        if (Util.IS_WINDOWS_OS){
            ProcessBuilder  builder = 
                new ProcessBuilder ("cmd.exe", "/C", "start", StringUtils.quote (title));
            
            for (String s : cmd)
                builder.command ().add (s);
            
            if (dir != null)
                builder.directory (dir);
            
        	builder.start ();
        }
        else{
            String[] params = new String[cmd.length - 1];
            System.arraycopy(cmd, 1, params, 0, params.length);
            ProcessBuilder builder = new ProcessBuilder(LinuxOS.paramsForStartScriptInTerminal("sh", title, new File(dir, cmd[0]), params));
            if (dir != null) builder.directory(dir);
         
            builder.start ();        
        }
    }

    public static void exec(File file) throws IOException {
        exec(file, "");
    }

    public static void exec(ProcessBuilder builder, boolean redirect) throws IOException {
        Process process = builder.start();
        
        if (redirect) {
            new StreamGrabber(process.getErrorStream(), System.err).start();
            new StreamGrabber(process.getInputStream(), System.out).start();            
        }
    }

}
