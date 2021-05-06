package com.epam.deltix.util.os;

import com.sun.jna.platform.win32.Shell32Util;

import java.io.File;

/**
 *
 */
public class SystemInfo {

    public static final boolean  IS_WINDOWS      = System.getProperty ("path.separator").equals(";");

    /*
        Returns platform-specific 'UserProfile' path
     */
    public static String            getUserProfile() {
        return IS_WINDOWS ?
                System.getenv("USERPROFILE") :
                System.getProperty("user.home");
    }

    /*
        Returns platform-specific user 'AppData' path
     */
    public static String            getAppData() {
        if (IS_WINDOWS) {
            String path = System.getenv("APPDATA");
            if (path == null)
                path = Shell32Util.getFolderPath(WindowsUtils.CSIDL_APPDATA);

            return path != null ? path : getUserProfile();
        } else {
            return getUserProfile();
        }
    }

    /*
       Returns platform-specific 'Application Data' location for the given application
       Linux path is '<APPDATA>/.application'
       Windows path is '<APPDATA>/application'
    */
    public static File getAppData(String application) {
        String path = getAppData();
        return new File(path, IS_WINDOWS ? application : "." + application);
    }
    
    public static void main(String[] args) {
        System.out.println("User profile: " + getUserProfile());
        System.out.println("AppData: " + getAppData());
    }
}
