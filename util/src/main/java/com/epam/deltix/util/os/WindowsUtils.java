package com.epam.deltix.util.os;

import com.github.sarxos.winreg.HKey;

import java.io.*;

public class WindowsUtils {

    // FIXME: do not delete - for a future use
    public static final int     CSIDL_DESKTOP                 = 0x0000;
    public static final int     CSIDL_INTERNET                = 0x0001;
    public static final int     CSIDL_PROGRAMS                = 0x0002;
    public static final int     CSIDL_CONTROLS                = 0x0003;
    public static final int     CSIDL_PRINTERS                = 0x0004;
    public static final int     CSIDL_PERSONAL                = 0x0005;
    public static final int     CSIDL_FAVORITES               = 0x0006;
    public static final int     CSIDL_STARTUP                 = 0x0007;
    public static final int     CSIDL_RECENT                  = 0x0008;
    public static final int     CSIDL_SENDTO                  = 0x0009;
    // Recycle Bin
    public static final int     CSIDL_BITBUCKET               = 0x000A;
    public static final int     CSIDL_STARTMENU               = 0x000B;
    public static final int     CSIDL_MYDOCUMENTS             = 0x000C;
    public static final int     CSIDL_MYMUSIC                 = 0x000D;
    public static final int     CSIDL_MYVIDEO                 = 0x000E;
    public static final int     CSIDL_DESKTOPDIRECTORY        = 0x0010;
    // My Computer
    public static final int     CSIDL_DRIVES                  = 0x0011;
    public static final int     CSIDL_NETWORK                 = 0x0012;
    public static final int     CSIDL_NETHOOD                 = 0x0013;
    public static final int     CSIDL_FONTS                   = 0x0014;
    public static final int     CSIDL_TEMPLATES               = 0x0015;
    public static final int     CSIDL_COMMON_STARTMENU        = 0x0016;
    public static final int     CSIDL_COMMON_PROGRAMS         = 0x0017;
    public static final int     CSIDL_COMMON_STARTUP          = 0x0018;
    public static final int     CSIDL_COMMON_DESKTOPDIRECTORY = 0x0019;
    public static final int     CSIDL_APPDATA                 = 0x001A;
    public static final int     CSIDL_PRINTHOOD               = 0x001B;
    public static final int     CSIDL_LOCAL_APPDATA           = 0x001C;
    public static final int     CSIDL_ALTSTARTUP              = 0x001D;
    public static final int     CSIDL_COMMON_ALTSTARTUP       = 0x001E;
    public static final int     CSIDL_COMMON_FAVORITES        = 0x001F;
    public static final int     CSIDL_INTERNET_CACHE          = 0x0020;
    public static final int     CSIDL_COOKIES                 = 0x0021;
    public static final int     CSIDL_HISTORY                 = 0x0022;
    public static final int     CSIDL_COMMON_APPDATA          = 0x0023;
    public static final int     CSIDL_WINDOWS                 = 0x0024;
    public static final int     CSIDL_SYSTEM                  = 0x0025;
    public static final int     CSIDL_PROGRAM_FILES           = 0x0026;
    public static final int     CSIDL_MYPICTURES              = 0x0027;
    public static final int     CSIDL_PROFILE                 = 0x0028;
    public static final int     CSIDL_PROGRAM_FILES_COMMON    = 0x002B;
    public static final int     CSIDL_COMMON_TEMPLATES        = 0x002D;
    public static final int     CSIDL_COMMON_DOCUMENTS        = 0x002E;
    public static final int     CSIDL_COMMON_ADMINTOOLS       = 0x002F;
    public static final int     CSIDL_ADMINTOOLS              = 0x0030;
    public static final int     CSIDL_COMMON_MUSIC            = 0x0035;
    public static final int     CSIDL_COMMON_PICTURES         = 0x0036;
    public static final int     CSIDL_COMMON_VIDEO            = 0x0037;
    public static final int     CSIDL_CDBURN_AREA             = 0x003B;
    public static final int     CSIDL_PROFILES                = 0x003E;
    public static final int     CSIDL_FLAG_CREATE             = 0x8000;

    private static final String SHELL_FOLDERS_REG_KEY         = "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders";

    // FIXME: do not delete - for a future use
    static native String getSpecialFolderPath (int csidl);

    private final static com.github.sarxos.winreg.WindowsRegistry registry = com.github.sarxos.winreg.WindowsRegistry.getInstance();

    public static String regQuery (final HKey hive,
                                   final String keyName,
                                   final String valueName) {

        try {
            return registry.readString (hive, keyName, valueName);
        } catch (final Throwable e) {
            return null;
        }
    }

    public static String regQueryCurrentUserDesktopPath () {
        return regQuery (HKey.HKCU,
                         SHELL_FOLDERS_REG_KEY,
                         "Desktop");
    }

    public static String regQueryCurrentUserStartMenuPath () {
        return regQuery (HKey.HKCU,
                         SHELL_FOLDERS_REG_KEY,
                         "Start Menu");
    }

    public static String regQueryCommonDesktopPath () {
        return regQuery (HKey.HKLM,
                         SHELL_FOLDERS_REG_KEY,
                         "Common Desktop");
    }

    public static String regQueryCommonStartMenuPath () {
        return regQuery (HKey.HKLM,
                         SHELL_FOLDERS_REG_KEY,
                         "Common Start Menu");

    }

    public static String regQueryCommonProgramsPath () {
        return regQuery (HKey.HKLM,
                         SHELL_FOLDERS_REG_KEY,
                         "Common Programs");

    }

    public static File getCurrentUserDesktopDir () {
        final String currentUserDesktopPath = regQueryCurrentUserDesktopPath ();
        return currentUserDesktopPath != null ? new File (currentUserDesktopPath) : null;
    }

    public static File getCurrentUserStartMenuDir () {
        final String currentUserStartMenuPath = regQueryCurrentUserStartMenuPath ();
        return currentUserStartMenuPath != null ? new File (currentUserStartMenuPath) : null;
    }

    public static File getCommonUserDesktopDir () {
        final String commonUserDesktopPath = regQueryCommonDesktopPath ();
        return commonUserDesktopPath != null ? new File (commonUserDesktopPath) : null;
    }

    public static File getCommonStartMenuDir () {
        final String commonUserStartMenuPath = regQueryCommonStartMenuPath ();
        return commonUserStartMenuPath != null ? new File (commonUserStartMenuPath) : null;
    }

    public static File getCommonProgramsDir () {
        final String commonommonProgramsPath = regQueryCommonProgramsPath ();
        return commonommonProgramsPath != null ? new File (commonommonProgramsPath) : null;
    }

    private static String b2s (byte b[]) {
        // Converts C string to Java String
        int len = 0;
        while (b[len] != 0)
            ++len;
        return new String (b,
                           0,
                           len);
    }

    //TODO: do not delete - for a future use
//    public static String getDiskSerialNumber (String path) {
//        Assert.notNull (path);
//        if (path.indexOf ("\\") != path.length () - 1) {
//            path += "\\";
//        }
//        Kernel32 kernel32 = (Kernel32) Native.loadLibrary ("kernel32",
//                                                           Kernel32.class);
//        byte volName[] = new byte[256], fsName[] = new byte[256];
//        int volSerNbr[] = new int[1], maxCompLen[] = new int[1], fileSysFlags[] = new int[1];
//        boolean ok = kernel32.GetVolumeInformationA (path,
//                                                     volName,
//                                                     256,
//                                                     volSerNbr,
//                                                     maxCompLen,
//                                                     fileSysFlags,
//                                                     fsName,
//                                                     256);
//        if (ok) {
//            boolean showInfo = false;
//            if (showInfo)// for test only
//                System.out.printf ("%s %08X '%s' %s %08X%n",
//                                   path,
//                                   volSerNbr[0],
//                                   b2s (volName),
//                                   b2s (fsName),
//                                   fileSysFlags[0]);
//
//            return String.format ("%08X",
//                                  volSerNbr[0]);
//        } else
//            throw new RuntimeException ("Unable to get HDD volume serial number");
//
//    }

    public static void main (final String[] args) {
        System.out.println ("User Desktop directory : " + regQueryCurrentUserDesktopPath ());

        System.out.println ("User StartMenu directory : " + regQueryCurrentUserStartMenuPath ());

        System.out.println ("Common Desktop directory : " + regQueryCommonDesktopPath ());

        System.out.println ("Common StartMenu directory : " + regQueryCommonStartMenuPath ());

        System.out.println ("Common Programs directory : " + regQueryCommonProgramsPath ());

        System.exit (0);

    }
}
