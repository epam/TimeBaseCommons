package com.epam.deltix.util.net;

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
    public long             getLastModified() throws IOException {
        HttpURLConnection   huc = (HttpURLConnection) url.openConnection ();

        huc.setRequestMethod ("HEAD");
        huc.setDoInput (true);
        huc.setDoOutput (false);
        huc.connect ();

        try {
            final long ts = huc.getLastModified();
            if (ts == 0) {
                final int rc = huc.getResponseCode();
                if (rc == HttpURLConnection.HTTP_NOT_FOUND)
                    throw new FileNotFoundException(url.toString());
                else if (rc != HttpURLConnection.HTTP_OK)
                    throw new IOException("header query failed with HTTP error: " + rc + " " + url.toString());
            }

            return (ts);
        } finally {
            huc.disconnect ();
        }
    }

    @Override
    public InputStream      openStream () throws IOException {
        return (openStream(0));
    }

    @Override
    public InputStream      openStream(int timeout) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        connection.setConnectTimeout(timeout);
        return (connection.getInputStream());
    }

    @Override
    public String           toString () {
        return (url.toString ());
    }

    public URL getUrl () {
        return url;
    }
    
    
}
