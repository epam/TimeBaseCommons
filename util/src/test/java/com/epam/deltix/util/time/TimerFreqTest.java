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
package com.epam.deltix.util.time;

import java.util.concurrent.locks.LockSupport;

/**
 * This test is used to measure effect of <a href="https://bugs.openjdk.org/browse/JDK-6435126">JDK-6435126</a>
 * and solution that is described there on Windows.
 */
public class TimerFreqTest {

    private static final boolean ENABLE_WINDOWS_TIMER_RESOLUTION_FIX = true;

    public static void main(String[] args) throws InterruptedException {
        // Without the fix each outer loop produces 10000+ ms results on Windows
        // With fix each outer loop produces 1000-2000 ms results on Windows

        if (ENABLE_WINDOWS_TIMER_RESOLUTION_FIX) {
            magicFix();
        }

        while (true) {
            long t0 = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                LockSupport.parkNanos (1_000_000); // 1 ms
                //Thread.sleep(1);
            }
            long t1 = System.nanoTime();
            System.out.println((t1 - t0) / 1_000_000d + " ms");
        }
    }

    private static void magicFix() {
        Thread magic =
                new Thread("Windows System Clock Speeder-Upper") {
                    @Override
                    @SuppressWarnings("SleepWhileInLoop")
                    public void run() {
                        for (; ; ) {
                            try {
                                Thread.sleep(Integer.MAX_VALUE);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                };
        magic.setDaemon(true);
        magic.start();
    }
}
