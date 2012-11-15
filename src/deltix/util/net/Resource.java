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
    
    public abstract long            getSize () throws IOException;

    public abstract long            getLastModified () throws IOException;

    public abstract InputStream     openStream () throws IOException;
}
