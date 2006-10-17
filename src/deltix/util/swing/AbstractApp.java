package deltix.util.swing;

import java.util.logging.*;
import java.io.*;
import javax.swing.*;

import deltix.util.io.*;

public abstract class AbstractApp extends JFrame {    
    protected AbstractApp () {
        setDefaultCloseOperation (EXIT_ON_CLOSE);        
    }
    
    public void		        handle (Throwable x) {
        SwingUtil.staticHandle (this, x);
    }
    
    public void		        handle (
        Throwable                   x,
        Logger                      logger,
        Level                       logLevel
    ) 
    {
        SwingUtil.staticHandle (this, x, logger, logLevel);
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
    
    public void		            syncHandle (final Throwable x)
        throws InterruptedException 
    {
        syncInvoke (
            new Runnable () {
                public void run () {
                    handle (x);
                }
            }
        );
    }
    
    public void                     printUsage () throws IOException, InterruptedException  {
        String      cname = getClass ().getName ();
        int         dot = cname.lastIndexOf ('.');
        String      path;
        
        if (dot > 0)
            path = cname.substring (0, dot + 1).replace ('.', '/') + "usage.txt";
        else
            path = "usage.txt";
        
        IOUtil.copyResource (path, System.out);
    }
}
