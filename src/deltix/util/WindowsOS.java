package deltix.util;

/**
 *
 */
public class WindowsOS {
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
}
