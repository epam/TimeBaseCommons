package deltix.util.os;

import deltix.util.lang.Util;
import deltix.util.lang.StringUtils;

import java.io.*;
import java.util.*;
import java.util.prefs.*;

/**
 *
 */
public final class WindowsOS {
    public static final boolean     IS_X64;
    public static final boolean     IS_X86;
    public static final boolean     IS_VISTA;

    static {
        String      PROC_ID = System.getenv ("PROCESSOR_IDENTIFIER");

        IS_X64 = PROC_ID != null && PROC_ID.contains ("64");

        IS_X86 = !IS_X64;
        
        String osName = System.getProperty ( "os.name" );
        IS_VISTA = osName.startsWith ( "Windows Vista" );
    }

    // mkshortcut.vbs
//    static String mkshortcut =
//        "set WshShell = WScript.CreateObject(\"WScript.Shell\" )\n" +
//        "set oShellLink = WshShell.CreateShortcut(Wscript.Arguments.Named(\"shortcut\") & \".lnk\")\n" +
//        "oShellLink.TargetPath = Wscript.Arguments.Named(\"target\")\n" +
//        "oShellLink.IconLocation = Wscript.Arguments.Named(\"icon\")\n" +
//        "oShellLink.WindowStyle = 1\n" +
//        "oShellLink.Save";

     static String mkshortcut =
        "set WshShell = WScript.CreateObject(\"WScript.Shell\" )\n" +
        "set oShellLink = WshShell.CreateShortcut(\"%s.lnk\")\n" +
        "oShellLink.TargetPath = %s \n" +
        "oShellLink.IconLocation = %s\n" +
        "oShellLink.WindowStyle = 1\n" +
        "oShellLink.Save";

    public static final String      getSystemDrive () {
        String      sysdrive = System.getenv ("C:");
        
        if (sysdrive == null)
            sysdrive = "C:";
        
        return (sysdrive);
    }
    
    public static final String      getProgramFiles () {
        String      pf = System.getenv ("ProgramFiles");
        
        if (pf == null)
            pf = getSystemDrive () + "\\Program Files";
        
        return (pf);
    }
    
    public static final String      getSystemRoot () {
        String      pf = System.getenv ("SystemRoot");
        
        if (pf == null)
            pf = getSystemDrive () + "\\Windows";
        
        return (pf);
    }
    
    public static final String      getAllUsersProfile () {
        String      pf = System.getenv ("ALLUSERSPROFILE");

        if (pf == null)
            pf = getSystemDrive () + "\\Documents and Settings\\All Users";
    
        return (pf);
    }
    
    public static final String      getPublic () {
        String      p = System.getenv ("PUBLIC");

        if (p == null)
            p = getSystemDrive () + "\\Users\\Public";
        return (p);
        
    }

    public static final String      getUserName () {
        String      pf = System.getenv ("USERNAME");

        if (pf == null)
            pf = "Administrator";

        return (pf);
    }

    public static final String      getUserProfile () {
        String      pf = System.getenv ("USERPROFILE");

        if (pf == null)
            pf = getSystemDrive () + "\\Documents and Settings\\" + getUserName ();

        return (pf);
    }
    
    public static final File       getDotNetHome () {
        return (getDotNetHome (-1));
    }
    
    public static final File       getDotNetHome (int version) {
        return (getDotNetHome (false, version));
    }
    
    public static final File       getDotNetHome (boolean force32, int version) {
        File        dotNet = new File (getSystemRoot (), "Microsoft.NET");
        
        if (!dotNet.isDirectory ())
            return (null);
        
        File        framework = null;
        
        if (!force32) {
            framework = new File (dotNet, "framework64");
            if (!framework.isDirectory ())
                framework = null;
        }
        
        if (framework == null) {
            framework = new File (dotNet, "framework");
            
            if (!framework.isDirectory ())
                framework = null;
        }
        
        if (framework == null)
            return (null);
        
        final String    start = 
            version < 1 ? "v" :"v" + version + ".";
        
        File []     homes = 
            framework.listFiles (
                new FileFilter () {
                    public boolean accept (File f) {
                        return (f.isDirectory () && f.getName ().startsWith (start));
                    }                    
                }
            );
        
        if (homes == null)
            return (null);
        
        Arrays.sort (homes);
        
        return (homes [homes.length - 1]);
    }

    public static void createShortcut(File target, File location, File icon)
            throws IOException
    {
        createShortcut(target.getAbsolutePath(), location.getAbsolutePath(), icon.getAbsolutePath());
    }

    public static void createShortcut(String target, String location, String icon)
            throws IOException
    {
        // create script that will make shortcut
        File script = File.createTempFile("shcut", ".vbs");
        script.deleteOnExit();
        FileWriter writer = null;
        try {
            writer = new FileWriter(script);
            writer.write(String.format(mkshortcut,
                    location, StringUtils.quote(target), StringUtils.quote(icon)));
            writer.close();
            writer = null;
        } finally {
            Util.close(writer);
        }
        
        (new ProcessBuilder("cmd.exe", "/K", StringUtils.quote(script.getAbsolutePath()))).start();
    }
    
    public static boolean asAdministrator () {
        // attempt to set a preference
        try {
            final String path = "/deltix/dummyPref";
            final Preferences prefs = Preferences.systemRoot ().node (path);
            prefs.putLong ("dummyKey",
                           System.currentTimeMillis ());
            prefs.flush ();
            return true;
        } catch (final Exception e) {
            return false;
        }
    }
    
    public static void main (String [] args) throws Exception {
        System.out.println (getDotNetHome ());
    }

    
}
