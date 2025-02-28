package deltix.util.vsocket;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class Test_ChannelOutputStream {

    /**
     * Ensures that we do not block if we can't flush data and buffer is not yet full (<75%)
     * on {@link ChannelOutputStream#enableFlushing()}.
     */
    @Test
    public void testNoFlushOnPartiallyFull() throws IOException, InterruptedException {
        VSChannelImpl vsChannel = Mockito.mock(VSChannelImpl.class);
        ChannelOutputStream cos = new ChannelOutputStream(vsChannel, 8 * 1024);
        cos.disableFlushing();
        byte[] buf = new byte[1024];
        // 5kb is less than 75% of 8kb
        for (int i = 1; i <= 5; i++) {
            cos.write(buf);
        }
        // No flush, because flushing is disabled
        Mockito.verifyNoInteractions(vsChannel);

        cos.enableFlushing();
        // No flush, because remote capacity is 0
        Mockito.verifyNoInteractions(vsChannel);

        cos.addAvailableCapacity(8*1024);
        cos.disableFlushing();
        cos.enableFlushing();
        // Data flushed
        verify(vsChannel).send(Mockito.any(), eq(0), eq(5 * 1024));
    }


    /**
     * Ensures that we block if we can't flush data and buffer is almost full (>75%)
     * on {@link ChannelOutputStream#enableFlushing()}.
     */
    @Test
    public void testBlockingFlushOnAlmostFull() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        VSChannelImpl vsChannel = Mockito.mock(VSChannelImpl.class);
        ChannelOutputStream cos = new ChannelOutputStream(vsChannel, 8 * 1024);
        cos.disableFlushing();
        byte[] buf = new byte[1024];
        // 7kb is more than 75% of 8kb
        for (int i = 1; i <= 7; i++) {
            cos.write(buf);
        }
        // No flush, because flushing is disabled
        Mockito.verifyNoInteractions(vsChannel);

        // We are expected to block here
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                cos.enableFlushing();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Thread.sleep(100);
        assertFalse("We expect enableFlushing() to be blocked yet", future.isDone());

        // Add capacity
        cos.addAvailableCapacity(8*1024);
        future.get(100, TimeUnit.MILLISECONDS);
        assertTrue("enableFlushing() is expected to finish", future.isDone());

        // Data flushed
        verify(vsChannel).send(Mockito.any(), eq(0), eq(7 * 1024));
    }
}
