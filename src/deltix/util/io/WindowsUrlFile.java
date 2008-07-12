package deltix.util.io;

import deltix.util.lang.Util;
import java.io.*;

/**
 *  Creates a Windows .url file.
 */
public class WindowsUrlFile {
/*
 * 
[InternetShortcut]

URL=http://www.someaddress.com/

WorkingDirectory=C:\WINDOWS\

ShowCommand=7

IconIndex=1

IconFile=...

Modified=20F06BA06D07BD014D

HotKey=1601


 * 
ShowCommand
(Nothing) - Normal

7         - Minimized

3         - Maximized

Note: this setting does not seem to appear in some versions of Internet Explorer/Windows.

 

 

HotKey
The HotKey field specifies what is the shortcut key used to automatically launch the Internet shortcut. The field uses a number to specify what hotkey is used.

 

833 � Ctrl + Shift + A

834 � Ctrl + Shift + B

835 � Ctrl + Shift + C

.

.

1345 � Shift + Alt + A

1346 � Shift + Alt + B

1347 � Shift + Alt + C

.

.

1601 � Ctrl + Alt + A

1602 � Ctrl + Alt + B

1603 � Ctrl + Alt + C

.

Refer to Appendix A for a more complete table of hotkeys.


 */    
    private String              mName;
    private String              mTarget;
    private String              mIconFile = null;
    private int                 mIconIndex;
    
    public WindowsUrlFile (String name, String target) {
        mName = name;
        mTarget = target;
    }
    
    public void                 setIcon (String file, int index) {
        mIconFile = file;
        mIconIndex = index;
    }
    
    public void                 setIcon (String file) {
        mIconFile = file;
        mIconIndex = 0;
    }
    
    public void                 save (File dir) throws IOException {
        PrintStream     ps = new PrintStream (new File (dir, mName + ".url"));
        
        try {
            ps.print ("[InternetShortcut]\r\n");
            ps.printf ("URL=%s\r\n", mTarget);
            
            if (mIconFile != null) {
                ps.printf ("IconIndex=%d\r\n", mIconIndex);
                ps.printf ("IconFile=%s\r\n", mIconFile);
            }
        } finally {
            Util.close (ps);
        }
    }
}
