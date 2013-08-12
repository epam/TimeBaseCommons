package deltix.util.os;

import deltix.util.lang.*;
import java.io.*;

/**
 *
 */
public class OSUtil {
    public static File  getUserHome () {
        return (new File (System.getProperty ("user.home")));
    }

    public static boolean isX64() {
        if (Util.IS_WINDOWS_OS) {
            return !WindowsOS.IS_X86;
        } else {
            return LinuxOS.isX64();
        }
    }
    
    public static int   getProcessId () {
        if (Util.IS_WINDOWS_OS)
            return (WindowsOS.getCurrentProcessId ());

        return (LinuxOS.getCurrentProcessId ());
    }
    
    public static int   kill (int pid) throws IOException, InterruptedException {
        if (Util.IS_WINDOWS_OS) 
            return (WindowsOS.kill (pid));
        
        return (LinuxOS.kill (pid));
    }
    
    public static void   makeSureIsKilled (int pid) throws IOException, InterruptedException {
        if (kill (pid) != 0)
            throw new IOException ("Unable to kill process " + pid);        
    }
}
