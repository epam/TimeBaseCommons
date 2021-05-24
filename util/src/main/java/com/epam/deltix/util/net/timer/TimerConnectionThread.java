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

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.memory.*;
import java.io.*;
import java.net.*;

/**
 *
 */
public class TimerConnectionThread extends Thread {
    private static final Log            LOG = LogFactory.getLog(Util.class);
    private final Socket                s;
    private final OutputStream          os;
    private final byte []               buffer = new byte [8];
    
    public TimerConnectionThread (Socket s) throws IOException {
        super ("Timer Connection Thread from " + s);
        
        this.s = s;
        this.os = s.getOutputStream ();
    }

    @Override
    public void                         run () {   
        LOG.info ( "%s connected.").with(s.getRemoteSocketAddress ());
                                
        try {
            for (int ii = 0; ii < TimerServer.WARMUP_NUM; ii++) {
                sendTime ();
            }
                        
            for (;;) {
                sendTime ();
                
                Thread.sleep (1000);
            }
        } catch (Exception x) {
            if ((x instanceof SocketException) && x.getMessage ().contains ("reset by peer"))
                LOG.info ("%s disconnected").with(s.getRemoteSocketAddress ());
            else
                LOG.info ("Connection %s produced error %s").with(s.getRemoteSocketAddress ()).with(x);
        } finally {
            Util.close (s);
        }
    } 

    private void sendTime () throws IOException {
        DataExchangeUtils.writeLong (buffer, 0, System.nanoTime ());
        os.write (buffer);
        os.flush ();
    }
}