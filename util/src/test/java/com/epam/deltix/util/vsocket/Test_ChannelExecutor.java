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

package com.epam.deltix.util.vsocket;

import com.epam.deltix.util.lang.DisposableListener;
import com.epam.deltix.util.vsocket.ChannelExecutor;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Test_ChannelExecutor {

    @Test(timeout = 10_000)
    public void testFlush() throws InterruptedException {
        ChannelExecutor channelExecutor = ChannelExecutor.createNonSharedTestInstance(null);

        StubVSChannel channel1 = new StubVSChannel(false);
        StubVSChannel channel2 = new StubVSChannel(true);
        channelExecutor.addChannel(channel1);
        channelExecutor.addChannel(channel2);

        while (!channel2.flushed) {
            Thread.sleep(1);
        }

        assertFalse(channel1.flushed);
        assertTrue(channel2.flushed);

        channelExecutor.shutdown();
    }

    private static class StubVSChannel implements VSChannel {
        private final boolean noDelay;
        private final StubVSOutputStream stubVSOutputStream = new StubVSOutputStream();
        private volatile boolean flushed = false;

        public StubVSChannel(boolean noDelay) {
            this.noDelay = noDelay;
        }

        @Override
        public int getLocalId() {
            return 0;
        }

        @Override
        public int getRemoteId() {
            return 0;
        }

        @Override
        public String getRemoteAddress() {
            return "";
        }

        @Override
        public String getClientAddress() {
            return "";
        }

        @Override
        public String getRemoteApplication() {
            return "";
        }

        @Override
        public String getClientId() {
            return "";
        }

        @Override
        public VSOutputStream getOutputStream() {
            return stubVSOutputStream;
        }

        @Override
        public DataOutputStream getDataOutputStream() {
            return null;
        }

        @Override
        public InputStream getInputStream() {
            return null;
        }

        @Override
        public DataInputStream getDataInputStream() {
            return null;
        }

        @Override
        public VSChannelState getState() {
            return VSChannelState.Connected;
        }

        @Override
        public boolean setAutoflush(boolean value) {
            return false;
        }

        @Override
        public boolean isAutoflush() {
            return false;
        }

        @Override
        public void close(boolean terminate) {

        }

        @Override
        public void setAvailabilityListener(Runnable lnr) {

        }

        @Override
        public Runnable getAvailabilityListener() {
            return null;
        }

        @Override
        public boolean getNoDelay() {
            return noDelay;
        }

        @Override
        public void setNoDelay(boolean value) {

        }

        @Override
        public String encode(String value) {
            return "";
        }

        @Override
        public String decode(String value) {
            return "";
        }

        @Override
        public void addDisposableListener(DisposableListener<VSChannel> listener) {

        }

        @Override
        public void removeDisposableListener(DisposableListener<VSChannel> listener) {

        }

        @Nullable
        @Override
        public String getTag() {
            return "";
        }

        @Override
        public void setTag(@Nullable String tag) {

        }

        @Override
        public void close() {

        }

        private class StubVSOutputStream extends VSOutputStream {
            @Override
            public void enableFlushing() throws IOException {

            }

            @Override
            public void disableFlushing() {

            }

            @Override
            public int flushAvailable(boolean flushAll) throws IOException {
                StubVSChannel.this.flushed = true;
                return 0;
            }

            @Override
            public void write(int b) throws IOException {

            }
        }
    }
}
