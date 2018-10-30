package deltix.util.swing;

import deltix.gflog.Log;
import deltix.gflog.LogLevel;
import deltix.util.io.IOUtil;
import deltix.util.lang.Util;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

public class SwingAbstractApp
    extends JFrame
    implements UncaughtExceptionHandler
{

    public static final String LOOK_AND_FEEL_FAILURE_CLASS = "com.sun.java.swing.plaf.windows.XPStyle$Skin";

    public SwingAbstractApp() {
        this (DO_NOTHING_ON_CLOSE);
    }

    public SwingAbstractApp(int defaultCloseOperation) {
        setDefaultCloseOperation (defaultCloseOperation);     
        
        addWindowListener (
            new WindowAdapter () {
                @Override
                public void windowClosing (WindowEvent e) {
                    SwingAbstractApp.this.windowClosing (e);
                }
            }
        );
        
        AppExceptionHandler.currentApp = this;
    }
    
    protected void          windowClosing (WindowEvent e) {        
    }
    
    /**
     *  Ask the user if he wants to exit, and call System.exit (0) if so.
     */
    public void             confirmSystemExit (String title, String msg) {
        if (yesNo (title, msg))
            System.exit (0); 
    }    

    public boolean          yesNo (String title, String msg) {
        int     ret =
            JOptionPane.showConfirmDialog (
                this,
                msg,
                title,
                JOptionPane.YES_NO_OPTION
            );

        return (ret == JOptionPane.YES_OPTION);
    }

    public void             showError (String title, String msg) {
        JOptionPane.showMessageDialog (
            this,
            msg,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public void		        handle (Throwable x) {
        handle (x, LogLevel.ERROR);
    }
    
    public void		        handle (
        Throwable                   x,
        LogLevel                       logLevel
    ) 
    {
        handle (x, SwingUtil.LOGGER, logLevel);
    }
    
    public void		        handle (
        Throwable                   x,
        Log logger,
        LogLevel                       logLevel
    ) 
    {
        SwingUtil.staticHandle (this, x, logger, logLevel);
    }
    
    public void		        asyncHandle (Throwable x) {
        asyncHandle (x, LogLevel.ERROR);
    }
    
    public void		        asyncHandle (
        Throwable                   x,
        LogLevel                       logLevel
    ) 
    {
        asyncHandle (x, SwingUtil.LOGGER, logLevel);
    }
    
    public void                 asyncHandle (
        Throwable                   x,
        Log                      logger,
        LogLevel                       logLevel
    ) 
    {
        SwingUtil.asyncHandle (this, x, logger, logLevel);
    }
    
    public void                 syncInvoke (Runnable r)
        throws InterruptedException 
    {
        try {
            if (SwingUtilities.isEventDispatchThread ())
                r.run ();
            else
                SwingUtilities.invokeAndWait (r);
        } catch (java.lang.reflect.InvocationTargetException x) {
            handle (x);
        }
    }
    
    public void                 printUsage () throws IOException, InterruptedException  {
        String      cname = getClass ().getName ();
        int         dot = cname.lastIndexOf ('.');
        String      path;
        
        if (dot > 0)
            path = cname.substring (0, dot + 1).replace ('.', '/') + "usage.txt";
        else
            path = "usage.txt";
        
        IOUtil.copyResource (path, System.out);
    }
    
    public void                 uncaughtException (Thread t, Throwable e) {
        asyncHandle (e);
    }
    
    public void                 installExceptionHandler () {
        Thread.setDefaultUncaughtExceptionHandler (this);
        System.setProperty ("sun.awt.exception.handler", AppExceptionHandler.class.getName ());
    }


    //temporary error catching solution
    //if NPE or InternalError has been thrown and stack trace contains link on the LOOK_AND_FEEL_FAILURE_CLASS
    //then we make decision to hide error on UI;
    //this stub doesn't influence on app functionality
    public static boolean lookAndFeelRepaintIssue(Throwable t) {
        try {
            if (!(t instanceof NullPointerException || t instanceof InternalError)) {
                return false;
            }

            StackTraceElement[] stackTrace = t.getStackTrace();
            for (StackTraceElement elem : stackTrace) {
                if (LOOK_AND_FEEL_FAILURE_CLASS.equals(elem.getClassName())) {
                    return true;
                }
            }
        } catch (Throwable x) {
            return false;
        }

        return false;
    }
    
}
