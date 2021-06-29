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

import com.epam.deltix.util.lang.*;
import java.io.*;
import java.net.*;

/**
 *
 */
public class TimerServerThread extends Thread {
    private final ServerSocket       ss;

    public TimerServerThread (int port) throws IOException {
        super ("Timer Server Thread on port " + port);
        
        ss = new ServerSocket (port);
        ss.setReuseAddress (true);
    }

    @Override
    public void run () {            
        System.out.println ("Timer Server is up on port " + ss.getLocalPort ());
                
        try {
            for (;;) {
            
                Socket              s = ss.accept ();
                
                new TimerConnectionThread (s).start ();        
            }
        } catch (Throwable x) {
            Util.handleException (x);
        } finally {
            Util.close (ss);
        }        
    }    
}