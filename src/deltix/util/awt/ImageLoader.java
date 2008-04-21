package deltix.util.awt;

import deltix.util.Util;
import deltix.util.io.IOUtil;
import deltix.util.io.StreamPump;
import java.net.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;


public class ImageLoader {    
    public static final int     MAX_WAIT_MS = 5000;
    
	public static Image			loadImage (URL url) 
        throws InterruptedException
    {
        Toolkit	tk = Toolkit.getDefaultToolkit ();
		Image   image = tk.createImage (url);        
        Image   loaded = ensureImageIsLoaded (image);        
        
        return (image);
    }
    
	public static Image			loadImage (String relPath) throws IOException {
		InputStream			is =
			IOUtil.openResourceAsStream (relPath);

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
        
		tk.prepareImage (img, -1, -1, null);   
        
        while (true) {
            Thread.sleep(1);
            int         ok = tk.checkImage(img, -1, -1, null);
            
            if ((ok & ImageObserver.ALLBITS) != 0)
                return (img);
            
            if ((ok & ImageObserver.ABORT) != 0 || (ok & ImageObserver.ERROR) != 0) {
                Util.LOGGER.log (
                    Level.WARNING, 
                    "Failed to read image"
                );
                return (null); 
            }
        }        	
	}
}
