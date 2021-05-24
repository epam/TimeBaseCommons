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
package com.epam.deltix.qsrv.hf.framework.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.epam.deltix.qsrv.hf.spi.conn.DisconnectEventListener;
import com.epam.deltix.qsrv.hf.spi.conn.Disconnectable;
import com.epam.deltix.qsrv.hf.spi.conn.ReconnectableImpl;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DisconnectableUtil {

    public static final Disconnectable CONNECTED_DISCONNECTABLE = new Disconnectable() {
        @Override
        public void addDisconnectEventListener(DisconnectEventListener listener) {
        }

        @Override
        public void removeDisconnectEventListener(DisconnectEventListener listener) {
        }

        @Override
        public boolean isConnected() {
            return true;
        }
    };
    
    public static boolean waitConnectionResult(Disconnectable connection, long timeout) throws InterruptedException, TimeoutException {
        final AtomicBoolean hasResult = new AtomicBoolean(false);
        final DisconnectEventListener listener = new DisconnectEventListener() {
            @Override
            public synchronized void onDisconnected() {
                hasResult.set(true);
                this.notifyAll();
            }

            @Override
            public synchronized void onReconnected() {
                hasResult.set(true);
                this.notifyAll();
            }
        };
        
        synchronized (listener) {
            try {
                // subscribe
                connection.addDisconnectEventListener(listener);
                
                if (connection.isConnected())
                    return true;                
                
                if (connection instanceof ReconnectableImpl) {
                    if (((ReconnectableImpl)connection).getNumReconnectAttempts() != 0)
                        return connection.isConnected();
                } 
                
                // wait if needed
                final long pause = timeout / 10;
                do {
                    if (hasResult.get()) {
                        return connection.isConnected();                            
                    }
                    listener.wait(pause);
                } while ((timeout -= pause) > 0);
                
                throw new TimeoutException("Unable to get connection result");
               
            } finally {
                // unsubscribe
                connection.removeDisconnectEventListener(listener);
            }            
        }

    }        

    public static boolean waitConnected(Disconnectable connection, long timeout) throws InterruptedException {
        if (connection.isConnected())
            return true;

        final CountDownLatch latch = new CountDownLatch(1);
        // create listener
        DisconnectEventListener listener = new DisconnectEventListener() {
            @Override
            public void onDisconnected() {
            }

            @Override
            public void onReconnected() {
                latch.countDown();
            }
        };

        try {
            // subscribe
            connection.addDisconnectEventListener(listener);
            // wait if needed
            boolean result = true;
            if (!connection.isConnected()) {
                if (timeout > 0) {
                    result = latch.await(timeout, TimeUnit.MILLISECONDS);
                } else {
                    latch.await();
                }
            }
            // return result
            return result;
        } finally {
            // unsubscribe
            connection.removeDisconnectEventListener(listener);
        }
    }
}