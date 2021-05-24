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
package com.epam.deltix.util.io;

import com.epam.deltix.util.concurrent.UncheckedInterruptedException;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Externalized GUID seed generation from {@link GUID}
 */
public class GUIDSeed {
    final long           time;
    final int            port;

    public GUIDSeed () {
        ServerSocket socket = null;

        try {
            socket = new ServerSocket ();

            socket.bind (null);

            //
            //  Sleep for 2 ticks to prevent the (extremely unlikely) situation
            //  where somebody else owned the port for a fraction of the previous tick.
            //
            Thread.sleep (2);

            //  Get a time at which we definitely owned the socket ...
            time = System.currentTimeMillis () - 1;
            port = socket.getLocalPort ();
        } catch (InterruptedException x) {
            throw new UncheckedInterruptedException(x);
        } catch (IOException x) {
            throw new com.epam.deltix.util.io.UncheckedIOException(x);
        } finally {
            IOUtil.close (socket);
        }
    }

}