package deltix.util.jide;

import java.awt.*;
import java.io.*;
import java.util.logging.*;

import javax.swing.*;

import com.jidesoft.dialog.*;

import deltix.util.lang.*;

/**
 *  Colleciton of static utilities
 */
public abstract class JideUtil { 
    
   public static void		    staticHandle (
        Component                   parent, 
        Throwable                   x,
        Logger                      logger,
        Level                       logLevel
    ) 
    {
        x = Util.unwrap (x);
		
        if (logger != null)
            logger.log (logLevel, "Uncaught Exception", x);

        StringWriter swr = new StringWriter();
        PrintWriter trace = new PrintWriter(swr);

        x.printStackTrace(trace);
        trace.close();

        String details = swr.toString();

        String xClassName = x.getClass().getName();
        String msg = x.getLocalizedMessage();

        if (msg == null || msg.length() == 0)
            msg = ("Exception: " + xClassName);

        JideOptionPane optionPane = new JideOptionPane(
                "Click \"Details\" button to see more information ... ",
                JOptionPane.ERROR_MESSAGE,
                JideOptionPane.CLOSE_OPTION);
        optionPane.setTitle(msg);
        optionPane.setDetails(details);

        JDialog dialog = optionPane.createDialog(parent, xClassName);
        dialog.setResizable(true);
        dialog.pack();
        dialog.setVisible(true);
    }
        
    public static void		    staticHandle (
        Component                   parent, 
        Throwable                   x,
        Level                       logLevel        
    )
    {
        staticHandle (parent, x, Util.LOGGER, logLevel);
    }
    
    public static void		    staticHandle (Throwable x) {
        staticHandle (null, x, Level.SEVERE);
    }

    public static void          asyncInvoke (Runnable r) {
        if (SwingUtilities.isEventDispatchThread())
            r.run();
        else
            SwingUtilities.invokeLater (r);
    }

    public static void          asyncHandle (
        final Component             parent, 
        final Throwable             x,
        final Level                 level
    ) 
    {
        asyncHandle (parent, x, Util.LOGGER, level);
    }
    
    public static void          asyncHandle (
        final Component             parent, 
        final Throwable             x,
        final Logger                logger,
        final Level                 level
    ) 
    {
        if (SwingUtilities.isEventDispatchThread())
            staticHandle (parent, x, logger, level);
        else
            SwingUtilities.invokeLater (
                new Runnable () {
                    public void     run () {
                        staticHandle (parent, x, logger, level);
                    }
                }
            );
    }

}
