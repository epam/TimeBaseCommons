package deltix.util.os;

import deltix.util.concurrent.UncheckedInterruptedException;
import deltix.util.io.UncheckedIOException;
import deltix.util.lang.Util;
import deltix.util.text.table.AlignedNoWhitespacesTable;
import deltix.util.text.table.Row;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class LinuxOS {

    
    public static boolean isX64 () {
        try {
            final ProcessBuilder pb = new ProcessBuilder ("uname",
                                                          "-m");

            pb.redirectErrorStream (true);

            final Process proc = pb.start ();
            final BufferedReader rd = new BufferedReader (new InputStreamReader (proc.getInputStream ()));

            String out = "";
            for (;;) {
                final String line = rd.readLine ();

                if (line == null)
                    break;
                out += line;
            }

            final int exitVal = proc.waitFor ();
            if (exitVal != 0)
                throw new IOException ("isX64 function failed with error code " + exitVal);

            return out.contains("64");
        } catch (IOException e) {
            throw new deltix.util.io.UncheckedIOException (e);

        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException (e);
        }
    }

    public static void startScriptInTerminal(String shell, String title, File script) {
        try {
            Runtime.getRuntime().exec(paramsForStartScriptInTerminal(shell, title, script));
        } catch (Throwable x) {
            throw new RuntimeException(x);
        }
    }

    public static void browse(URI uri) throws IOException {
        assert uri != null;

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
            Desktop.getDesktop().browse(uri);
        else
            throw new RuntimeException("Browse functionality is not supported");
    }

    public static void open(File dir) throws IOException {
        assert dir != null;

        DesktopApi.open(dir);

    }


    public static String[] paramsForStartScriptInTerminal(String shell, String title, File script, String... parameters) {

        List<String> cmdarray = new ArrayList<>();
        if (new File("/usr/bin/xterm").exists()) {
            cmdarray.add("/usr/bin/xterm");
            cmdarray.add("-T");
            cmdarray.add(title);
            cmdarray.add("-e");
        } else
        if (new File("/usr/bin/gnome-terminal").exists()) {
            cmdarray.add("/usr/bin/gnome-terminal");
            cmdarray.add("-t");
            cmdarray.add(title);
            cmdarray.add("-e");
        }
        //FIXME: do not delete - for a future use
//        else if (new File("/usr/bin/konsole").exists()) {
//            cmdarray.add("/usr/bin/konsole");
//            cmdarray.add("--title");
//            cmdarray.add(title);
//            cmdarray.add("-e");
//        }
        else {
            throw new IllegalStateException("Cann't find supported terminal. Please install 'gnome-terminal' or 'xtrem'.");
        }
        String command = shell + " '" + script.getPath() + "'";

        if (parameters != null) {
            for (String parameter : parameters) {
                command += " ";
                command += parameter;
            }
        }
        cmdarray.add(command);
        return cmdarray.toArray(new String[cmdarray.size()]);

    }

    public static LiveProcess[] getProcessList() throws Exception {
        final List<LiveProcess> result = new ArrayList<>();                
                
        final AlignedNoWhitespacesTable psTable = new AlignedNoWhitespacesTable(
                    command(new StringBuilder(), "ps", "-eo", "pid,comm,cmd")
                );

        final int pid = psTable.getColumn("PID").getIndex();
        final int name = psTable.getColumn("COMMAND").getIndex();
        final int cmd = psTable.getColumn("CMD").getIndex();
        
        for (Row row : psTable) {
            result.add(
                    new LiveProcess(Integer.parseInt(row.getValue(pid).toString()),
                    row.getValue(name).toString(),
                    row.getValue(cmd).toString()));

        }
        
        return result.toArray(new LiveProcess[result.size()]);
    }

    public static void commandNoError (String... parameters) {
        try {
            command(null, parameters);
        } catch (Exception e) {
            Executor.LOG.log(Level.FINE, "An error while execution " + Arrays.toString(parameters), e);
        }
    }
    
    public static void chown (File file, String user, boolean deep) throws IOException {
        if (deep) {
            command(null, new String[] {"chown", "-R", user, file.getAbsolutePath()});
        } else {
            command(null, new String[] {"chown", user, file.getAbsolutePath()});
        }
    }

    public static void command (String... parameters) throws IOException {
        command(System.out, parameters);
    }
    
    public static <T extends Appendable> T command (T out, String... parameters) throws IOException {
        try {

            if (Util.IS_WINDOWS_OS || parameters == null || parameters.length == 0)
                return out;
            final ProcessBuilder pb = new ProcessBuilder (parameters);

            pb.redirectErrorStream (true);

            final Process proc = pb.start ();
            final BufferedReader rd = new BufferedReader (new InputStreamReader (proc.getInputStream ()));
           
            for (; ;) {
                final String line = rd.readLine ();

                if (line == null)
                    break;
                
                if (out != null) {
                    out.append (line);
                    out.append (Util.NATIVE_LINE_BREAK);
                }
            }

            final int exitVal = proc.waitFor ();
            if (exitVal != 0)
                throw new ExecutionException(parameters[0] + " function failed with error code " + exitVal, exitVal);

        } catch (InterruptedException e) {
            throw new IOException (e);
        }
        
        return out;
    }        
    
    public static int   getCurrentProcessId () {
        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String    jvmName = ManagementFactory.getRuntimeMXBean ().getName ();
        final int       index = jvmName.indexOf ('@');

        if (index < 1) 
            throw new UnsupportedOperationException ();        

        try {
            return (Integer.parseInt (jvmName.substring (0, index)));
        } catch (NumberFormatException e) {
            // ignore
        }
        
        throw new UnsupportedOperationException ();
    }
    
    public static int           kill (int pid) 
        throws IOException, InterruptedException 
    {
        return (Runtime.getRuntime ().exec ("kill -9 " + pid).waitFor ());
    }
            
    public static void main(String[] args) throws IOException {
        //Runtime.getRuntime().exec(new String[]{"gnome-terminal", "-e", "csh -f '/home/PaharelauK/deltix/MAIN/bin/uhfshell' -connect http://localhost:8888"});
        //Runtime.getRuntime().exec(new String[]{"xterm", "-e", "csh -f '/home/PaharelauK/deltix/MAIN/bin/uhfshell' -connect http://localhost:8888"});

        System.out.println("System.getProperty(\"os.arch\") = " + System.getProperty("os.arch"));
    }
    
}
