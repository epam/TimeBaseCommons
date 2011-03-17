package deltix.util.os;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import deltix.util.concurrent.*;
import deltix.util.io.*;
import deltix.util.lang.StringUtils;
import deltix.util.lang.Util;
import org.springframework.util.Assert;

import javax.swing.*;

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

            return out.indexOf ("64") != -1;
        } catch (IOException e) {
            throw new UncheckedIOException (e);

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
        Assert.notNull(uri);

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
            Desktop.getDesktop().browse(uri);
        else
            throw new RuntimeException("Browse functionality is not supported");
    }

    public static void open(File dir) throws IOException {
        Assert.notNull(dir);

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
            Desktop.getDesktop().open(dir);
        else
            throw new RuntimeException("Open functionality is not supported");
    }


    public static String[] paramsForStartScriptInTerminal(String shell, String title, File script, String... parameters) {

        List<String> cmdarray = new ArrayList<String>();
        if (new File("/usr/bin/gnome-terminal").exists()) {
            cmdarray.add("/usr/bin/gnome-terminal");
            cmdarray.add("-t");
            cmdarray.add(title);
            cmdarray.add("-e");
        } else if (new File("/usr/bin/xterm").exists()) {
            cmdarray.add("/usr/bin/xterm");
            cmdarray.add("-T");
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
        String command = shell + " -f '" + script.getPath() + "'";

        if (parameters != null) {
            for (String parameter : parameters) {
                command += " ";
                command += parameter;
            }
        }
        cmdarray.add(command);
        return cmdarray.toArray(new String[cmdarray.size()]);

    }


    public static void command (String... parameters) {
        try {

            if (Util.IS_WINDOWS_OS || parameters == null || parameters.length == 0)
                return;
            final ProcessBuilder pb = new ProcessBuilder (parameters);

            pb.redirectErrorStream (true);

            final Process proc = pb.start ();
            final BufferedReader rd = new BufferedReader (new InputStreamReader (proc.getInputStream ()));

            for (; ;) {
                final String line = rd.readLine ();

                if (line == null)
                    break;
                System.out.println (line);
            }

            final int exitVal = proc.waitFor ();
            if (exitVal != 0)
                throw new IOException (parameters[0] + " function failed with error code " + exitVal);

        } catch (IOException e) {
            throw new UncheckedIOException (e);

        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException (e);
        }
    }

    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec(new String[]{"gnome-terminal", "-e", "csh -f '/home/PaharelauK/deltix/MAIN/bin/uhfshell' -connect http://localhost:8888"});
        Runtime.getRuntime().exec(new String[]{"xterm", "-e", "csh -f '/home/PaharelauK/deltix/MAIN/bin/uhfshell' -connect http://localhost:8888"});
    }

}
