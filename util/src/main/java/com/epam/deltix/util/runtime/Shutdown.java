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
package com.epam.deltix.util.runtime;

public final class Shutdown {

    private final static Object lock = new Object();
    private static boolean      terminated;
    private static int          code = 0;

    public static void          terminate() {
        synchronized (lock) {
            terminated = true;
        }

        System.exit(1001);
    }

    public static void          asyncTerminate() {
        synchronized (lock) {
            terminated = true;
        }

        asyncExit(1001);
    }

    public static int           getCode() {
        return code;
    }

    public static boolean       isTerminated() {
        synchronized (lock) {
            return terminated;
        }
    }

    public static void          asyncExit(final int code) {
        Shutdown.code = code;
        new Thread() {
            @Override
            public void run() {
                System.exit(code);
            }
        }.start();
    }
}