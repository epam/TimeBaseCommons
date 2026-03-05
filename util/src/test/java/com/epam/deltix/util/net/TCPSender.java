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
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import com.epam.deltix.util.text.CharSequenceCounter;

/**
 * Establishes connection to given server and start sending sample FIX message (each message will have unique FIX tag 34)
 */
class TCPSender extends Thread {

    private final CharSequenceCounter counter;

    private final int length;
    private final byte[] buffer;
    private final OutputStream stream;
    private final long delayNs;

    public TCPSender(int rate, OutputStream stream) throws IOException {
        this(TCPConfiguration.MESSAGE, rate, stream);
    }

    private TCPSender(String message, int rate, OutputStream stream) throws IOException {
        this.buffer = message.getBytes();
        this.length = buffer.length;
        this.stream = stream;
        this.counter = new CharSequenceCounter(buffer, message.indexOf("0000000000"));
        this.delayNs = (int) TimeUnit.SECONDS.toNanos(1) / rate;

        System.out.println("Theoretical rate " + rate + " msg/sec");
        System.out.println("Message interval " + delayNs + " ns");
        System.out.println("Message size " + length + " bytes");
    }

    public void run() {
        try {
            for (long next = System.nanoTime(); ; ) {
                for (long now = System.nanoTime(); next <= now; next += delayNs) {
                    sendMessage();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() throws IOException {
        counter.increment();
        stream.write(buffer, 0, length);
    }

}