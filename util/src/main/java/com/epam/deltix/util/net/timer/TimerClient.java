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
package com.epam.deltix.util.net.timer;

import com.epam.deltix.util.io.*;
import com.epam.deltix.util.lang.*;
import com.epam.deltix.util.memory.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.net.*;

/**
 *
 */
public class TimerClient extends Thread {
    private final String                host;
    private final int                   port;
    private Socket                      socket;
    private InputStream                 is;
    private final byte []               buffer = new byte [8];
    private volatile long               correction = 0;
    
    public TimerClient (String host) {
        this (host, TimerServer.DEF_PORT);
    }
    
    public TimerClient (String host, int port) {
        super ("Timer Client Thread <-- " + host + ":" + port);
        
        if (host == null)
            host = "localhost";
        
        this.host = host;
        this.port = port;
        
        setDaemon (true);
    }
    
    public long                         nanoTime () {
        return (System.nanoTime () + correction);
    }

    @SuppressFBWarnings("UNENCRYPTED_SOCKET")
    @Override
    public void                         run () {
        try {
            socket = new Socket (host, port);
        
            socket.setTcpNoDelay (true);
            socket.setSoTimeout (0);
            socket.setKeepAlive (true);
            socket.setReuseAddress (true);

            is = socket.getInputStream ();

            for (int ii = 1; ii < TimerServer.WARMUP_NUM; ii++)
                applyCorrection ();
            
            for (;;) {
                long        oldCorrection = correction;
                
                applyCorrection ();
                
//                    System.out.println (
//                        "Correction adjustment: " + (correction - oldCorrection)
//                    );
            }             
        } catch (Exception x) {
            Util.handleException (x);
        } finally {
            Util.close (socket);
        }
    }

    private void applyCorrection () throws IOException {
        IOUtil.readFully (is, buffer, 0, 8);
        
        long        st = DataExchangeUtils.readLong (buffer, 0);
        
        correction = st - System.nanoTime ();
    }
}