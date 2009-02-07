package deltix.util.net;

import java.io.*;
import java.net.*;

/**
 *
 */
public class NetResource extends Resource {
    private final URL       url;

    public NetResource (String s) throws MalformedURLException {
        this (new URL (s));
    }

    public NetResource (URL url) {
        this.url = url;
    }

    @Override
    public long             getSize () throws IOException {
        HttpURLConnection   huc = (HttpURLConnection) url.openConnection ();

        huc.setRequestMethod ("HEAD");
        huc.setDoInput (true);
        huc.setDoOutput (false);
        huc.connect ();

        try {
            return (huc.getContentLength ());
        } finally {
            huc.disconnect ();
        }
    }

    @Override
    public InputStream      openStream () throws IOException {
        return (url.openStream ());
    }

    @Override
    public String           toString () {
        return (url.toString ());
    }
}
