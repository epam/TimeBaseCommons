package com.epam.deltix.util.io;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Caches local IP address.
 * <p><b>Attention:</b> Doesn't track dynamic IP address changes.</p>      
 */
public abstract class CachedLocalIP {
    private static String localIP = null;

    public static String getIP() {
        if (localIP == null) {
            try {
                final InetAddress addr = InetAddress.getLocalHost();
                final StringBuilder s = new StringBuilder();
                for (byte b : addr.getAddress()) {
                    s.append(String.format("%02x", b & 0xFF));
                }
                localIP = s.toString();
            } catch (UnknownHostException x) {
                throw new com.epam.deltix.util.io.UncheckedIOException(x);
            }
        }
        return localIP;
    }
}
