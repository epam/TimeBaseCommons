package deltix.util.net;

import deltix.util.text.CharSequenceCounter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/** Establishes connection to given server and start sending sample FIX message (each message will have unique FIX tag 34) */
class TCPSender extends Thread {
    private static final int MAX_RATE = 5000;

    // the simplest way to make some kind of sequence inside FIX message (we are not expecting it to be parsed by FIX)
    private static final String MSG = "8=FIX.4.4\u00019=189\u000135=D\u000134=00000000000000000000\u000149=DEMO2Kweoj_DEMOFIX\u000152=20130605-15:41:28.638\u000156=DUKASCOPYFIX\u000111=5080\u000115=EUR\u000121=1\u000138=10000\u000140=Q\u000144=1.209\u000154=1\u000155=EUR/USD\u000159=6\u000160=20130605-15:41:28.638\u0001126=20130605-15:46:23.000\u000110=246\u0001";
    private final CharSequenceCounter counter;

    private final long intervalBetweenMessagesInNanos;
    private final int blockSize;
    private final byte[] buf;
    private final OutputStream os;

    public TCPSender(String message, int rateMessagesPerSec, OutputStream os) throws IOException {
        this.buf = message.getBytes();
        this.blockSize = buf.length;
        this.os = os;

        if (rateMessagesPerSec >= MAX_RATE)
            intervalBetweenMessagesInNanos = 0;
        else
            intervalBetweenMessagesInNanos = (int) TimeUnit.SECONDS.toNanos(1) / rateMessagesPerSec;

        this.counter = new CharSequenceCounter(buf, message.indexOf("\u000134=")+4);
    }

    public TCPSender(int rateMessagesPerSec, OutputStream os) throws IOException {
        this (MSG, rateMessagesPerSec, os);
    }

    public void run() {
        try {
            while (true) {
                long nextNanoTime = (intervalBetweenMessagesInNanos != 0) ? System.nanoTime() + intervalBetweenMessagesInNanos : 0;

                while (true) {
                    if (intervalBetweenMessagesInNanos != 0) {
                        if (System.nanoTime() < nextNanoTime)
                            continue; // spin-wait
                    }

                    sendMessage();

                    nextNanoTime += intervalBetweenMessagesInNanos;
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() throws IOException {
        counter.increment();
        os.write(buf, 0, blockSize);
    }
}
