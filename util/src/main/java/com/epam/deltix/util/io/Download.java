package com.epam.deltix.util.io;

import java.io.*;
import java.net.*;

/**
 *
 */
public class Download {
    
    public static void main (String [] args) throws Exception {
        URL                 url = new URL (args [0]);
        FileOutputStream    os = new FileOutputStream (args [1]);        
        InputStream         is = url.openStream ();
        
        StreamPump.pump (is, os);
        
        os.close ();
    }
    
}
