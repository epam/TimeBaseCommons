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

import com.epam.deltix.util.lang.StringUtils;

/**
 *
 */
public final class IPEndpoint {
    private final String                host;
    private final int                   port;
    private final IPType                protocol;
    
    public IPEndpoint (
        String                          host, 
        int                             port, 
        IPType                          protocol
    )
    {
        if (host == null || host.isEmpty ())
            throw new IllegalArgumentException ("Empty or null host");
        
        if (port < 1 || port > 65535)
            throw new IllegalArgumentException ("Illegal port: " + port);
        
        if (protocol == null)
            throw new IllegalArgumentException ("Null protocol");
        
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    public String               getHost () {
        return host;
    }

    public int                  getPort () {
        return port;
    }

    public IPType               getProtocol () {
        return protocol;
    }

    @Override
    public boolean              equals (Object obj) {
        if (this == obj)
            return (true);
        
        if (!(obj instanceof IPEndpoint))
            return (false);
                    
        final IPEndpoint    that = (IPEndpoint) obj;
        
        return (
            this.host.equals (that.host) && 
            this.port == that.port &&
            this.protocol == that.protocol
        );        
    }

    @Override
    public int                  hashCode () {
        int     hash = 7;
        hash = 31 * hash + this.host.hashCode ();
        hash = 31 * hash + this.port;
        hash = 31 * hash + this.protocol.hashCode ();
        
        return hash;
    }
    
    @Override
    public String               toString () {
        return (protocol + ":" + host + ":" + port);
    }
    
    public static IPEndpoint    valueOf (String s) {
        int     a = s.indexOf (':');
        int     b = s.lastIndexOf (':');
        
        if (a < 0 || b < 0 || a >= b)
            throw new NumberFormatException (s);
        
        return (
            new IPEndpoint (
                s.substring (a + 1, b), 
                Integer.parseInt (s.substring (b + 1)),
                IPType.valueOf (s.substring (0, a))
            )
        );
    }
}