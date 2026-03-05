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

import com.epam.deltix.util.lang.Util;
import java.io.*;
import java.net.*;

import com.epam.deltix.util.*;

/**
 *
 */
public class TCPLogger {
    static class Tee extends Thread {
        private OutputStream        mLog;    
        private InputStream         mIn;
        private OutputStream        mOut;
    
        public Tee (String id, InputStream is, OutputStream os) 
            throws IOException
        {
            mLog = new FileOutputStream (id);
            mIn = is;
            mOut = os;
        } 
        
        public void         run () {
            byte []     buf = new byte [1024];
            
            try {
                for (;;) {
                    int     numRead = mIn.read (buf);
                    
                    if (numRead < 0)
                        break;
                    
                    mOut.write (buf, 0, numRead);
                    mOut.flush ();
                    
                    mLog.write (buf, 0, numRead);
                    mLog.flush ();
                }
            } catch (IOException iox) {
                iox.printStackTrace ();
            }       
            
            Util.close (mLog);
        }
    }

    public static void main (String [] args) throws Exception {
        if (args.length == 0) {
            System.out.println ("tcplog <remote host> <local port> <remote port>");
            return;
        }
        
        String              delegateHost = args [0];
        int                 localPort = Integer.parseInt (args [1]);
        int                 delegatePort = Integer.parseInt (args [2]);
        
        ServerSocket        ss = new ServerSocket (localPort); 

        System.out.println ("Server ready on port " + ss.getLocalPort ());

        int                 num = 0;
        
        for (;;) {
            Socket          s = ss.accept ();
            num++;
            
            Socket          delegate = new Socket (delegateHost, delegatePort);
            
            new Tee ("req-" + num + ".txt", s.getInputStream (), delegate.getOutputStream ()).start ();
            new Tee ("resp-" + num + ".txt", delegate.getInputStream (), s.getOutputStream ()).start ();
        }            
    }
}