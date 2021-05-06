package com.epam.deltix.util.io;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * OutputStream which updates a MessageDigest 
 */
public class MessageDigestOutputStream extends OutputStream {
    public final MessageDigest          md;
    
    public MessageDigestOutputStream (MessageDigest md) {
        this.md = md;
    }
    
    public MessageDigestOutputStream (String algo) throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance (algo);
    }
    
    public MessageDigestOutputStream () {
        try {
            this.md = MessageDigest.getInstance ("SHA-256");
        } catch (NoSuchAlgorithmException x) {
            throw new RuntimeException ("No SHA-256??", x);
        }
    }
    
    @Override
    public void     write (int b) throws IOException {
        md.update ((byte) b);
    }

    @Override
    public void     write (byte[] b, int off, int len) throws IOException {
        md.update (b, off, len);
    }        
}
