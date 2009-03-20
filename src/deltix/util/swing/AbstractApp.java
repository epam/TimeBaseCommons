package deltix.util.swing;

import deltix.util.lang.Util;
import java.awt.event.*;
import java.util.logging.*;
import java.io.*;
import javax.swing.*;

import deltix.util.*;
import deltix.util.io.*;
import java.lang.Thread.UncaughtExceptionHandler;

public class AbstractApp 
    extends JFrame 
    implements UncaughtExceptionHandler
{    
    public AbstractApp () {
        this (DO_NOTHING_ON_CLOSE);
    }
    
    public AbstractApp (int defaultCloseOperation) {
        setDefaultCloseOperation (defaultCloseOperation);     
        
        addWindowListener (
            new WindowAdapter () {
                @Override
                public void windowClosing (WindowEvent e) {
                    AbstractApp.this.windowClosing (e);
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
        int     ret =
            JOptionPane.showConfirmDialog (
                this,
                msg,
                title,
                JOptionPane.YES_NO_OPTION
            );
        
        if (ret == JOptionPane.YES_OPTION)
            System.exit (0); 
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
        handle (x, Level.SEVERE);
    }
    
    public void		        handle (
        Throwable                   x,
        Level                       logLevel        
    ) 
    {
        handle (x, Util.LOGGER, logLevel);
    }
    
    public void		        handle (
        Throwable                   x,
        Logger                      logger,
        Level                       logLevel
    ) 
    {
        SwingUtil.staticHandle (this, x, logger, logLevel);
    }
    
    public void		        asyncHandle (Throwable x) {
        asyncHandle (x, Level.SEVERE);
    }
    
    public void		        asyncHandle (
        Throwable                   x,
        Level                       logLevel        
    ) 
    {
        asyncHandle (x, Util.LOGGER, logLevel);
    }
    
    public void                 asyncHandle (
        Throwable                   x,
        Logger                      logger,
        Level                       logLevel
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

    @Override
    public void                 setTitle (String title) {
        if (title.indexOf (Version.VERSION_STRING) < 0)
            title = title + " - Version " + Version.VERSION_STRING;
        
        super.setTitle (title);
    }
    
    
}
