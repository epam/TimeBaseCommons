package deltix.util.lang;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 *
 */
public class WindowsOS {
    public static final boolean     IS_X64 =
        "AMD64".equalsIgnoreCase (System.getenv ("PROCESSOR_ARCHITECTURE"));
    
    public static final boolean     IS_X86 =
        "X86".equalsIgnoreCase (System.getenv ("PROCESSOR_ARCHITECTURE"));

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
    
    public static void main (String [] args) throws Exception {
        System.out.println (getDotNetHome ());
    }
}
