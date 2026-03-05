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
package com.epam.deltix.util.tomcat;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Inner implementation of Tomcat connection handler (TimeBase, SNMP, FIX)
 */
public interface ConnectionHandshakeHandler {
    /**
     *  Handle the initial transport-level handshake with a client.
     *
     *  @param socket   Socket to perform the handshake with.
     *  @return     true if the connection is accepted and socket added to the
     *              set of transport channels. false if socket should be closed.
     *
     *  @throws IOException
     */
    public boolean      handleHandshake(
            Socket socket,
            BufferedInputStream bis,
            OutputStream os
    ) throws IOException;
}