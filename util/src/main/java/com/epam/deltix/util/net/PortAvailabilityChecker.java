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

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 */
public class PortAvailabilityChecker {
    public static boolean       isPortAvailable (int n) {
        try {
            ServerSocket    ss = new ServerSocket (n);

            ss.close ();

            return (true);
        } catch (java.net.BindException x) {
            return (false);
        } catch (IOException iox) {
            throw new com.epam.deltix.util.io.UncheckedIOException(iox);
        }
    }

    public static void main (String [] args) {
        System.out.println (isPortAvailable (Integer.parseInt (args [0])));
    }
}