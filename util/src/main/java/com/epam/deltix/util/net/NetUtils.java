/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.net;

import com.epam.deltix.util.io.ByteArrayOutputStreamEx;
import com.epam.deltix.util.lang.Util;

import javax.xml.bind.DatatypeConverter;
import java.net.*;
import java.io.*;

/**
 *
 */
public class NetUtils {    
    public static final NetUtils    INSTANCE = new NetUtils ();
    public static final int         DEFAULT_TIMEOUT = 1000;
    
    private NetUtils () { }
    
    public boolean          isUrlAccessible (String s) {
        return (isUrlAccessible (s, 1000, 1000));
    }
    
    public boolean          isUrlAccessible (String s, int connectTimeout, int readTimeout) {
        try {
            checkUrl (s, connectTimeout, readTimeout);
            return (true);
        } catch (Throwable x) {
            return (false);
        }
    }
    
    public String           encodeUrl (String s) {
        try {
            return (URLEncoder.encode (s, "UTF-8"));
        } catch (UnsupportedEncodingException x) {
            throw new RuntimeException (x);
        }
    }

    public String           decodeUrl (String s) {
        try {
            return (URLDecoder.decode(s, "UTF-8"));
        } catch (UnsupportedEncodingException x) {
            throw new RuntimeException (x);
        }
    }
    
    public String           formatUrl (
        String                  protocol, 
        String                  host, 
        int                     port
    ) 
    {
        return (formatUrl (protocol, host, port, null));
    }
    
    public String           formatUrl (
        String                  protocol, 
        String                  host, 
        int                     port,
        String                  path
    ) 
    {
        return (formatUrl (protocol, host, port, path, null, null));
    }
    
    public String           formatUrl (
        String                  protocol, 
        String                  host, 
        int                     port,
        String                  path,
        String                  user,
        String                  password
    ) 
    {        
        StringBuilder   sb = new StringBuilder ();
        
        sb.append (protocol);
        sb.append ("://");
        
        if (user != null && !user.isEmpty ()) {
            sb.append (encodeUrl (user));
            
            if (password != null && !password.isEmpty ()) {
                sb.append (':');
                sb.append (encodeUrl (password));
            }
            
            sb.append ('@');
        }
        
        sb.append (encodeUrl (host));
        
        if (port != 0) {
            sb.append (':');
            sb.append (port);
        }
        
        if (path != null) {
            sb.append ('/');
            sb.append (encodeUrl (path));
        }
        
        return (sb.toString ());
    }
    
    public void             checkUrl (String s) 
        throws IOException, InterruptedException 
    {
        checkUrl (s, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
    }
    
    public void             checkUrl (String s, int connectTimeout, int readTimeout) 
        throws IOException, InterruptedException 
    {
        getUrl (s, 1, connectTimeout, readTimeout);
    }
    
    public byte []          getUrl (String s) 
        throws IOException, InterruptedException         
    {
        return (getUrl (s, 1000, 1000));
    }
    
    public byte []          getUrl (String s, int connectTimeout, int readTimeout) 
        throws IOException, InterruptedException         
    {
        return (getUrl (s, Integer.MAX_VALUE, connectTimeout, readTimeout));
    }
    
    public byte []          getUrl (String s, int size) 
        throws IOException, InterruptedException 
    {
        return (getUrl (s, size, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT));
    }
    
    public byte []          getUrl (String s, int size, int connectTimeout, int readTimeout) 
        throws IOException, InterruptedException 
    {
        URL                     url = new URL (s);  
        
        URLConnection           urlConn = url.openConnection ();
       
        urlConn.setConnectTimeout (connectTimeout);
        urlConn.setReadTimeout (readTimeout);
        urlConn.setAllowUserInteraction (false);         
        urlConn.setDoOutput (false);
          
        InputStream             is = urlConn.getInputStream ();
        
        try {            
            ByteArrayOutputStreamEx bos = new ByteArrayOutputStreamEx ();
            
            for (;;) {
                int             cur = bos.size ();
                int             rem = size - cur;
                
                if (rem <= 0)
                    break;
                
                int             n = Math.min (4096, rem);
                
                bos.ensureCapacity (cur + n);
                
                int             numRead = 
                    is.read (bos.getInternalBuffer (), cur, n);
                
                if (numRead < 0)
                    break;
                
                bos.reset (cur + numRead);                                
            }
            
            return (bos.toByteArray ());
        } finally {
            Util.close (is);
        }
    }

    public void authorize (URLConnection connection, String user, String pass){
        String ticket = user + ":" + pass;

        connection.setRequestProperty("Authorization", "Basic " +
                DatatypeConverter.printBase64Binary(ticket.getBytes()));
    }
    
    public static void      main (String [] args) throws Exception {
        byte []         bytes = new NetUtils ().getUrl ("ftp://ftp.funet.fi/pub/standards/RFC/rfc959.txt", 40);
        
        for (byte b : bytes) 
            System.out.print ((char) b);        
    }
}