package deltix.util.net;

import java.io.*;
import java.net.MalformedURLException;

/**
 *
 */
public abstract class Resource {
    public static Resource          create (String url)
        throws MalformedURLException 
    {
        File    local = new File (url);

        if (local.exists ())
            return (new FileResource (local));

        return (new NetResource (url));
    }

    public static Resource          create2 (String url)
            throws IOException
    {
        if (url.startsWith("http:"))
            return new NetResource(url);

        final File f = new File(url);
        if (f.exists())
            return new FileResource(f);
        else
            throw new FileNotFoundException(url);
    }

    public abstract long            getSize () throws IOException;

    public abstract long            getLastModified () throws IOException;

    public abstract InputStream     openStream () throws IOException;
}
