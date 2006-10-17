package deltix.util.swing;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;

import deltix.util.Util;
import deltix.util.io.StreamPump;

/**
 *  Colleciton of static utilities
 */
public abstract class SwingUtil {    
    public static final String  ERROR_TITLE =
        ResourceBundle.getBundle ("deltix/util/swing/exceptions").getString ("errorTitle");
    
    public static void          expandEntireTree (JTree tree) {
        for (int ii = 0; ii < tree.getRowCount(); ii++) {
            tree.expandRow (ii);
        }
    }
    
    public static ResourceBundle    getBundle (String name) {
        try {
            return (ResourceBundle.getBundle (name));
        } catch (MissingResourceException mrx) {
            staticHandle (mrx);
            System.exit (1);
            return (null);
        }
    }

    public static ImageIcon	    loadIcon (String relPath) {
            Image       img = loadImage (relPath);
        
        return (img == null ? null : new ImageIcon (img));
    }

    public static Image			loadImage (String relPath) {
        InputStream			is =
                Util.class.getClassLoader ().getResourceAsStream (relPath);

    if (is == null)
        return (null);

            try {
                    return (loadImage (is));
            } catch (Throwable x) {
                    Util.LOGGER.log (
            Level.WARNING, 
            "Failed to read image from relative path " + relPath,
            x
        );
                    return (null);
            } finally {
                    Util.close (is);
            }        
    }
    
	public static Image			loadImage (File file) throws IOException {
		InputStream			is = new FileInputStream (file);

		try {
			return (loadImage (is));
		} catch (Throwable x) {
			Util.LOGGER.log (
                Level.WARNING, 
                "Failed to read image file " + file,
                x
            );
			return (null);
		} finally {
			Util.close (is);
		}        
    }
    
	public static Image			loadImage (InputStream is)
		throws InterruptedException, IOException
	{
    	ByteArrayOutputStream	baos = new ByteArrayOutputStream (100000);

    	try {
	    	StreamPump.pump (is, baos);
        } finally {
        	Util.close (is);
        }

		return (loadImage (baos.toByteArray ()));
	}

	public static Image			loadImage (byte [] bytes)
		throws InterruptedException
	{
		Toolkit			tk = Toolkit.getDefaultToolkit ();
		Image			img = tk.createImage (bytes);

		ensureImageIsLoaded (img);

		return (img);
	}

	public static Image			ensureImageIsLoaded (Image img)
		throws InterruptedException
	{
		Toolkit			tk = Toolkit.getDefaultToolkit ();

		while (!tk.prepareImage (img, -1, -1, null))
			Thread.sleep (1);

		return (img);
	}
    
	protected SwingUtil () {
	}

	private static String		getMsg (Throwable x) {
		String		msg = x.getLocalizedMessage ();

		if (msg == null || msg.length () == 0)
			return ("Exception: " + x.getClass ().getName ());

		return (msg);
	}

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

    	JOptionPane.showMessageDialog (
    		parent,
    		getMsg (x),
    		x.getClass ().getName (),
    		JOptionPane.ERROR_MESSAGE
    	);
    }
        
    public static void		    staticHandle (Component parent, Throwable x) {
        staticHandle (parent, x, Util.LOGGER, Level.WARNING);
    }
    
    public static void		    staticHandle (Throwable x) {
        staticHandle (null, x);
    }

    public static void                 asyncInvoke (Runnable r) {
        if (SwingUtilities.isEventDispatchThread())
            r.run();
        else
            SwingUtilities.invokeLater (r);
    }

    public static void          setWindowsLookAndFeel () {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable e) {
        } 
    }
}
