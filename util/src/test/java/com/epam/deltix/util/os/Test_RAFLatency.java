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
package com.epam.deltix.util.os;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 *
 */
public class Test_RAFLatency {

    private static int          PACKET_SIZE = 16;
    private static int          NUM_TESTS = 1000;
    private static int          NUM_LAUNCHES = 10;
    private static String       RAF_NAME = "test.pipe";

    boolean serverCanRead = false;
    boolean clientCanRead = false;

    @Test
    public void                     testSocketLatency() throws Exception {
        LatencyServer server = new SocketLatencyServer(7788);
        LatencyClient client = new SocketLatencyClient(7788);

        server.start();
        for (int i = 0; i < NUM_LAUNCHES; i++ )
            client.run();
    }

    @Test
    public void                     testNamedPipesLatency() throws Exception {
        //clear raf
        NamedPipe pipe = new NamedPipe(RAF_NAME, "rw");
        pipe.getRAF().setLength(0);
        pipe.close();

        LatencyServer server = new NPLatencyServer();
        LatencyClient client = new NPLatencyClient();

        server.start();
        for (int i = 0; i < NUM_LAUNCHES; i++ )
            client.run();
    }

    @Test
    public void                     testMemoryMappedFilesLatency() throws Exception {
        //clear raf
        NamedPipe pipe = new NamedPipe(RAF_NAME, "rw");
        pipe.getRAF().setLength(0);
        pipe.close();

        LatencyServer server = new MMLatencyServer();
        LatencyClient client = new MMLatencyClient();

        server.start();
        for (int i = 0; i < NUM_LAUNCHES; i++ )
            client.run();
    }

    class SocketLatencyServer extends LatencyServer {
        private ServerSocket    ss;
        private Socket          curSocket;
        DataOutputStream        out;
        DataInputStream         in;

        SocketLatencyServer(int port) throws IOException {
            super(PACKET_SIZE);
            ss = new ServerSocket (port);
            ss.setReuseAddress(true);
        }

        @Override
        protected void accept() throws IOException {
            curSocket = ss.accept();
            in = new DataInputStream(curSocket.getInputStream ());
            out = new DataOutputStream(curSocket.getOutputStream());
        }

        @Override
        protected void close() {
            try {
                curSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void readBuffer(byte[] buf) throws IOException {
            in.readFully(buf);
        }

        public void writeLong(long count) throws IOException {
            out.writeLong(count);
            out.flush();
        }
    }

    class SocketLatencyClient extends LatencyClient {
        private int         port;
        private Socket      socket;
        OutputStream        out;
        DataInputStream     in;

        SocketLatencyClient(int port) {
            super(PACKET_SIZE);
            this.port = port;
        }

        @Override
        protected void connect() throws IOException {
            socket = new Socket("localhost", port);
            out = socket.getOutputStream();
            in = new DataInputStream(socket.getInputStream ());
        }

        @Override
        protected void close() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void writeBuffer(byte[] buf) throws IOException {
            out.write(buf);
            out.flush();
        }

        protected long readLong() throws IOException {
            return in.readLong();
        }
    }

    //--------------------------------------------------------------------

    class NPLatencyServer extends LatencyServer {
        private NamedPipe           pipe;

        NPLatencyServer() throws IOException {
            super(PACKET_SIZE);
            pipe = new NamedPipe(RAF_NAME, "rw");
            pipe.getRAF().setLength(0);
        }

        public void readBuffer(byte[] buf) throws IOException {
            pipe.getRAF().read(buf, 0, buf.length);
        }

        public void writeLong(long count) throws IOException {
            pipe.getRAF().writeLong(count);
        }
    }

    class NPLatencyClient extends LatencyClient {
        private NamedPipe           pipe;

        NPLatencyClient() throws IOException {
            super(PACKET_SIZE);
            pipe = new NamedPipe(RAF_NAME, "rw");
            pipe.getRAF().setLength(0);
        }

        public void writeBuffer(byte[] buf) throws IOException {
            pipe.getRAF().write(buf, 0, buf.length);
        }

        public long readLong() throws IOException {
            return pipe.getRAF().readLong();
        }
    }

    //--------------------------------------------------------------------

    class MMLatencyServer extends LatencyServer {
        long                        memBufSize = 8 * 1024 * 1024;
        MappedByteBuffer            mem;

        MMLatencyServer() throws IOException {
            super(PACKET_SIZE);
            FileChannel fc = new RandomAccessFile(RAF_NAME, "rw").getChannel();
            mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, memBufSize);
        }

        public void readBuffer(byte[] buf) {
            mem.get(buf, 0, buf.length);
        }

        public void writeLong(long count) {
            mem.putLong(count);
        }
    }

    class MMLatencyClient extends LatencyClient {
        long                        memBufSize = 8 * 1024 * 1024;
        MappedByteBuffer            mem;

        MMLatencyClient() throws IOException {
            super(PACKET_SIZE);
            FileChannel fc = new RandomAccessFile(RAF_NAME, "rw").getChannel();
            mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, memBufSize);
        }

        public void writeBuffer(byte[] buf) {
            mem.put(buf, 0, buf.length);
        }

        public long readLong() {
            return mem.getLong();
        }
    }

    //--------------------------------------------------------------------

    abstract class LatencyServer extends Thread {
        private final byte[]        buffer;
        private final byte[]        testBuffer;

        LatencyServer(int packetSize) {
            buffer = new byte[packetSize];
            testBuffer = new byte[packetSize];
            for (int ii = 0; ii < packetSize; ii++)
                testBuffer[ii] = (byte) ii;
        }

        protected void accept() throws IOException { }
        protected void close() { }

        public abstract void readBuffer(byte[] buf) throws IOException;
        public abstract void writeLong(long count) throws IOException;

        @Override
        public void                 run() {
            for (long launch = 0; launch < NUM_LAUNCHES; ++launch) {
                try {
                    accept();
                    for (long test = 0; test < NUM_TESTS; ++test) {
                        while (!serverCanRead)
                            Thread.sleep(0);
                        serverCanRead = false;

                        readBuffer(buffer);
                        assert Arrays.equals(testBuffer, buffer);

                        writeLong(test);
                        clientCanRead = true;
                    }
                } catch (Throwable x) {
                    x.printStackTrace();
                } finally {
                    close();
                }
            }
        }
    }

    abstract class LatencyClient {

        byte[] buffer;

        public LatencyClient(int packetSize) {
            buffer = new byte[packetSize];
            for (int ii = 0; ii < packetSize; ii++)
                buffer[ii] = (byte) ii;
        }

        protected void connect() throws IOException { }
        protected void close() { }

        protected abstract void writeBuffer(byte[] buf) throws IOException;
        protected abstract long readLong() throws IOException;

        public void              run() throws IOException, InterruptedException {
            double[] latencies = new double[NUM_TESTS];

            try {
                connect();
                for (int test = 0; test < NUM_TESTS; test++) {
                    long start = System.nanoTime();

                    writeBuffer(buffer);
                    serverCanRead = true;

                    while (!clientCanRead)
                        Thread.sleep(0);
                    clientCanRead = false;

                    long readTest = readLong();
                    assert (readTest == test);

                    long latency = System.nanoTime() - start;
                    latencies[test] = latency;

                    Thread.sleep(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }

            Arrays.sort(latencies);

            System.out.println("Min: " + latencies[0]);
            System.out.println("Med: " + latencies[NUM_TESTS / 2]);
            System.out.println("90%: " + latencies[NUM_TESTS * 9 / 10]);
            System.out.println("99%: " + latencies[NUM_TESTS * 99 / 100]);
            System.out.println("Max: " + latencies[NUM_TESTS - 1]);

            System.out.println("--------------------------------------------");

            Thread.sleep(100);
        }
    }

}