package deltix.util.net;

import java.io.*;

/**
 *
 */
public abstract class Resource {
    public abstract long            getSize () throws IOException;

    public abstract InputStream     openStream () throws IOException;
}
