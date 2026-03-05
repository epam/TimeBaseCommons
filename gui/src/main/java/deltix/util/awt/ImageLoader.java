package deltix.util.awt;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.io.IOUtil;
import com.epam.deltix.util.io.StreamPump;
import com.epam.deltix.util.lang.Util;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.URL;


public class ImageLoader {
	private static final Log LOG = LogFactory.getLog(ImageLoader.class);
	public static final int     MAX_WAIT_MS = 5000;

	public static Image			loadImage (URL url) 
        throws InterruptedException
    {
        Toolkit	tk = Toolkit.getDefaultToolkit ();
		Image   image = tk.createImage (url);        
        Image   loaded = ensureImageIsLoaded (image);        
        
        return (image);
    }
    
	public static Image			loadImage (String relPath) 
        throws FileNotFoundException
    {
		InputStream			is =
			IOUtil.openResourceAsStream (relPath);

        if (is == null)
            return (null);

		try {
			return (loadImage (is));
		} catch (Exception x) {
			LOG.warn ("Failed to read image from path %s: %s").with(relPath).with(x);
			return (null);
		} finally {
			Util.close (is);
		}        
    }
    
	public static Image			loadImage (File file) throws IOException {
		InputStream			is = new FileInputStream (file);

		try {
			return (loadImage (is));
		} catch (Exception x) {
			LOG.warn ("Failed to read image %s: %s").with(file).with(x);
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
                LOG.warn ("Failed to read image");
                return (null); 
            }
        }        	
	}
}
