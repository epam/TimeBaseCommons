package deltix.util.os;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import deltix.util.concurrent.*;
import deltix.util.io.*;
import deltix.util.lang.StringUtils;

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
            Runtime r = Runtime.getRuntime();
            if (new File("/usr/bin/gnome-terminal").exists()) {
                r.exec(new String[]{"/usr/bin/gnome-terminal", "-t", title, "-e", shell + " -c " + StringUtils.quote(script.getPath())});
            } else if (new File("/usr/bin/xterm").exists()) {
                r.exec(new String[]{"/usr/bin/xterm", "-T", title, "-e", shell, "-c", StringUtils.quote(script.getPath())});
            } else if (new File("/usr/bin/konsole").exists()) {
                r.exec(new String[]{"/usr/bin/konsole", "--title", title, "-e", shell, "-c", StringUtils.quote(script.getPath())});
            } else {
                throw new IllegalStateException("Cann't find any terminal. Please install 'konsole', 'gnome-terminal' or 'xtrem'.");
            }
        } catch (Throwable x) {
            throw new RuntimeException(x);
        }
    }


    public static void startScriptInTerminal2(String title, File script, String... parameters) {
        try {
            Runtime r = Runtime.getRuntime();
            r.exec(paramsForStartScriptInTerminal(title, script, parameters));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] paramsForStartScriptInTerminal(String title, File script, String... parameters) {
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
        } else if (new File("/usr/bin/konsole").exists()) {
            cmdarray.add("/usr/bin/konsole");
            cmdarray.add("--title");
            cmdarray.add(title);
            cmdarray.add("-e");
            cmdarray.add("csh");
            cmdarray.add("-c");
        } else {
            throw new IllegalStateException("Cann't find any terminal. Please install 'konsole', 'gnome-terminal' or 'xtrem'.");
        }
        String command = "'" + script.getPath() + "'";

        if (parameters != null) {
            for (String parameter : parameters) {
                command+= " ";
                command+= parameter;
            }
        }
       cmdarray.add(command);

       return cmdarray.toArray(new String[cmdarray.size()]);

    }


    public static void command (String... parameters) {
        try {

            if (parameters == null || parameters.length == 0)
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

}
